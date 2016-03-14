package com.wohlig.blazennative.ARC.Http;

/**
 * Created by Jay on 12-03-2016.
 */
public interface HttpInterface {

    void refreshView(String response);
    void noInternet();
    void error();
}
