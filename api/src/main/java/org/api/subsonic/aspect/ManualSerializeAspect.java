package org.api.subsonic.aspect;

import cn.hutool.core.comparator.VersionComparator;
import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.subsonic.common.ErrorEnum;
import org.api.subsonic.common.SubsonicResult;
import org.api.subsonic.utils.SubsonicUserUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.mybatis.pojo.SysUserPojo;
import org.jetbrains.annotations.NotNull;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Aspect
@Slf4j
public class ManualSerializeAspect {
    
    private final SubsonicUserCache subsonicUserCache;
    
    public ManualSerializeAspect(SubsonicUserCache subsonicUserCache) {
        this.subsonicUserCache = subsonicUserCache;
    }
    
    @Pointcut("@annotation(org.api.subsonic.aspect.ManualSerialize)")
    public void pointCut() {
    }
    
    /**
     * 环绕通知：
     * 注意:Spring AOP的环绕通知会影响到AfterThrowing通知的运行,不要同时使用
     * <p>
     * 环绕通知第一个参数必须是org.aspectj.lang.ProceedingJoinPoint类型
     */
    @Around(value = "pointCut()")
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) {
        log.debug("@ManualSerialize环绕通知: {}", proceedingJoinPoint.getSignature());
        Object[] args = proceedingJoinPoint.getArgs();
        // 取请求中第一个公共参数
        BeanMap beanMap = BeanMap.create(args[0]);
        String from = Objects.toString(beanMap.get("f"), null);
        String user = Objects.toString(beanMap.get("u"), null);
        String password = Objects.toString(beanMap.get("p"), null);
        String version = Objects.toString(beanMap.get("v"), null);
        String token = Objects.toString(beanMap.get("t"), null);
        String salt = Objects.toString(beanMap.get("s"), null);
        return getStringResponseEntity(proceedingJoinPoint, from, user, password, version, token, salt);
    }
    
    @NotNull
    private Object getStringResponseEntity(ProceedingJoinPoint proceedingJoinPoint, String from, String user, String password, String version, String token, String salt) {
        try {
            authenticate(user, password, token, salt, version);
            // 执行目标方法
            return proceedingJoinPoint.proceed();
        } catch (BaseException e) {
            log.error(e.getResultMsg(), e);
            // 对自定义异常进行转换
            ErrorEnum errorEnum = ExceptionTransform.get(e);
            if (Objects.isNull(errorEnum)) {
                throw e;
            }
            return new SubsonicResult().error(StringUtils.equalsIgnoreCase(from, "json"), errorEnum);
        } catch (Throwable e) {
            // 不能使用spring boot 异常拦截器, 因为返回值需要根据请求参数进行转换
            log.error(e.getMessage(), e);
            return new SubsonicResult().error(StringUtils.equalsIgnoreCase(from, "json"), ErrorEnum.A_GENERIC_ERROR);
        } finally {
            SubsonicUserUtil.removeUser();
        }
    }
    
    /**
     * 兼容1.12.0之前的版本
     *
     * @param user     用户名
     * @param password 用户密码
     * @param token    token 高版本会用到
     * @param salt     盐
     * @param version  客户端版本
     */
    private void authenticate(String user, String password, String token, String salt, String version) {
        // 目标API版本为1.12.0或更低版本，则通过以明文或十六进制编码的形式发送密码来执行身份验证。示例：
        // http://your-server/rest/ping.view?u=joe&p=sesame&v=1.12.0&c=myapp
        // http://your-server/rest/ping.view?u=joe&p=enc:736573616d65&v=1.12.0&c=myapp
        
        // 大于1.12版本，而且必须不是以enc:开头, password字段为空。使用身份验证令牌
        int compare = VersionComparator.INSTANCE.compare(version, "1.12.0");
        boolean protocolCheckflag = compare > 0 && !StringUtils.startsWithIgnoreCase(password, "enc:");
        if (protocolCheckflag || StringUtils.isBlank(password)) {
            // 从API版本1.13.0开始，推荐的身份验证方案是发送身份验证令牌，计算为密码的单向加盐哈希。
            // This involves two steps: 这涉及两个步骤：
            
            // For each REST call, generate a random string called the salt. Send this as parameter s.
            // 对于每个REST调用，生成一个称为salt的随机字符串。将其作为参数 s 发送。
            
            // Use a salt length of at least six characters.
            // 使用至少六个字符的salt长度。
            
            // Calculate the authentication token as follows: token = md5(password + salt). The md5() function takes a string and returns the 32-byte ASCII hexadecimal representation of the MD5 hash, using lower case characters for the hex values. The '+' operator represents concatenation of the two strings. Treat the strings as UTF-8 encoded when calculating the hash. Send the result as parameter t.
            // 计算身份验证令牌，如下所示：token = md5（password + salt. md5（）函数接受一个字符串，并返回MD5哈希的32字节ASCII十六进制表示，十六进制值使用小写字符。“+”运算符表示两个字符串的连接。在计算哈希时，将字符串视为UTF-8编码。将结果作为参数 t 发送。
            
            // For example: if the password is sesame and the random salt is c19b2d, then token = md5("sesamec19b2d") = 26719a1196d2a940705a59634eb18eab. The corresponding request URL then becomes:
            // 例如：如果口令是sesame，随机盐是c19 b2 d，则token = md5（“sesamec 19 b2 d”）= 26719 a1196 d2 a940705 a59634 eb 18 eab。相应的请求URL变为：
            
            // http://your-server/rest/ping.view?u=joe&t=26719a1196d2a940705a59634eb18eab&s=c19b2d&v=1.12.0&c=myapp
            SysUserPojo pojo = subsonicUserCache.checkUser(user);
            String originToken = SecureUtil.md5(pojo.getPassword() + salt);
            if (StringUtils.equals(originToken, token)) {
                SubsonicUserUtil.setUser(pojo);
            } else {
                throw new BaseException(ResultCode.PASSWORD_ERROR);
            }
        } else {
            String tempPassword = password;
            // 对前端请求密码参数处理
            if (StringUtils.startsWithIgnoreCase(password, "enc:")) {
                // 删除多余前缀
                String replace = StringUtils.replace(password, "enc:", "");
                tempPassword = HexUtil.decodeHexStr(replace);
            }
            SysUserPojo account = subsonicUserCache.checkUser(user);
            if (StringUtils.equals(account.getPassword(), tempPassword)) {
                SubsonicUserUtil.setUser(account);
            } else {
                throw new BaseException(ResultCode.PASSWORD_ERROR);
            }
        }
    }
}