package com.coder.kzxt.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * 处理文件工具类
 */
public class FileUtil {


    private Context context;
    private SharedPreferencesUtil spu;

    public FileUtil(Context context) {
        this.context =context;
        spu = new SharedPreferencesUtil(context);
    }

    /**
     * 检查文件夹是否已经创建
     * @param folderPath // 文件夹路径
     * @return
     */
    public static boolean isExistFolder(String folderPath){
        File file = new File(folderPath);
        if(file.exists()){
            return true;
        }
        return false;
    }

    /**
     * 删除文件夹下所有的文件
     * @param FilePath
     * @return
     */
    public static boolean delteallFile(String FilePath){
        if(!isExistFolder(FilePath))
            return false;
        File file = new File(FilePath);
        File[] files = file.listFiles();
        for(File f : files){
            if(f.isDirectory()){
                //如果是目录什么也不做
            }else {
                f.delete();
            }
        }

        if(file.delete()){
            return true;
        }
        return false;
    }



    /**
     * 删除文件夹及文件夹里的内容
     * @param folderPath
     * @return
     */
    public static boolean deleteFolder(String folderPath){
        if(!isExistFolder(folderPath))
            return false;
        File file = new File(folderPath);
        File[] files = file.listFiles();
        for(File f : files){
            if(f.isDirectory()){
                deleteFolder(f.getAbsolutePath());
            }else {
                f.delete();
            }
        }
        if(file.delete()){
            return true;
        }
        return false;
    }

    /**
     * 删除指定文件
     * @param filePath
     * @return
     */
    public static boolean deleteFile(String filePath){
        File f = new File(filePath);
        if(f.exists() && f.isFile()){
            f.delete();
            return true;
        }
        return false;
    }

    /**
     * 转换文件
     * @param fileS
     * @return
     */
    public static String FormetFileSize(long fileS) {
        Log.v("tangcy", "下载完成视频文件的大小："+fileS);
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 转换文件大小，去掉B单位 用于计算下载速度
     */
    public  static String  downSpeed(long speed) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (speed < 1024) {
            fileSizeString = df.format((double) speed) + "K";
        } else if (speed < 1048576) {
            fileSizeString = df.format((double) speed / 1024) + "M";
        } else if (speed < 1073741824) {
            fileSizeString = df.format((double) speed / 1048576) + "G";
        }
        return fileSizeString;
    }

    /**
     * 移动SD卡上的指定目录的所有文件
     *
     * @return
     * @throws IOException
     */
    public static boolean moveSDFilesTo(String SRCPATH,String SDPATH)throws IOException {
        File srcDir = new File(SRCPATH);
        File destDir = new File(SDPATH);
        return moveFilesTo(srcDir, destDir);
    }

    /**
     * 移动目录下的所有文件到指定目录
     *
     * @param srcDir
     * @param destDir
     * @return
     * @throws IOException
     */
    public static boolean moveFilesTo(File srcDir, File destDir) throws IOException {
        if (!srcDir.isDirectory() || !destDir.isDirectory()) {
            return false;
        }
        File[] srcDirFiles = srcDir.listFiles();
        for (int i = 0; i < srcDirFiles.length; i++) {
            if (srcDirFiles[i].isFile()) {
                File oneDestFile = new File(destDir.getPath() + "//"
                        + srcDirFiles[i].getName());
                moveFileTo(srcDirFiles[i], oneDestFile);
                delFile(srcDirFiles[i]);
            } else if (srcDirFiles[i].isDirectory()) {
                File oneDestFile = new File(destDir.getPath() + "//"
                        + srcDirFiles[i].getName());
                moveFilesTo(srcDirFiles[i], oneDestFile);
                delDir(srcDirFiles[i]);
            }

        }
        return true;
    }


    /**
     * 删除一个目录（可以是非空目录）
     *
     * @param dir
     */
    public static boolean delDir(File dir) {
        if (dir == null || !dir.exists() || dir.isFile()) {
            return false;
        }
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                delDir(file);// 递归
            }
        }
        dir.delete();
        return true;
    }

    /**
     * 移动一个文件
     *
     * @param srcFile
     * @param destFile
     * @return
     * @throws IOException
     */
    public static boolean moveFileTo(File srcFile, File destFile) throws IOException {
        boolean iscopy = copyFileTo(srcFile, destFile);
        if (!iscopy){
            return false;
        }
        delFile(srcFile);
        return true;
    }

    /**
     * 拷贝一个文件,srcFile源文件，destFile目标文件
     *
     * @throws IOException
     */
    public static boolean copyFileTo(File srcFile, File destFile) throws IOException {
        if (srcFile.isDirectory() || destFile.isDirectory())
            return false;// 判断是否是文件
        FileInputStream fis = new FileInputStream(srcFile);
        FileOutputStream fos = new FileOutputStream(destFile);
        int readLen = 0;
        byte[] buf = new byte[1024];
        while ((readLen = fis.read(buf)) != -1) {
            fos.write(buf, 0, readLen);
        }
        fos.flush();
        fos.close();
        fis.close();
        return true;
    }

    /**
     * 删除一个文件
     *
     * @param file
     * @return
     */
    public static boolean delFile(File file) {
        if (file.isDirectory())
            return false;
        return file.delete();
    }

    /**
     * 获取文件夹大小
     */
    public static long getFileSize(File f) throws Exception{
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++){
            if (flist[i].isDirectory()){
                size = size + getFileSize(flist[i]);
            }
            else{
                size = size + flist[i].length();
            }
        }
        return size;
    }


    public static String getRealFilePath(final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


    /**
     * 检查本地下载的视频买m3u8文件是否存在
     */
    public boolean isCheckSdVideoExists(Context context,HashMap<String, String> hashMap){
        boolean isExisits;

        File downloadFile_m3u8 = new File(context.getExternalFilesDir(null)+Constants.DOWNLOAD_M3U8);
        File downloadFile_secondSdcard = new File(spu.getSecondSdcard()+Constants.DOWNLOAD_M3U8+"//"+hashMap.get("tid")+hashMap.get("id")+".m3u8");
        File downloadFile_secondSdcard_api_19 = new File(spu.getSecondSdcard()+"/Android/data/"+ context.getPackageName()+Constants.DOWNLOAD_M3U8+"//"+hashMap.get("tid")+hashMap.get("id")+".m3u8");

        if(!downloadFile_m3u8.exists()&&!downloadFile_secondSdcard.exists()&&!downloadFile_secondSdcard_api_19.exists()){
            isExisits = false;
        }else {
            isExisits = true;
        }
        return isExisits;

    }


}
