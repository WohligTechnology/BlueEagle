package com.wohlig.blazennative.Util;

/**
 * Created by Jay on 15-03-2016.
 */
public class Youtube {
    public static String getUrl(String youtubeId) {
        return "https://www.youtube.com/embed/" + youtubeId + "?autoplay=1&modestbranding=1&showinfo=0&rel=0&loop=1";
    }

    public static String getThumbnail(String youtubeId) {
        return "http://img.youtube.com/vi/" + youtubeId + "/0.jpg";
    }

}
