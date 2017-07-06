package com.coder.kzxt.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.app.utils.L;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import static com.coder.kzxt.utils.SdcardUtils.createSDDir;
import static com.coder.kzxt.utils.SdcardUtils.isFileExist;

/**
 * 处理上传图片工具类
 */
public class Bimp
{

    public static int max = 0; //记录当前有几张图片，如果不等于集合里的图片数量那么添加新图片
    public static List<Bitmap> bmp = new ArrayList<Bitmap>(); //存放图片bitmap集合
    public static List<String> drr = new ArrayList<String>();//存放图片sd卡路径集�?
    public static List<String> thumbnail_drr = new ArrayList<String>();//存放图片sd卡缩略图路径集合

    // 图片sd地址 上传服务器时把图片调用下面方法压缩后 保存到临时文件夹 图片压缩后小�??00KB，失真度不明�??
    public static Bitmap revitionImageSize(String path) throws IOException
    {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 这个参数代表，不为bitmap分配内存空间，只记录�?些该图片的信息（例如图片大小），说白了就是为了内存优
        options.inJustDecodeBounds = true;
        // 通过创建图片的方式，取得options的内容（这里就是利用了java的地�?传�?�来赋�?�）
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true)
        {
            // 这一步是根据要设置的大小，使宽和高都能满�?
            if ((options.outWidth >> i <= 1000) && (options.outHeight >> i <= 1000))
            {
                // 重新取得流，注意：这里一定要再次加载，不能二次使用之前的流！
                in = new BufferedInputStream(new FileInputStream(new File(path)));
                // 这个参数表示 新生成的图片为原始图片的几分之一�?
                options.inSampleSize = (int) Math.pow(2.0D, i);
                // 这里之前设置为了true，所以要改为false，否则就创建不出图片
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                in.close();
                break;
            }
            i += 1;
        }
        return bitmap;
    }


    /**
     * 把图片保存到sd卡
     *
     * @param imgUrl
     * @param sdUrl
     */
    public static void saveImgToSd(final String imgUrl, final String sdUrl)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    URL url = new URL(imgUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap tmpBitmap = BitmapFactory.decodeStream(input);
                    saveBitmapToFile(tmpBitmap, sdUrl, false);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }


            }
        }).start();
    }

    public static void saveBitmapToFile(final Bitmap bitmap, final String saveUrl, boolean png) throws IOException
    {
        if (bitmap == null)
        {
            return;
        }
        BufferedOutputStream os = null;
        try
        {
            final File file = new File(saveUrl);
            final int end = saveUrl.lastIndexOf(File.separator);
            final String _filePath = saveUrl.substring(0, end);
            final File filePath = new File(_filePath);
            if (!filePath.exists())
            {
                filePath.mkdirs();
            }
            file.createNewFile();
            os = new BufferedOutputStream(new FileOutputStream(file));
            if (png)
            {
                bitmap.compress(Bitmap.CompressFormat.PNG, 80, os);
            } else
            {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, os);
            }
        } finally
        {
            if (os != null)
            {
                try
                {
                    os.close();
                } catch (final IOException e)
                {
                }
            }
        }
    }

    /**
     * 保存图片缩略图
     *
     * @param bm
     * @param picName
     */
    public static String saveBitmap(Bitmap bm, String picName)
    {
        try
        {
            if (!isFileExist(""))
            {
                File tempf = createSDDir("");
            }
            File f = new File(Constants.POST_PHOTO, picName + ".JPEG");
            if (!f.exists())
            {
                f.mkdirs();
            }
            f.delete();
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            L.d("保存图片"+f.getAbsolutePath());
            return f.getAbsolutePath();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 检查本地图片的宽度是否大于320
     */
    public static boolean getBitmapWH(String path){
        BitmapFactory.Options options = new BitmapFactory.Options();
        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
        /**
         *options.outHeight为原始图片的高
         */
        int bitMapW = options.outWidth;
        L.v("tangcy","图片的宽度"+bitMapW);
        if(bitMapW>320){
            return true;
        }else {
            return false;
        }
    }


    /**
     * 生成二维码.
     * @param url 需要生成的字符串
     * @return bitmap
     */
    public static Bitmap createQRImage(String url) {
        try {
            // 判断URL合法性
            if (url == null || "".equals(url) || url.length() < 1) {
                return null;
            }
            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.MARGIN, 1);
            // 图像数据转换，使用了矩阵转换
            BitMatrix matrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, 300, 320,hints);
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            int[] pixels = new int[width * height];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (matrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    } else {
                        pixels[y * width + x] = 0xffffffff;
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

}
