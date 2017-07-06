package com.coder.kzxt.im.business;

import android.content.Context;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.app.utils.LogWriter;
import com.coder.kzxt.base.beans.ImInfoBean;
import com.coder.kzxt.im.beans.GroupInfo;
import com.coder.kzxt.im.beans.UserInfo;
import com.coder.kzxt.im.conversation.FriendshipInfo;
import com.coder.kzxt.im.event.FriendshipEvent;
import com.coder.kzxt.im.event.GroupEvent;
import com.coder.kzxt.im.event.MessageEvent;
import com.coder.kzxt.login.beans.ImInfoResult;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.MyApplication;
import com.tencent.TIMCallBack;
import com.tencent.TIMManager;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.core.impl.ILVBLogin;

/**
 * Created by MaShiZhao on 2017/3/28
 */

public class ImLoginBusiness implements HttpCallBack, TIMCallBack
{
    private Context mContext;
    //登录回调
    private HttpCallBack httpCallBack;
    //登录请求Crequestode
    private int requestCode;


    public ImLoginBusiness()
    {
        mContext = MyApplication.getInstance();
    }

    public ImLoginBusiness setHttpCallBack(HttpCallBack httpCallBack)
    {
        this.httpCallBack = httpCallBack;
        return this;
    }

    public ImLoginBusiness setRequestCode(int requestCode)
    {
        this.requestCode = requestCode;
        return this;
    }

    //退出 是否重新登录 首页调用
    public void logout(final boolean isRestart)
    {
        TIMManager.getInstance().logout(new TIMCallBack()
        {
            @Override
            public void onError(int i, String s)
            {
                LogWriter.e("退出失败" + i + s);
            }

            @Override
            public void onSuccess()
            {
                UserInfo.getInstance().setId(null);
                MessageEvent.getInstance().clear();
                FriendshipInfo.getInstance().clear();
                GroupInfo.getInstance().clear();
                if (isRestart)
                {
                    login();
                }
            }
        });
    }

    // 登录
    public void login()
    {
        new HttpGetBuilder(this.mContext)
                .setHttpResult(this)
                .setClassObj(ImInfoResult.class)
                .setUrl(UrlsNew.USER_IM_PROFILE)
                .setRequestCode(requestCode)
                .build();
    }

    //本地接口的回调
    @Override
    public void setOnSuccessCallback(final int requestCode, Object resultBean)
    {
        final ImInfoBean imInfoBean = ((ImInfoResult) resultBean).getItem();

        //登录之前要初始化群和好友关系链缓存
        FriendshipEvent.getInstance().init();
        GroupEvent.getInstance().init();
        //登录
//        TIMManager.getInstance().login(Integer.valueOf(imInfoBean.getSdk_app_id()), imInfoBean.getTimUser(), imInfoBean.getUser_sig(), ImLoginBusiness.this);
        ILVBLogin.getInstance().iLiveLogin(imInfoBean.getIdentifier(), imInfoBean.getUser_sig(), new ILiveCallBack()
        {
            @Override
            public void onSuccess(Object data)
            {
                UserInfo.getInstance().setId(imInfoBean.getIdentifier());
                if (httpCallBack != null)
                {
                    //登录成功做些处理
                    httpCallBack.setOnSuccessCallback(requestCode, null);
                }
            }

            @Override
            public void onError(String module, int errCode, String errMsg)
            {
                if (httpCallBack != null)
                {
                    httpCallBack.setOnErrorCallback(requestCode, errCode, errMsg);
                }
            }
        });
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg)
    {
        if (httpCallBack != null)
        {
            httpCallBack.setOnErrorCallback(requestCode, code, msg);
        }
    }

    //im登录的回调
    @Override
    public void onSuccess()
    {
        if (httpCallBack != null)
        {
            //登录成功做些处理
            httpCallBack.setOnSuccessCallback(requestCode, null);
        }

    }
//    /**
//     * 实际初始化AVSDK
//     */
//    private void startContext(String identifier) {
//        if (hasAVContext() == false) {
//            AVContext.StartParam mParam  mParam = new AVContext.StartParam();
//            mParam.sdkAppId = ILiveSDK.getInstance().getAppId();
//            mParam.accountType = "" + ILiveSDK.getInstance().getAccountType();
//            mParam.appIdAt3rd = Integer.toString(ILiveSDK.getInstance().getAppId());
//            mParam.identifier = identifier;
//            mAVContext = AVContext.createInstance(ILiveSDK.getInstance().getAppContext(), false);
//            if (null == mAVContext) {
//                ILiveFunc.notifyError(loginListener, ILiveConstants.Module_AVSDK, -1, "start av context return null");
//                loginListener = null;   // 用完置空
//                return;
//            }
//            mAVContext.start(mParam, mStartContextCompleteCallback);
//        } else {
//            ILiveFunc.notifySuccess(loginListener, 0);
//            loginListener = null;   // 用完置空
//        }
//    }

    @Override
    public void onError(int i, String msg)
    {
        if (httpCallBack != null)
        {
            if (i == 6208)
            {
                //离线状态下被其他终端踢下线
                httpCallBack.setOnErrorCallback(requestCode, requestCode, msg);
            } else
            {
                // 6200 网络
                httpCallBack.setOnErrorCallback(requestCode, Constants.HTTP_CODE_4000, msg);
            }
        }
    }


}
