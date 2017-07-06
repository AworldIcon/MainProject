package com.coder.kzxt.utils;

import android.os.Environment;
import android.os.StatFs;
import java.io.File;
import java.io.IOException;

/**
 * sd卡工具类
 */
public class SdcardUtils {


    /**
     * 判断内置卡是否存在
     */
    public static boolean isExistSdcard(){
        if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        }else{
            return false;
        }
    }


    /**
     * SD卡总容量
     */
    public static long getSDAllSize(String path) {
        //取得SD卡文件路径
        StatFs sf = new StatFs(path);
        //获取单个数据块的大小（Byte）
        long blockSize = sf.getBlockSize();
        //获取所有数据块数
        long allBlocks = sf.getBlockCount();
        //返回SD卡的大小
        return (allBlocks * blockSize)/1024/1024;
    }


    public static long getSDAllSizeByte(String path) {
        //取得SD卡文件路径
        StatFs sf = new StatFs(path);
        //获取单个数据块的大小（Byte）
        long blockSize = sf.getBlockSize();
        //获取所有数据块数
        long allBlocks = sf.getBlockCount();
        //返回SD卡的大小
        return (allBlocks * blockSize);
    }


    /**
     * SD卡剩余空间
     * 返回 MB
     */
    public static long getSDFreeSize(String path) {
        StatFs sf = new StatFs(path);
        //获取单个数据块的大小（Byte）
        long blockSize = sf.getBlockSize();
        //空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        //返回SD卡空闲大小
        //return (freeBlocks * blockSize);//返回单位为Byte
        //return (freeBlocks * blockSize)/1024;//返回单位为KB
        return (freeBlocks * blockSize)/1024/1024; //返回单位为MB
    }


    /**
     * SD卡剩余空间
     * 返回 Byte
     */
    public static long getSDFreeSizeByte(String path) {
        StatFs sf = new StatFs(path);
        //获取单个数据块的大小（Byte）
        long blockSize = sf.getBlockSize();
        //空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        //返回SD卡空闲大小
        return (freeBlocks * blockSize);//返回单位为Byte
        //return (freeBlocks * blockSize)/1024;//返回单位为KB
//		return (freeBlocks * blockSize)/1024/1024; //返回单位为MB
    }


    /**
     * @return
     * 在外置sd卡上创建文件，检查外置sd卡是否存在
     * dir 在外置sd卡上创建一个文件 如果能创建则外置sd卡存在
     */
    public static boolean checkFsWritable(String dir) {
        if (dir == null)
            return false;
        File directory = new File(dir);

        if (!directory.isDirectory()) {
            if (!directory.mkdirs()) {
                return false;
            }
        }
        File f = new File(directory, ".keysharetestgzc");
        try {
            if (f.exists()) {
                f.delete();
            }
            if (!f.createNewFile()) {
                return false;
            }
            f.delete();
            return true;

        } catch (Exception e) {
        }

        return false;
    }


    public static File createSDDir(String dirName) throws IOException {
        File dir = new File(Constants.POST_PHOTO + dirName);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            System.out.println("createSDDir:" + dir.getAbsolutePath());
            System.out.println("createSDDir:" + dir.mkdir());
        }
        return dir;
    }

    public static boolean isFileExist(String fileName){
        File file = new File(Constants.POST_PHOTO + fileName);
        file.isFile();
        return file.exists();
    }


}
