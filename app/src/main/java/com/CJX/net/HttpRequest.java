package com.CJX.net;

import android.graphics.Bitmap;

public interface HttpRequest {
    boolean checkNetWork();
    String getJson(String path, String requestLimit, String method);
    Bitmap getImage(String request);
}
