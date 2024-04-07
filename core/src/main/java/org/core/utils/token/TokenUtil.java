package org.core.utils.token;

import cn.hutool.core.util.IdUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import org.core.common.constant.UserCacheTypeConstant;
import org.core.common.exception.BaseException;
import org.core.config.JwtConfig;
import org.core.model.UserLoginCacheModel;
import org.core.mybatis.pojo.SysUserPojo;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Component
public class TokenUtil {
    private static final String USER_INFO = "info";
    
    private static final String REFRESH = "refresh";
    
    private static final Algorithm algorithm = Algorithm.HMAC256(JwtConfig.getSeedKey());
    
    private final UserCacheServiceUtil userCacheServiceUtil;
    
    public TokenUtil(UserCacheServiceUtil userCacheServiceUtil) {
        this.userCacheServiceUtil = userCacheServiceUtil;
    }
    
    //----------------------------------------------------------------sign token
    
    /**
     * 生成jwt字符串，根据设置时间过期  JWT(json web token)
     *
     * @param userId 根据用户ID生成token
     * @param user   保存用户
     * @return 返回token
     */
    public String webdavSignToken(String userId, SysUserPojo user) {
        return sign(UserCacheTypeConstant.WEBDAV, new Date(System.currentTimeMillis() + JwtConfig.getExpireTime()), userId, user, USER_INFO);
    }
    
    public String subsonicSignToken(String userId, SysUserPojo user) {
        return sign(UserCacheTypeConstant.SUBSONIC, new Date(System.currentTimeMillis() + JwtConfig.getExpireTime()), userId, user, USER_INFO);
    }
    
    public String nMusicSignToken(String userId, SysUserPojo user) {
        return sign(UserCacheTypeConstant.NMUSIC, new Date(System.currentTimeMillis() + JwtConfig.getExpireTime()), userId, user, USER_INFO);
    }
    
    public String adminSignToken(String userId, SysUserPojo user) {
        return sign(UserCacheTypeConstant.ADMIN, new Date(System.currentTimeMillis() + JwtConfig.getExpireTime()), userId, user, USER_INFO);
    }
    
    // ----------------------------------------------------------------
    
    public String webdavSignToken(Date expires, String userId, SysUserPojo user) {
        return sign(UserCacheTypeConstant.WEBDAV, expires, userId, user, USER_INFO);
    }
    
    public String subsonicSignToken(Date expires, String userId, SysUserPojo user) {
        return sign(UserCacheTypeConstant.SUBSONIC, expires, userId, user, USER_INFO);
    }
    
    public String nMusicSignToken(Date expires, String userId, SysUserPojo user) {
        return sign(UserCacheTypeConstant.NMUSIC, expires, userId, user, USER_INFO);
    }
    
    public String adminSignToken(Date expires, String userId, SysUserPojo user) {
        return sign(UserCacheTypeConstant.ADMIN, expires, userId, user, USER_INFO);
    }
    
    // ---------------------------------------------------------------- refresh
    public String webdavRefreshSignToken(Date expires, String userId, SysUserPojo user) {
        return sign(UserCacheTypeConstant.WEBDAV_REFRESH, expires, userId, user, REFRESH);
    }
    
    public String subsonicRefreshSignToken(Date expires, String userId, SysUserPojo user) {
        return sign(UserCacheTypeConstant.SUBSONIC_REFRESH, expires, userId, user, REFRESH);
    }
    
    public String nMusicRefreshSignToken(Date expires, String userId, SysUserPojo user) {
        return sign(UserCacheTypeConstant.NMUSIC_REFRESH, expires, userId, user, REFRESH);
    }
    
    public String adminRefreshSignToken(Date expires, String userId, SysUserPojo user) {
        return sign(UserCacheTypeConstant.ADMIN_REFRESH, expires, userId, user, REFRESH);
    }
    
    public String refreshSignToken(String type, Date expires, String userId, SysUserPojo user) {
        return sign(type, expires, userId, user, REFRESH);
    }
    
    private String sign(String type, Date expires, String userId, SysUserPojo user, String info) {
        String uuid = type + IdUtil.fastUUID();
        String sign = JWT.create()
                         // 将userId保存到token里面
                         .withAudience(userId)
                         // 存放自定义数据
                         .withClaim(info, uuid)
                         // 根据设定的时间过期
                         .withExpiresAt(expires)
                         // token的密钥
                         .sign(algorithm);
        userCacheServiceUtil.setUserCache(uuid, new UserLoginCacheModel(user, sign, uuid, LocalDateTime.now()));
        return sign;
    }
    
    /**
     * 根据token获取userId
     *
     * @param token token值
     * @return 返回User ID
     */
    public String getUserId(String token) {
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
    public UserLoginCacheModel getUserInfo(String token) {
        return userCacheServiceUtil.getUserCache(getInfo(token, USER_INFO));
    }
    
    public SysUserPojo getRefreshUserInfo(String token) {
        return userCacheServiceUtil.getUserCache(getInfo(token, REFRESH));
    }
    
    private String getInfo(String token, String name) {
        try {
            return JWT.decode(token).getClaim(name).asString();
        } catch (JWTDecodeException e) {
            throw new BaseException(e.getMessage());
        }
    }
    
    /**
     * 判断 jwt 是否过期
     */
    public void isJwtExpired(String token) {
        JWT.require(algorithm).acceptExpiresAt(JwtConfig.getExpireTime()).build().verify(token);
    }
    
    /**
     * 校验token
     *
     * @param token token 数据
     */
    public void checkSign(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        verifier.verify(token);
    }
}

