package org.core.common.constant;

import org.core.utils.i18n.I18nUtil;

import java.util.Map;

public class LanguagesConstant {
    // 纯音乐
    public static final String ABSOLUTE_MUSIC = "AbsoluteMusic";
    // 汉语（普通话/中文）
    public static final String CHINESE = "Chinese";
    // 粤语
    public static final String CANTONESE = "Cantonese";
    // 英语
    public static final String ENGLISH = "English";
    // 西班牙语
    public static final String SPANISH = "Spanish";
    // 法语
    public static final String FRENCH = "French";
    // 阿拉伯语
    public static final String ARABIC = "Arabic";
    // 俄语
    public static final String RUSSIAN = "Russian";
    // 葡萄牙语
    public static final String PORTUGUESE = "Portuguese";
    // 德语
    public static final String GERMAN = "German";
    // 日语
    public static final String JAPANESE = "Japanese";
    // 韩语
    public static final String KOREAN = "Korean";
    // 意大利语
    public static final String ITALIAN = "Italian";
    // 荷兰语
    public static final String DUTCH = "Dutch";
    // 印度尼西亚语
    public static final String INDONESIAN = "Indonesian";
    // 印地语
    public static final String HINDI = "Hindi";
    // 孟加拉语
    public static final String BENGALI = "Bengali";
    // 定义语言与国际化字符串的映射关系
    public static final Map<String, String> map = Map.ofEntries(
            Map.entry(ABSOLUTE_MUSIC, I18nUtil.getMsg("language.absolute_music")),
            Map.entry(CHINESE, I18nUtil.getMsg("language.chinese")),
            Map.entry(CANTONESE, I18nUtil.getMsg("language.cantonese")),
            Map.entry(ENGLISH, I18nUtil.getMsg("language.english")),
            Map.entry(SPANISH, I18nUtil.getMsg("language.spanish")),
            Map.entry(FRENCH, I18nUtil.getMsg("language.french")),
            Map.entry(ARABIC, I18nUtil.getMsg("language.arabic")),
            Map.entry(RUSSIAN, I18nUtil.getMsg("language.russian")),
            Map.entry(PORTUGUESE, I18nUtil.getMsg("language.portuguese")),
            Map.entry(GERMAN, I18nUtil.getMsg("language.german")),
            Map.entry(JAPANESE, I18nUtil.getMsg("language.japanese")),
            Map.entry(KOREAN, I18nUtil.getMsg("language.korean")),
            Map.entry(ITALIAN, I18nUtil.getMsg("language.italian")),
            Map.entry(DUTCH, I18nUtil.getMsg("language.dutch")),
            Map.entry(INDONESIAN, I18nUtil.getMsg("language.indonesian")),
            Map.entry(HINDI, I18nUtil.getMsg("language.hindi")),
            Map.entry(BENGALI, I18nUtil.getMsg("language.bengali"))
    );
    
    
    private LanguagesConstant() {
    }
}

