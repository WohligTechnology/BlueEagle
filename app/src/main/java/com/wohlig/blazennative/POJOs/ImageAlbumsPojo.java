package com.wohlig.blazennative.POJOs;

import android.content.Context;

/**
 * Created by Jay on 03-03-2016.
 */
public class ImageAlbumsPojo {
    protected String id;
    protected String imageUrl;
    protected String title;
    protected Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
