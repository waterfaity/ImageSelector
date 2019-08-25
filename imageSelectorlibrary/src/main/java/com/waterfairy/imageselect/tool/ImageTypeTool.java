package com.waterfairy.imageselect.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2019-07-26 17:26
 * @info:
 */
public class ImageTypeTool {

    public static final int TYPE_JPG = 1;
    public static final int TYPE_PNG = 2;
    public static final int TYPE_GIF = 3;


//  PNG:
//
//  1 80 P
//  2 78 N
//  4 72 G
//  12 73 I
//  13 73 H
//  14 68 D
//  15 82 R

//  JPG:
//
//  1 FF
//  2 D8
//  3 FF
//  6 74 J
//  7 70 F
//  8 73 I
//  9 70 F

//  GIF:
//  1 71 G
//  2 73 I
//  3 70 F
//  4 56 8
//  5 57 9 (55 7)
//  6 97 a


    public static ImageTypeTool newInstance() {
        return new ImageTypeTool();
    }

    public void loadImage(File file) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[1024 * 1024];
        List<Byte> arrayList = new ArrayList<>();
        while (inputStream.read(bytes) != -1) {
            for (byte aByte : bytes) {
                arrayList.add(aByte);
            }
        }
    }
}
