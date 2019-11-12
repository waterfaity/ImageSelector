package com.waterfairy.imageselect.tool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.NonNull;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2019-11-12 17:29
 * @info:
 */
public class ImageRotateTool {
    public static ImageRotateTool newInstance() {
        return new ImageRotateTool();
    }

    /**
     * 获取旋转角度
     *
     * @param imgPath
     * @return
     * @throws IOException
     * @throws NullPointerException
     */
    public int getRotate(@NonNull String imgPath) throws IOException {
        ExifInterface exifInterface = new ExifInterface(imgPath);
        int anInt = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        switch (anInt) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
        }
        return 0;
    }

    /**
     * @param imgPath
     * @return
     * @throws IOException
     */
    public String rotate(@NonNull String imgPath) throws IOException {
        return rotate(imgPath, imgPath);
    }

    /**
     * @param imgPath
     * @param targetPath
     * @return
     * @throws IOException
     */
    public String rotate(@NonNull String imgPath, String targetPath) throws IOException {
        int rotate = getRotate(imgPath);
        if (rotate != 0) {
            return rotate(imgPath, targetPath, -rotate);
        } else {
            return imgPath;
        }
    }

    /**
     * @param imgPath
     * @param targetPath
     * @param degree
     * @return
     * @throws IOException
     */
    public String rotate(@NonNull String imgPath, String targetPath, int degree) throws IOException {

        if (TextUtils.isEmpty(imgPath)) throw new IOException("imgPath is empty");
        File file = new File(imgPath);
        if (!file.exists()) throw new IOException("file is not exist");
        if (!file.canRead()) throw new IOException("has no permission for read");
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
        Bitmap rotateBitmap = rotate(bitmap, degree);
        bitmap.recycle();
        //保存 判断jpg  或 png
        File saveFile = getSaveFile(imgPath, targetPath);
        //保存
        FileOutputStream fileOutputStream = new FileOutputStream(saveFile);
        rotateBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        return saveFile.getAbsolutePath();
    }

    /**
     * 获取保存文件
     *
     * @param imgPath
     * @param targetPath
     * @return
     * @throws IOException
     */
    private File getSaveFile(String imgPath, String targetPath) throws IOException {
        String savePath = "";
        if (TextUtils.isEmpty(targetPath)) {
            savePath = imgPath;
        } else {
            savePath = targetPath;
        }
        File saveFile = new File(savePath);

        if (!saveFile.exists()) {
            File parentFile = saveFile.getParentFile();
            if (parentFile == null) throw new IOException("parentFile`s is null");
            boolean parentExist;
            if (!(parentExist = parentFile.exists())) {
                parentExist = parentFile.mkdirs();
            }
            if (parentExist) {
                boolean newFile = saveFile.createNewFile();
                if (!newFile) {
                    throw new IOException("saveFile  create error");
                }
            } else {
                throw new IOException("parentFile  create error");
            }
        }
        return saveFile;
    }

    /**
     * 旋转角度
     *
     * @param degree
     */
    public Bitmap rotate(@NonNull Bitmap bitmap, int degree) throws IOException {
        if (bitmap.isRecycled()) throw new IOException("bitmap has recycled ");
        Matrix matrix = new Matrix();
        matrix.setRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
