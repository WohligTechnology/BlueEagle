package com.wohlig.blazennative.Util;

/**
 * Created by Jay on 21-03-2016.
 */
public class IconFont {

    public static String getGmdFontCode(String fontcode){

        return "{gmd_" + fontcode.replaceAll(" ", "_").toLowerCase().trim() + "}";
    }
}
