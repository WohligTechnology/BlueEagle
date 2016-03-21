package com.wohlig.blazennative.Navigation;

/**
 * Created by poliveira on 24/10/2014.
 */
public class NavigationItem {
    private String mText;
    private String mDrawable;
    private String mType;
    private String mLink;


    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getDrawable() {
        return mDrawable;
    }

    public void setDrawable(String drawable) {
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
