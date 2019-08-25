package com.waterfairy.imageselect.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BinaryTool {

    public BinaryTool() {
    }

    public List[] getBinaryList(File file) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[1024 * 1024];
        List<Byte> byteList = new ArrayList<>();
        List<String> hexList = new ArrayList<>();
        while (inputStream.read(bytes) != -1) {
            for (byte aByte : bytes) {
                byteList.add(aByte);
                hexList.add(toHex(aByte));
            }
        }
        return new List[]{byteList, hexList};
    }

    /**
     * 8位(bit) 1000 0000
     *
     * @param aByte
     * @return int (Int = 32位(bit))
     */
    public int toInt(byte aByte) {

        return 0;
    }

    /**
     * 16位(bit) 2字节(Byte) 1000 0000  0000 0000
     *
     * @param aChar
     * @return
     */
    public int toInt(char aChar) {


        return 0;
    }

    public char toChar(byte heightByte, byte lowByte) {
        //我  25105
        //98         17
        //0110 0010  0001 0001

        //耀 32768
        //128        0
        //1000 0000  0000 0000

        return (char) ((heightByte << 8) | lowByte);
    }

    public String toHex(byte aByte) {
        String hex = "";
        if (aByte < 0) {
            int v = (int) Math.pow(2, 8) + aByte;
            hex = Integer.toHexString(v);
        } else {
            hex = Integer.toHexString(aByte);
        }
        if (hex.length() == 1) return "0" + hex;
        return hex;
    }

    public String toHex2(byte aByte) {
        String hex = "";
        if (aByte < 0) {
            int v = (int) Math.pow(2, 8) + aByte;
            hex = Integer.toHexString(v);
        } else {
            hex = Integer.toHexString(aByte);
        }
        if (hex.length() == 1) return "0" + hex;
        return hex;
    }


//    /**
//     * 十六进制
//     * Integer.toHexString()
//     *
//     * @param num
//     * @return
//     */
//    public String toHEX(int num) {
//        StringBuffer s = new StringBuffer();
//        String a;
//        char[] b = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
//        while (num != 0) {
//            s = s.append(b[num % 16]);
//            num = num / 16;
//        }
//        a = s.reverse().toString();
//        if (a.length() % 2 != 0) a = 0 + a;
//        return a;
//    }
//
//    /**
//     * 八进制
//     * Integer.toOctalString()
//     *
//     * @return
//     */
//    public String toOCT(int num) {
//        StringBuffer s = new StringBuffer();
//        String a;
//        char[] b = {'0', '1', '2', '3', '4', '5', '6', '7'};
//        while (num != 0) {
//            s = s.append(b[num % 8]);
//            num = num / 8;
//        }
//        a = s.reverse().toString();
//        return a;
//    }

//    /**
//     * 二进制
//     * Integer.toBinaryString()
//     *
//     * @return
//     */
//    public String toBinary(int num) {
//        StringBuffer s = new StringBuffer();
//        String a;
//        char[] b = {'0', '1'};
//        while (num != 0) {
//            s = s.append(b[num % 2]);
//            num = num / 2;
//        }
//        a = s.reverse().toString();
//        int time = 0;
//        if ((time = a.length() % 8) != 0) {
//            for (int i = 0; i < 8 - time; i++) {
//                a = "0" + a;
//            }
//        }
//        return a;
//    }

    public static void main(String[] args) {
        new BinaryTool().toChar((byte) -1, (byte) -1);
    }
}
