package org.base.library.utils;

/**
 * Created by YinShengyi on 2016/11/25.
 */
public class StringUtils {
    /**
     * 检测字符串是否为null或空字符串
     * @param content 待检测字符串
     */
    public static boolean isNull(String content) {
        return content == null || "".equals(content);
    }
}
