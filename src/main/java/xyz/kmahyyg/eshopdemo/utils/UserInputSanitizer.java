package xyz.kmahyyg.eshopdemo.utils;

import org.apache.commons.text.StringEscapeUtils;

public class UserInputSanitizer {
    public static String SanitizeForHTML(String input) {
        return StringEscapeUtils.escapeHtml4(input);
    }

    public static String SanitizeForJava(String input) {
        return StringEscapeUtils.escapeJava(input);
    }

    public static String SanitizeForJS(String input) {
        return StringEscapeUtils.escapeEcmaScript(input);
    }
}
