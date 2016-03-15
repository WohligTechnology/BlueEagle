package com.wohlig.blazennative.Util;

/**
 * Created by Jay on 05-02-2016.
 */
public class FormValidation {
    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean isValidMobile(String phone) {
        if (phone == null) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(phone).matches();
        }
    }
}
