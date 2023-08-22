package com.alexsitiy.script.evaluation.util;

public class JSScriptUtils {

    private static final String VALID_JS_CODE = "console.log('123');";
    private static final String INVALID_JS_CODE = "console.log('123";
    private static final String ENDLESS_JS_CODE = "while(true){}";


    private JSScriptUtils() {

    }

    public static String getValidJSCode() {
        return VALID_JS_CODE;
    }

    public static String getInvalidJsCode() {
        return INVALID_JS_CODE;
    }

    public static String getEndlessJsCode() {
        return ENDLESS_JS_CODE;
    }
}
