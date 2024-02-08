package org.core.utils;

import cn.hutool.core.util.IdUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import org.core.common.exception.BaseException;
import org.core.config.JwtConfig;
import org.core.mybatis.pojo.SysUserPojo;

import java.util.Date;

public class TokenUtil {
    private static final String USER_INFO = "info";
    
    private static final String REFRESH = "refresh";
    
    private static final Algorithm algorithm = Algorithm.HMAC256(JwtConfig.getSeedKey());
    
    private TokenUtil() {
    }
    
    /**
     * 生成jwt字符串，根据设置时间过期  JWT(json web token)
     *
     * @param userId 根据用户ID生成token
     * @param user   保存用户
     * @return 返回token
     */
    public static String signToken(String userId, SysUserPojo user) {
        return sign(new Date(System.currentTimeMillis() + JwtConfig.getExpireTime()), userId, user, USER_INFO);
    }
    
    public static String signToken(Date expires, String userId, SysUserPojo user) {
        return sign(expires, userId, user, USER_INFO);
    }
    
    public static String refreshSignToken(String userId, SysUserPojo user) {
        return sign(new Date(System.currentTimeMillis() + JwtConfig.getRefreshExpireTime()), userId, user, REFRESH);
    }
    
    public static String refreshSignToken(Date expires, String userId, SysUserPojo user) {
        return sign(expires, userId, user, REFRESH);
    }
    
    private static String sign(Date expires, String userId, SysUserPojo user, String info) {
        String uuid = IdUtil.fastUUID();
        UserCacheServiceUtils.setUserCache(uuid, user);
        return JWT.create()
                  // 将userId保存到token里面
                  .withAudience(userId)
                  // 存放自定义数据
                  .withClaim(info, uuid)
                  // 根据设定的时间过期
                  .withExpiresAt(expires)
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
    public static SysUserPojo getUserInfo(String token) {
        return UserCacheServiceUtils.getUserCache(getInfo(token, USER_INFO));
    }
    
    public static SysUserPojo getRefreshUserInfo(String token) {
        return UserCacheServiceUtils.getUserCache(getInfo(token, REFRESH));
    }
    
    private static String getInfo(String token, String name) {
        try {
            return JWT.decode(token).getClaim(name).asString();
        } catch (JWTDecodeException e) {
            throw new BaseException(e.getMessage());
        }
    }
    
    /**
     * 判断 jwt 是否过期
     */
    public static void isJwtExpired(String token) {
        JWT.require(algorithm).acceptExpiresAt(JwtConfig.getExpireTime()).build().verify(token);
    }
    
    /**
     * 校验token
     *
     * @param token token 数据
     */
    public static void checkSign(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        verifier.verify(token);
    }
}

