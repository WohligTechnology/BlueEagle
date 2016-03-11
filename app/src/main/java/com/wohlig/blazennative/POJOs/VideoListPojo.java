package com.wohlig.blazennative.POJOs;

import android.content.Context;

/**
 * Created by Jay on 11-03-2016.
 */
public class VideoListPojo {

    protected String videoId;
    protected String title;
    protected Context context;

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
