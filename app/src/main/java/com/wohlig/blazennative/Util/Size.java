package com.wohlig.blazennative.Util;

import android.content.Context;

/**
 * Created by Jay on 07-03-2016.
 */
public class Size {

    public static int dpToPx(final Context context, final float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    public static int pxToDp(final Context context, final float px) {
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }
}
