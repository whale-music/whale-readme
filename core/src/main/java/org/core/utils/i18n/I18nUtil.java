package org.core.utils.i18n;

import cn.hutool.extra.spring.SpringUtil;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class I18nUtil {
    
    public static MessageSource MESSAGE_SOURCE;
    private static String DEFAULT_LANGUAGE;
    
    public static String getMsg(String code, Object[] args, Locale locale) {
        return I18nUtil.getI18().getMessage(code, args, locale);
    }
    
    public static String getMsg(String code, Object[] args) {
        MessageSource messageSource = I18nUtil.getI18();
        if (StringUtils.equalsIgnoreCase(DEFAULT_LANGUAGE, "local")) {
            return messageSource.getMessage(code, args, Locale.getDefault());
        } else {
            return messageSource.getMessage(code, args, LocaleUtils.toLocale(DEFAULT_LANGUAGE));
        }
    }
    
    public static String getMsg(String code) {
        return getMsg(code, null);
    }
    
    public static MessageSource getI18() {
        if (I18nUtil.MESSAGE_SOURCE == null) {
            I18nUtil.MESSAGE_SOURCE = SpringUtil.getBean(MessageSource.class);
        }
        return I18nUtil.MESSAGE_SOURCE;
    }
    
    @Value("${spring.messages.default-language:local}")
    public void setDefaultLanguage(String defaultLanguage) {
        I18nUtil.DEFAULT_LANGUAGE = defaultLanguage;
    }
}
