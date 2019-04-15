package com.waterfairy.imageselect.async;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.waterfairy.imageselect.listener.OnCropBitmapListener;
import com.waterfairy.imageselect.utils.FileUtils;
import com.waterfairy.imageselect.utils.ImageUtils;

import java.io.File;

public class CropBitmapAsync {
    private Bitmap.CompressFormat format;
    private Drawable drawable;
    private RectF lineRect;
    private RectF bitmapRect;
    private String cropSavePath;
    private OnCropBitmapListener onCropBitmapListener;
    private String imgRealPath;//图片真实路径

    public CropBitmapAsync() {

    }

    public CropBitmapAsync initData(String cropSavePath, String imgRealPath, Bitmap.CompressFormat format,
                                    Drawable drawable,
                                    RectF lineRect,
                                    RectF bitmapRect,
                                    OnCropBitmapListener onCropBitmapListener) {
        this.cropSavePath = cropSavePath;
        this.imgRealPath = imgRealPath;
        this.format = format;
        this.drawable = drawable;
        this.lineRect = lineRect;
        this.bitmapRect = bitmapRect;
        this.onCropBitmapListener = onCropBitmapListener;
        return this;
    }

    public void start() {
        if (onCropBitmapListener != null) onCropBitmapListener.onCropStart();
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                //剪切bitmap
                Bitmap bitmap = null;
                boolean needRecycler = false;
                if (!TextUtils.isEmpty(imgRealPath)) {
                    needRecycler = true;
                    bitmap = cropBitmap(imgRealPath, lineRect, bitmapRect);
                } else {
                    bitmap = cropBitmap(drawable, lineRect, bitmapRect);
                }
                if (bitmap == null) {
                    if (onCropBitmapListener != null)
                        onCropBitmapListener.onCropError("剪切bitmap失败!");
                } else {
                    //保存图片
                    File savePath = FileUtils.getCropSavePath(cropSavePath, Bitmap.CompressFormat.PNG == format ? "png" : "jpg");
                    boolean success = ImageUtils.saveBitmap(savePath.getAbsolutePath(), bitmap, format, 95);
                    if (needRecycler) {
                        if (bitmap != null && !bitmap.isRecycled()) {
                            bitmap.recycle();
                            bitmap = null;
                        }
                    }
                    if (success)
                        return savePath.toString();
                    else if (onCropBitmapListener != null)
                        onCropBitmapListener.onCropError("保存图片失败");
                }
                return null;
            }

            @Override
            protected void onPostExecute(String savePath) {
                super.onPostExecute(savePath);
                if (onCropBitmapListener != null) onCropBitmapListener.onCropSuccess(savePath);
            }
        }.execute();
    }

    /**
     * 从真实地址裁剪图片
     *
     * @param imgRealPath
     * @param lineRect
     * @param bitmapRect
     * @return
     */
    public Bitmap cropBitmap(String imgRealPath, RectF lineRect, RectF bitmapRect) {
        Drawable drawable = new BitmapDrawable(imgRealPath);
        return cropBitmap(drawable, lineRect, bitmapRect);
    }

    /**
     * 从 drawable 裁剪图片(1. 图片的真实地址装换, 2 . imageView中获取)
     *
     * @param drawable
     * @param lineRect
     * @param bitmapRect
     * @return
     */
    public Bitmap cropBitmap(Drawable drawable, RectF lineRect, RectF bitmapRect) {
        if (drawable instanceof BitmapDrawable) {
            //获取bitmap
            BitmapDrawable bitmapDrawer = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawer.getBitmap();
            //bitmap的宽高
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();

            //缩放后的bitmap在view中的宽高
            float tempBitmapWidth = bitmapRect.width();
            float tempBitmapHeight = bitmapRect.height();

            //计算剪切位置在缩放的尺寸中的占比  然后得到对应真实图片的尺寸
            int cropLeft = (int) ((lineRect.left - bitmapRect.left) / tempBitmapWidth * bitmapWidth);
            int cropTop = (int) ((lineRect.top - bitmapRect.top) / tempBitmapHeight * bitmapHeight);
            int cropWidth = (int) (lineRect.width() / tempBitmapWidth * bitmapWidth);
            int cropHeight = (int) (lineRect.height() / tempBitmapHeight * bitmapHeight);
            if (cropLeft < 0) cropLeft = 0;
            if (cropTop < 0) cropTop = 0;
            if (cropWidth + cropLeft > bitmapWidth) cropWidth = bitmapWidth - cropLeft;
            if (cropHeight + cropTop > bitmapHeight) cropHeight = bitmapHeight - cropTop;
            return Bitmap.createBitmap(bitmap, cropLeft, cropTop, cropWidth, cropHeight);
        }
        return null;
    }
}
