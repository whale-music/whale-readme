package org.common.utils.i18n;

import cn.hutool.extra.spring.SpringUtil;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class I18nUtil {
    
    public static MessageSource MESSAGE_SOURCE;
    
    private static String defaultLanguage;
    
    public static String getMsg(String code, Object[] args, Locale locale) {
        return I18nUtil.MESSAGE_SOURCE.getMessage(code, args, locale);
    }
    
    public static String getMsg(String code, Object[] args) {
        if (StringUtils.equalsIgnoreCase(defaultLanguage, "local")) {
            return I18nUtil.MESSAGE_SOURCE.getMessage(code, args, Locale.getDefault());
        } else {
            return I18nUtil.MESSAGE_SOURCE.getMessage(code, args, LocaleUtils.toLocale(defaultLanguage));
        }
    }
    
    public static String getMsg(String code) {
        return getMsg(code, null);
    }
    
    @PostConstruct
    public void initI18n() {
        I18nUtil.MESSAGE_SOURCE = SpringUtil.getBean(MessageSource.class);
    }
    
    @Value("${spring.messages.default-language:local}")
    public void setDefaultLanguage(String defaultLanguage) {
        I18nUtil.defaultLanguage = defaultLanguage;
    }
}
