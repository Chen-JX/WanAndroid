package com.CJX.Interface;

import android.graphics.Bitmap;

public interface ImageCallback {
    void Success(Bitmap bitmap);
    void Failed(Exception e);
}
