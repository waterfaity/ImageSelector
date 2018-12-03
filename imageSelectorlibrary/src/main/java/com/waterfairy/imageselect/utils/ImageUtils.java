package com.waterfairy.imageselect.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/11/30 17:07
 * @info:
 */
public class ImageUtils {
    /**
     * 图片保存
     *
     * @param imgPath        保存路径
     * @param source         需要保存的img
     * @param compressFormat 格式
     * @param quality        质量 1-100
     * @return
     */
    public static boolean saveBitmap(String imgPath, Bitmap source, Bitmap.CompressFormat compressFormat, int quality) {

        if (source == null || source.isRecycled()) return false;
        if (TextUtils.isEmpty(imgPath)) return false;
        File file = new File(imgPath);
        boolean canSave = true;
        if (!file.exists()) {
            File parent = file.getParentFile();
            canSave = parent.exists() || parent.mkdirs();
            if (canSave) {
                try {
                    canSave = file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    canSave = false;
                }
            }
        }
        if (canSave) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                source.compress(compressFormat, quality, fos);
                fos.flush();
                fos.close();
                canSave = true;
            } catch (IOException e) {
                e.printStackTrace();
                canSave = false;
            }
        }
        if (!canSave) {
            try {
                if (file.exists())
                    file.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return canSave;
    }


    /**
     * 尺寸缩放
     *
     * @param source 源bitmap
     * @param width  目标宽px
     * @param height 目标高px
     * @param fitXY  等比缩放?
     * @return
     */
    public static Bitmap matrix(Bitmap source, int width, int height, boolean fitXY) {
        if (source != null) {
            int bitmapWidth = source.getWidth();
            int bitmapHeight = source.getHeight();
            float xScale = 1;
            float yScale = 1;
            if (fitXY) {
                if (bitmapWidth > width) {
                    xScale = width / (float) bitmapWidth;
                }
                if (bitmapHeight > height) {
                    yScale = height / (float) bitmapHeight;
                }
                if (xScale == 0 || yScale == 0) {
                    float scale = Math.max(xScale, yScale);
                    xScale = scale;
                    yScale = scale;
                }
            } else {
                boolean isWidthBig = false;
                if (bitmapWidth / (float) bitmapHeight > (width / (float) height)) {
                    //宽 为主
                    isWidthBig = true;
                }
                if (bitmapWidth > width || bitmapHeight > height) {
                    if (isWidthBig) {
                        xScale = width / (float) bitmapWidth;
                        yScale = xScale;
                    } else {
                        yScale = height / (float) bitmapHeight;
                        xScale = yScale;
                    }
                }
            }
            return matrix(source, xScale, yScale);
        }
        return null;
    }

    /**
     * 尺寸缩放
     *
     * @param source 源bitmap
     * @param xScale width  缩放比例
     * @param yScale height 缩放比例
     * @return
     */
    public static Bitmap matrix(Bitmap source, float xScale, float yScale) {
        Matrix matrix = new Matrix();
        matrix.setScale(xScale, yScale);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


    /**
     * 从文件读取bitmap 并缩放
     *
     * @param imageFile
     * @param width
     * @param height
     * @param useLittle 使用最小的缩小倍数 倍数最小为1 ,
     * @return
     */
    public static Bitmap decodeFromFile(File imageFile, int width, int height, boolean useLittle) throws IOException {
        if (width <= 0 || height <= 0) {
            throw new IOException("宽或高必须大于0");
        }
        if (!imageFile.exists()) {
            throw new IOException("文件不存在");
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        if (outWidth <= 0 || outHeight <= 0) {
            throw new IOException("文件解析失败");
        }
        float xScale = 1;
        float yScale = 1;

        if (outWidth > width) {
            //width缩放倍数
            xScale = outWidth / (float) width;
        }
        if (outHeight > height) {
            //height缩放倍数
            yScale = outHeight / (float) height;
        }
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;
        int sampleSize = (int) (useLittle ? Math.min(xScale, yScale) : Math.max(xScale, yScale));
        options.inSampleSize = sampleSize < 1 ? 1 : sampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
    }


    public static Bitmap compress(File imageFile, int maxWidth, int maxHeight, int maxSize) {
        try {
            Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
            if (imageFile.getAbsolutePath().endsWith(".PNG") || imageFile.getAbsolutePath().endsWith(".png")) {
                compressFormat = Bitmap.CompressFormat.PNG;
            }
            Bitmap bitmap = decodeFromFile(imageFile, maxWidth, maxHeight, false);
            if (compressFormat == Bitmap.CompressFormat.PNG) {
                return compressPNG(bitmap, maxWidth, maxHeight);
            } else {
                return compressJPG(bitmap, maxSize);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Bitmap compressPNG(Bitmap bitmap, int maxWidth, int maxHeight) {
        //200 80
        //200 90     100 45
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int tempWidth = 0;
        int tempHeight = 0;

        if (width > maxWidth || height > maxHeight) {
            //压缩
            if (maxWidth / (float) maxHeight > width / (float) height) {
                tempHeight = maxHeight;
                tempWidth = (int) (maxHeight * width / (float) height);
            } else {
                tempWidth = maxWidth;
                tempHeight = (int) (maxWidth * height / (float) width);
            }
            Bitmap bitmapTemp = Bitmap.createBitmap(tempWidth, tempHeight, Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(bitmapTemp);
            canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), new Rect(0, 0, tempWidth, tempHeight), null);
            return bitmapTemp;
        }
        return bitmap;
    }


    /**
     * 质量压缩方法
     *
     * @param image
     * @param maxSize KB
     * @return
     */
    public static Bitmap compressJPG(Bitmap image, int maxSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;
        while (baos.toByteArray().length / 1024 > maxSize && options >= 10) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null);
    }

}
