package org.core.config;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.mybatis.iservice.SysUserService;
import org.core.mybatis.pojo.SysUserPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class UserSubPasswordConfig {
    public static final String ACCOUNT_NAME = "name";
    public static final String ACCOUNT = "account";
    public static final String PASSWORD = "password";
    protected static final Map<String, String> allSubAccount = new LinkedHashMap<>();
    protected static final Map<String, SysUserPojo> SUBACCOUNTMAP = new LinkedHashMap<>();
    private final TypeReference<List<Map<String, String>>> type = new TypeReference<>() {
    };
    @Autowired
    @Qualifier("sysUserServiceImpl")
    SysUserService accountService;
    
    /**
     * 初始化用户子账户信息
     * 子账户信息 [ {'name': '','account': '','password':''} ]
     */
    @PostConstruct
    public void userInitSubAccount() {
        List<SysUserPojo> list = accountService.list();
        
        list.forEach(sysUserPojo -> {
            String subAccountPassword = sysUserPojo.getSubAccountPassword();
            List<Map<String, String>> subAccount = JSON.parseObject(subAccountPassword, type);
            if (CollUtil.isNotEmpty(subAccount)) {
                subAccount.forEach(stringStringMap -> allSubAccount.put(stringStringMap.get(ACCOUNT), stringStringMap.get(PASSWORD)));
            }
        });
        // key: account value: user info
        Map<String, SysUserPojo> userMap = new HashMap<>();
        for (SysUserPojo sysUserPojo : list) {
            String subAccountPassword = sysUserPojo.getSubAccountPassword();
            List<Map<String, String>> subAccount = JSON.parseObject(subAccountPassword, type);
            if (CollUtil.isEmpty(subAccount)) {
                continue;
            }
            for (Map<String, String> stringStringMap : subAccount) {
                userMap.put(stringStringMap.get(ACCOUNT), sysUserPojo);
            }
        }
        SUBACCOUNTMAP.putAll(userMap);
    }
    
    /**
     * 检测账户是否存在
     *
     * @param account 账户名
     * @return true 有值 false 无值
     */
    public boolean accountExistence(String account) {
        return StringUtils.isNotBlank(allSubAccount.get(account));
    }
    
    /**
     * 验证账户密码
     *
     * @param account  账户
     * @param password 密码
     * @return true通过, false不通过
     */
    public boolean accountVerification(String account, String password) {
        return StringUtils.equals(allSubAccount.get(account), password);
    }
    
    /**
     * 添加缓存账户
     *
     * @param account  账户
     * @param password 密码
     */
    public void addAccountCache(SysUserPojo user, String account, String password) {
        if (CollUtil.isEmpty(UserSubPasswordConfig.SUBACCOUNTMAP)) {
            UserSubPasswordConfig.SUBACCOUNTMAP.put(account, user);
        } else {
            for (String s : UserSubPasswordConfig.SUBACCOUNTMAP.keySet()) {
                if (StringUtils.equals(s, account)) {
                    throw new BaseException(ResultCode.SUB_ACCOUNT_EXISTS);
                }
            }
            UserSubPasswordConfig.SUBACCOUNTMAP.put(account, user);
        }
        allSubAccount.put(account, password);
    }
    
    /**
     * 删除账户缓存
     *
     * @param account 账户
     */
    public void delAccountCache(String account) {
        UserSubPasswordConfig.SUBACCOUNTMAP.remove(account);
        allSubAccount.remove(account);
    }
    
    /**
     * 通过账户名获取用户信息
     *
     * @param account 账户
     * @return 用户信息
     */
    public SysUserPojo getSubAccount(String account) {
        return UserSubPasswordConfig.SUBACCOUNTMAP.get(account);
    }
    
    public String getSubPassword(String account) {
        return allSubAccount.get(account);
    }
}
