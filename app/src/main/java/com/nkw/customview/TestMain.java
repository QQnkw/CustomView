package com.nkw.customview;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.zip.ZipFile;

public class TestMain {
    public static void main(String[] args) {
        String path = "c:/apk/CN/tazai_v1.apk";
        writeApk(new File(path), "https://juejin.im/post/5b211cc46fb9a01e6d6bd98d");
        System.out.println("Complete! File lies in " + path);
        try {
            ZipFile zipFile = new ZipFile(new File(path));
            System.out.println("Zip file comment = " + zipFile.getComment());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Zip file comment read failed!");
        }
    }

    public static void writeApk(File file, String comment) {
        ZipFile zipFile = null;
        ByteArrayOutputStream outputStream = null;
        RandomAccessFile accessFile = null;
        try {
            zipFile = new ZipFile(file);
            String zipComment = zipFile.getComment();
            if (zipComment != null) {
                return;
            }

            byte[] byteComment = comment.getBytes();
            outputStream = new ByteArrayOutputStream();

            outputStream.write(byteComment);
            outputStream.write(short2Stream((short) byteComment.length));

            byte[] data = outputStream.toByteArray();

            accessFile = new RandomAccessFile(file, "rw");
            accessFile.seek(file.length() - 2);
            accessFile.write(short2Stream((short) data.length));
            accessFile.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (zipFile != null) {
                    zipFile.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (accessFile != null) {
                    accessFile.close();
                }
            } catch (Exception e) {

            }

        }
    }

    /**
     * 字节数组转换成short（小端序）
     */
    private static byte[] short2Stream(short data) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putShort(data);
        buffer.flip();
        return buffer.array();
    }
    /*--------------------------上面是写入信息代码-------------------------*/
    /*--------------------------下面是读取信息代码-------------------------*/
    public static String getUnfinishedURL(Context context) {
        //获取缓存的 APK 文件
        File file = new File(context.getPackageCodePath());
        byte[] bytes;
        RandomAccessFile accessFile = null;
        // 从指定的位置找到 WriteAPK.java 写入的信息
        try {
            accessFile = new RandomAccessFile(file, "r");
            long index = accessFile.length();
            bytes = new byte[2];
            index = index - bytes.length;
            accessFile.seek(index);
            accessFile.readFully(bytes);
            int contentLength = stream2Short(bytes, 0);
            bytes = new byte[contentLength];
            index = index - bytes.length;
            accessFile.seek(index);
            accessFile.readFully(bytes);
            return new String(bytes, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (accessFile != null) {
                try {
                    accessFile.close();
                } catch (IOException ignored) {
                    ignored.printStackTrace();
                }
            }
        }
        return null;
    }
    public static short stream2Short(byte[] stream, int offset) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(stream[offset]);
        buffer.put(stream[offset + 1]);
        return buffer.getShort(0);
    }
}
