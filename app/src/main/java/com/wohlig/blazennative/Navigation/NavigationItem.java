package com.wohlig.blazennative.Navigation;

import android.graphics.drawable.Drawable;

/**
 * Created by poliveira on 24/10/2014.
 */
public class NavigationItem {
    private String mText;
    private Drawable mDrawable;
    private String mType;
    private String mLink;

    /*public NavigationItem(String text, Drawable drawable, String type, String link) {
        mText = text;
        mDrawable = drawable;
        mType = type;
        mLink = link;
    }*/

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public void setDrawable(Drawable drawable) {
        mDrawable = drawable;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String mLink) {
        this.mLink = mLink;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }
}
