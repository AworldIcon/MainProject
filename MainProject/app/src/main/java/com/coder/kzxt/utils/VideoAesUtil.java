package com.coder.kzxt.utils;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * mp4加密解密工具类
 */
public class VideoAesUtil {
    //将视频文件的数据流前50个字节中的每个字节与其下标进行异或运算
    public static final int REVERSE_LENGTH = 50;
    /**
     * 加解密
     * @param strFile 源文件绝对路径
     * @return
     */
    public static boolean encrypt(String strFile) {
        int len = REVERSE_LENGTH;
        try {
            File f = new File(strFile);
            RandomAccessFile raf = new RandomAccessFile(f, "rw");
            long totalLen = raf.length();

            if (totalLen < REVERSE_LENGTH){
                len = (int) totalLen;
            }
            FileChannel channel = raf.getChannel();
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, REVERSE_LENGTH);
            byte tmp;
            for (int i = 0; i < len; ++i) {
                byte rawByte = buffer.get(i);
                tmp = (byte) (rawByte ^ i);
                buffer.put(i, tmp);
            }
            buffer.force();
            buffer.clear();
            channel.close();
            raf.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
