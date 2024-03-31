package org.core.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class StringUtil extends StringUtils {
    public static final String EMPTY = "";
    public static final String NULL = null;
    private StringUtil() {
    }
    
    /**
     * Returns either the passed in String,
     * or if the String is {@code null}, an empty String ("").
     *
     * <pre>
     * StringUtils.defaultString(null)  = ""
     * StringUtils.defaultString("")    = ""
     * StringUtils.defaultString("bat") = "bat"
     * </pre>
     *
     * @param str the String to check, may be null
     * @return the passed in String, or the empty String if it
     * was {@code null}
     * @see Objects#toString(Object, String)
     * @see String#valueOf(Object)
     */
    public static String defaultString(final Object str) {
        return Objects.toString(str, EMPTY);
    }
    
    public static String defaultNullString(final Object str) {
        return Objects.toString(str, NULL);
    }
}
