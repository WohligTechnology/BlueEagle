package com.wohlig.blazennative.Util;

import java.io.IOException;

/**
 * Created by Jay on 02-03-2016.
 */
public class InternetOperations {

    public static String SERVER_URL = "http://192.168.0.200:81/callApi/blazen/";
    public static String SERVER_UPLOADS_URL = "http://www.jaipurpinkpanthers.com/admin/uploads/";
    public static String SERVER_THUMB_URL = "http://www.jaipurpinkpanthers.com/admin/index.php/image/index?name=";
    public static String SERVER_WIDTH_250 = "&width=250";
    public static String SERVER_WIDTH_400 = "&width=400";


    public static boolean checkIsOnlineViaIP() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            //Process ipProcess = runtime.exec("/system/bin/ping -c 1 192.168.0.124");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e){
            e.printStackTrace();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }

        return false;
    }
}
