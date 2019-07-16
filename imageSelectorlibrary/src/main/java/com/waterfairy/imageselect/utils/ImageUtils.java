package com.waterfairy.imageselect.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;


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
    private static final String TAG = "imageUtils";

    /**
     * 图片保存
     *
     * @param imgPath 保存路径
     * @param source  需要保存的io流
     * @return
     */
    public static boolean saveBitmap(String imgPath, ByteArrayInputStream source) {
        if (source == null) return false;
        File file = createFile(imgPath);
        boolean canSave = file != null;
        if (canSave) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                int length = 1024 * 1024 * 4;
                byte[] bytes = new byte[length];
                while ((length = source.read(bytes)) != -1) {
                    fos.write(bytes, 0, length);
                }
                fos.flush();
                fos.close();
                source.close();
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
     * 创建文件
     *
     * @param imgPath
     * @return
     */
    public static File createFile(String imgPath) {
        if (TextUtils.isEmpty(imgPath)) return null;
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
        if (canSave) return file;
        else return null;
    }


    /**
     * 保存bitmap
     *
     * @param imgPath
     * @param source
     * @param compressFormat
     * @param quality
     * @return
     */
    public static boolean saveBitmap(String imgPath, Bitmap source, Bitmap.CompressFormat compressFormat, int quality) {
        if (source == null || source.isRecycled()) return false;
        File file = createFile(imgPath);
        boolean canSave = file != null;
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


//    /**
//     * 尺寸缩放
//     *
//     * @param source 源bitmap
//     * @param fitXY  等比缩放?
//     * @return
//     */
//    public static Bitmap matrix(Bitmap source, CompressOptions compressOptions, boolean fitXY) {
//        int width = compressOptions.getMaxWidth();
//        int height = compressOptions.getMaxHeight();
//        if (width <= 0 || height <= 0) return source;
//        if (source != null) {
//            int bitmapWidth = source.getWidth();
//            int bitmapHeight = source.getHeight();
//            float xScale = 1;
//            float yScale = 1;
//            if (fitXY) {
//                if (bitmapWidth > width) {
//                    xScale = width / (float) bitmapWidth;
//                }
//                if (bitmapHeight > height) {
//                    yScale = height / (float) bitmapHeight;
//                }
//                if (xScale == 0 || yScale == 0) {
//                    float scale = Math.max(xScale, yScale);
//                    xScale = scale;
//                    yScale = scale;
//                }
//            } else {
//                boolean isWidthBig = false;
//                if (bitmapWidth / (float) bitmapHeight > (width / (float) height)) {
//                    //宽 为主
//                    isWidthBig = true;
//                }
//                if (bitmapWidth > width || bitmapHeight > height) {
//                    if (isWidthBig) {
//                        xScale = width / (float) bitmapWidth;
//                        yScale = xScale;
//                    } else {
//                        yScale = height / (float) bitmapHeight;
//                        xScale = yScale;
//                    }
//                }
//            }
//            return matrix(source, xScale, yScale);
//        }
//        return null;
//    }

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
     * @param compressFormat
     * @return
     */


    public static Bitmap decodeFromFile(File imageFile, Bitmap.CompressFormat compressFormat, int width, int height) throws IOException {

        if (!imageFile.exists()) {
            throw new IOException("文件不存在");
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (width > 0 && height > 0) {
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
            //1,2,3,4,5  =1   6,7,8 = 2   9,10,11 3
            //1248
            int sampleSize = (int) Math.max(xScale, yScale);
            if (sampleSize > 16) sampleSize = 4;
            else if (sampleSize > 8) sampleSize = 2;
            else sampleSize = 1;
            options.inSampleSize = sampleSize;
            options.inJustDecodeBounds = false;
        }
        options.inPreferredConfig = compressFormat == Bitmap.CompressFormat.PNG ? Bitmap.Config.ARGB_4444 : Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);

        if (options.outWidth <= 0 || options.outHeight <= 0) {
            throw new IOException("文件解析失败");
        }
        Log.i(TAG, "compress decode: inSampleSize: " + options.inSampleSize + " ; size: " + bitmap.getWidth() + "/" + bitmap.getHeight());
        return bitmap;
    }

    /**
     * 文件压缩
     *
     * @param imageFile
     * @return
     */
    public static Object compress(File imageFile, int maxWidth, int maxHeight, int maxSize) {
        try {
            Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
            if (imageFile.getAbsolutePath().endsWith(".PNG") || imageFile.getAbsolutePath().endsWith(".png")) {
                compressFormat = Bitmap.CompressFormat.PNG;
            }
            //从文件解码
            Bitmap bitmap = decodeFromFile(imageFile, compressFormat, maxWidth, maxHeight);
            return compress(bitmap, maxWidth, maxHeight, maxSize, compressFormat);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * bitmap压缩
     *
     * @param bitmap
     * @param maxWidth
     * @param maxHeight
     * @param maxSize
     * @param compressFormat
     * @return
     */
    public static Object compress(Bitmap bitmap, int maxWidth, int maxHeight, int maxSize, Bitmap.CompressFormat compressFormat) {
        if (bitmap == null) return null;
        if (compressFormat == Bitmap.CompressFormat.PNG) {
            //png压缩尺寸
            return compressMeasurement(bitmap, maxWidth, maxHeight, Bitmap.Config.ARGB_4444);
        } else {
            //jpg压缩尺寸
//                Bitmap bitmapTemp = matrix(bitmap, compressOptions, false);
            Bitmap bitmapTemp = compressMeasurement(bitmap, maxWidth, maxHeight, Bitmap.Config.RGB_565);
            if (bitmapTemp != null) {
                //jpg压缩质量
                return compressQualityOutIS(bitmapTemp, maxSize);
            }
        }
        return null;
    }

    /**
     * 尺寸压缩
     *
     * @param bitmap
     * @return
     */
    private static Bitmap compressMeasurement(Bitmap bitmap, int maxWidth, int maxHeight, Bitmap.Config config) {
        //200 80
        //200 90     100 45
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int tempWidth = 0;
        int tempHeight = 0;

        if (maxWidth <= 0 || maxHeight <= 0) {
            maxWidth = width;
            maxHeight = height;
        }
        //尺寸有差比 或者 配置有差别
        if ((width > maxWidth || height > maxHeight) || bitmap.getConfig() != config) {
            //压缩
            if (maxWidth / (float) maxHeight > width / (float) height) {
                tempHeight = maxHeight;
                tempWidth = (int) (maxHeight * width / (float) height);
            } else {
                tempWidth = maxWidth;
                tempHeight = (int) (maxWidth * height / (float) width);
            }
            Bitmap bitmapTemp = Bitmap.createBitmap(tempWidth, tempHeight, config);
            Canvas canvas = new Canvas(bitmapTemp);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            paint.setFlags(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
            canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), new Rect(0, 0, tempWidth, tempHeight), paint);
            //回收
            bitmap.recycle();
            Log.i(TAG, "compress measure: old: " + width + "/" + height + "; new: " + tempWidth + "/" + tempHeight);
            bitmap = null;
            return bitmapTemp;
        }
        return bitmap;
    }


//    /**
//     * 质量压缩方法
//     *
//     * @param image
//     * @param maxSize KB
//     * @return
//     */
//    public static Bitmap compressQuality(Bitmap image, int maxSize) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//        int options = 100;
//        long length = 0;
//        while ((length = baos.toByteArray().length) / 1024 > maxSize && options >= 10) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
//            options -= 8;// 每次都减少5
//            Log.i(TAG, "compress quality: " + options + ";size: " + length);
//            baos.reset(); // 重置baos即清空baos
//            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
//        }
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
//        BitmapFactory.Options options1 = new BitmapFactory.Options();
//        options1.inPreferredConfig = Bitmap.Config.RGB_565;
//        return BitmapFactory.decodeStream(isBm, null, options1);
//    }
//

    /**
     * 质量压缩方法
     *
     * @param bitmap
     * @param maxSize KB
     * @return
     */
    public static ByteArrayInputStream compressQualityOutIS(Bitmap bitmap, int maxSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 86, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        Log.i(TAG, "compress quality: " + 86 + ";size: " + baos.toByteArray().length);

        if (maxSize > 0) {
            int options = 86;
            long length = 0;
            while ((length = baos.toByteArray().length) / 1024 > maxSize && options >= 10) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
                options -= 8;// 每次都减少5
                Log.i(TAG, "compress quality: " + options + ";size: " + length);
                baos.reset(); // 重置baos即清空baos
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            }
        }
        bitmap.recycle();
        bitmap = null;
        return new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
    }


    public static void saveBitmap(Bitmap cropBitmap, Bitmap.CompressFormat format, File savePath) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(savePath);
        cropBitmap.compress(format, 100, fileOutputStream);
        fileOutputStream.close();
    }
}
