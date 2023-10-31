package org.core.utils;

import cn.hutool.core.util.IdUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.config.JwtConfig;
import org.core.mybatis.pojo.SysUserPojo;

import java.util.Date;
import java.util.Objects;

public class TokenUtil {
    private static final String INFO = "info";
    
    private TokenUtil() {
    }
    
    /**
     * 生成jwt字符串，根据设置时间过期  JWT(json web token)
     *
     * @param userId 根据用户ID生成token
     * @param user   保存用户
     * @return 返回token
     */
    public static String sign(String userId, SysUserPojo user) {
        Date date = new Date(System.currentTimeMillis() + JwtConfig.getExpireTime());
        Algorithm algorithm = Algorithm.HMAC256(JwtConfig.getSeedKey());
        String uuid = IdUtil.fastUUID();
        UserCacheServiceUtils.setUserCache(uuid, user);
        return JWT.create()
                  // 将userId保存到token里面
                  .withAudience(userId)
                  // 存放自定义数据
                  .withClaim(INFO, uuid)
                  // 根据设定的时间过期
                  .withExpiresAt(date)
                  // token的密钥
                  .sign(algorithm);
    }
    
    /**
     * 根据token获取userId
     *
     * @param token token值
     * @return 返回User ID
     */
    public static String getUserId(String token) {
        try {
            return JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException e) {
            throw new BaseException(e.getMessage());
        }
    }
    
    /**
     * 根据token获取自定义数据info
     *
     * @param token token
     * @return 返回自定义数据
     */
    public static SysUserPojo getInfo(String token) {
        try {
            return UserCacheServiceUtils.getUserCache(JWT.decode(token).getClaim(INFO).asString());
        } catch (JWTDecodeException e) {
            throw new BaseException(e.getMessage());
        }
    }
    
    /**
     * 校验token
     *
     * @param token token 数据
     */
    public static void checkSign(String token) {
        Algorithm algorithm = Algorithm.HMAC256(JwtConfig.getSeedKey());
        JWTVerifier verifier = JWT.require(algorithm).build();
        verifier.verify(token);
        if (Objects.isNull(getInfo(token))) {
            throw new BaseException(ResultCode.COOKIE_INVALID);
        }
    }
}

