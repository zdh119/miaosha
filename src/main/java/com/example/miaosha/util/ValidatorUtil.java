package com.example.miaosha.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtil {
    private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");

    public static boolean isMobile(String phoneNum){
        if(StringUtils.isEmpty(phoneNum)) {
            return false;
        }
        Matcher m = mobile_pattern.matcher(phoneNum);
        return m.matches();
    }
}
