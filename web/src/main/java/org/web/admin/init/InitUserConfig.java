package org.web.admin.init;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.NoOpLog;
import org.core.mybatis.pojo.SysUserPojo;
import org.core.service.AccountService;
import org.core.utils.spring.SpringUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InitUserConfig implements ApplicationRunner, InitializingBean {
    
    public static final String ADMIN = "admin";
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
            Log logger = LogFactory.getLog(NoOpLog.class);
            String message = """
                                                                                 _\s
                              _ __    __ _  ___  ___ __      __  ___   _ __   __| |
                             | '_ \\  / _` |/ __|/ __|\\ \\ /\\ / / / _ \\ | '__| / _` |
                             | |_) || (_| |\\__ \\\\__ \\ \\ V  V / | (_) || |   | (_| |
                             | .__/  \\__,_||___/|___/  \\_/\\_/   \\___/ |_|    \\__,_|
                             |_|                                                  \s
                             """;
    
            String format = CharSequenceUtil.format("\nuser: {}\npassword: {}", user.getUsername(), user.getPassword());
            logger.info(message + format);
    
            ApplicationContext context = SpringUtil.getApplicationContext();
            // 调用exit()方法来优雅地退出应用程序，传入一个退出状态码（可选）
            SpringApplication.exit(context, () -> 0);
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
            String password = RandomStringUtils.randomAlphanumeric(12);
            SysUserPojo user = new SysUserPojo();
            user.setNickname(ADMIN);
            user.setUsername(ADMIN);
            user.setRoleName(ADMIN);
            user.setPassword(password);
            accountService.createAdmin(user);
            log.info("create admin");
        }
    }
}
