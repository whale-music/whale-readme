package org.core.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.core.pojo.SysUserPojo;
import org.core.service.AccountService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@Slf4j
public class InitUserConfig implements ApplicationRunner, InitializingBean {
    
    public static final String ADMIN = "admin";
    private static final String PASS_WORD_SEED = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final AccountService accountService;
    
    private final LambdaQueryWrapper<SysUserPojo> userEq = Wrappers.<SysUserPojo>lambdaQuery().eq(SysUserPojo::getAccountType, 0);
    
    public InitUserConfig(AccountService accountService) {
        this.accountService = accountService;
    }
    
    /**
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     */
    @Override
    public void run(ApplicationArguments args) {
        // 是否查看Admin用户密码
        if (args.getNonOptionArgs().contains(ADMIN)) {
            SysUserPojo user = accountService.getOne(userEq);
            log.info("\nuser: {}\npassword: {}", user.getUsername(), user.getPassword());
        }
    }
    
    /**
     * 创建管理员用户
     */
    @Override
    public void afterPropertiesSet() {
        log.info("init user info");
        // 没有管理员用户则创建
        long count = accountService.count(userEq);
        if (count == 0) {
            SecureRandom random = new SecureRandom();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 12; i++) {
                char at = PASS_WORD_SEED.charAt(random.nextInt(PASS_WORD_SEED.length()));
                sb.append(at);
            }
            SysUserPojo user = new SysUserPojo();
            user.setNickname(ADMIN);
            user.setUsername(ADMIN);
            user.setPassword(sb.toString());
            accountService.createAdmin(user);
        }
    }
}
