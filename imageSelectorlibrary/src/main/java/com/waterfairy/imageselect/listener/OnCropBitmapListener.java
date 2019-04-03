package com.waterfairy.imageselect.listener;


public interface OnCropBitmapListener {
    void onCropSuccess(String bitmapPath);

    void onCropError(String errMsg);

    void onCropStart();
}
