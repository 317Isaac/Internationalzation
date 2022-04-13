package io.github.isaac;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtil {

    public static void main(String[] args) {
        String a = "aabaa_aa";
        String b = "a_aaAaa";
        String c = "aab011_a";
        System.out.println(isComplyWithIDNamingConvention(a));
        System.out.println(isComplyWithIDNamingConvention(b));
        System.out.println(isComplyWithIDNamingConvention(c));
    }

    /**
     * 判断字符串是否为NULL或空字符串
     *
     * @param str 字符串实例
     * @return true 当字符串为NULL或空字符串, 否则 false
     */
    public static boolean isEmpty(String str) {
        return null == str || str.isEmpty();
    }

    /**
     * 判断字符串中是否包含中文, 中文标点不算作中文
     *
     * @param str 待校验字符串
     * @return 是否为中文
     */
    public static boolean isContainChinese(String str) {
        final Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        final Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 是否遵守string.xml的id命名规范
     * 仅包含小写英文字母及下划线，以英文字母开头及结尾
     * @param str id名称
     * @return true:符合规范； false：不符合规范
     */
    public static boolean isComplyWithIDNamingConvention(String str) {
        final Pattern p = Pattern.compile("^[a-z_]+$");
        final Matcher m = p.matcher(str);
        return m.matches() && !str.startsWith("_") && !str.endsWith("_");
    }
}
