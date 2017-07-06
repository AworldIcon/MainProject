package com.coder.kzxt.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;

import com.coder.kzxt.activity.R;

import java.io.IOException;
import java.util.HashMap;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by tangcy on 2017/3/2.
 */

public class ShareSdkUtil {

    private SharedPreferencesUtil spu;
    public ShareSdkUtil(SharedPreferencesUtil spu) {
        this.spu = spu;
    }
    /**
     * @param context
     * @param shareUrl 分享的url地址
     * @param content  分享内容
     */
    public  void shareSDK(Activity context, final String shareUrl, final String content) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        final String str = Environment.getExternalStorageDirectory().getPath();
        ShareSDK.initSDK(context, Constants.SHARESDKEY);
        HashMap<String,Object> qqMap = new HashMap<String, Object>();
        qqMap.put("Id","7");
        qqMap.put("SortId","7");
        qqMap.put("AppId",spu.getQQID());
        qqMap.put("AppSecret",spu.getQQSK());
        qqMap.put("ShareByAppClient","true");
        qqMap.put("Enable","true");
        ShareSDK.setPlatformDevInfo(QQ.NAME,qqMap);
        HashMap<String,Object> wxMap = new HashMap<String, Object>();
        wxMap.put("Id","4");
        wxMap.put("SortId","4");
//        map.put("AppId","wx7928b2d447773c91");
//        map.put("AppSecret","f02e0ea61f837d3ec40887a760e5aa68");
        wxMap.put("AppId",spu.getWXID());
        wxMap.put("AppSecret",spu.getWXSK());
        wxMap.put("BypassApproval","true");
        wxMap.put("Enable","true");
        ShareSDK.setPlatformDevInfo(Wechat.NAME,wxMap);
        OnekeyShare oks = new OnekeyShare();

        if(TextUtils.isEmpty(spu.getQQID())||spu.getShowQLogin().equals("0")){
            oks.addHiddenPlatform(QQ.NAME);
        }
        if(TextUtils.isEmpty(spu.getWXID())||spu.getShowWLogin().equals("0")){
            oks.addHiddenPlatform(Wechat.NAME);
        }

        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//        oks.setTitle(content);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(shareUrl);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(content);
        try {
            Bimp.saveBitmapToFile(bitmap, str + "/shareIcon.png", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        oks.setImagePath(str + "/shareIcon.png");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(shareUrl);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(context.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(shareUrl);
        // 启动分享GUI
        oks.show(context);
    }

}
