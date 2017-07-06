package com.app.http;

import android.content.Context;
import android.text.TextUtils;

import com.app.utils.BaseConstants;
import com.app.utils.BaseSharedPreferencesUtil;
import com.app.utils.Decrypt_Utils;
import com.app.utils.L;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpGetOld
{

    private InterfaceHttpResult mHttpResult;
    private Platform mPlatform;
    private Object objectTag;
    private Class<?> classObj;
    private String postUrl;
    private String[] params;
    private int requestCode;
    private BaseSharedPreferencesUtil sharedPreferencesUtil;

    public void setmHttpResult(InterfaceHttpResult mHttpResult)
    {
        this.mHttpResult = mHttpResult;
    }

    public void setObjectTag(Object objectTag)
    {
        this.objectTag = objectTag;
    }

    public void setClassObj(Class<?> classObj)
    {
        this.classObj = classObj;
    }

    public void setPostUrl(String postUrl)
    {
        this.postUrl = postUrl;
    }

    public void setParams(String[] params)
    {
        this.params = params;
    }

    public void setRequestCode(int requestCode)
    {
        this.requestCode = requestCode;
    }

    public void setResultString(String resultString)
    {
        this.resultString = resultString;
    }


    public HttpGetOld()
    {
    }

    /**
     * 构造函数
     *
     * @param object    页面tag
     * @param listener  服务器 回调接口
     * @param className 返回结果 强转对象
     * @param params    请求参数 第一位是url
     */
    public HttpGetOld(Context context, Object object, InterfaceHttpResult listener, Class<?> className, String... params)
    {
        this.objectTag = object;
        this.mHttpResult = listener;
        this.mPlatform = Platform.getInstance();
        this.classObj = className;
        this.params = params;
        sharedPreferencesUtil = new BaseSharedPreferencesUtil(context);
//        onInit(params);
    }

    public void excute()
    {
        excute(0);
    }

    /**
     * @param requestCode 请求码默认是0
     */
    public void excute(int requestCode)
    {
        this.requestCode = requestCode;
        if (params == null || params.length == 0)
        {
            setHttpResultString("params is null or params length is 0");
            return;
        }

        String url = params[0];
        String[] keys = url.split(";");

        if (keys.length > params.length)
        {
            setHttpResultString("请求参数小于设置数量");
            return;
        }

        if (keys.length < params.length)
        {
            setHttpResultString("请求参数大于设置数量");
            return;
        }
        getRequestUrl(params, keys);
        try
        {
            okHttpRequest();
        } catch (IOException e)
        {
            L.e("HttpGetOld okHttpRequest IOException " + e.toString());
            e.printStackTrace();
        }
    }

    private void getRequestUrl(String[] params, String[] keys)
    {
        postUrl = keys[0];
        //params 第一位是url  后面的是value
        //url 包含请求地址和请求需要的key
        for (int i = 1; i < params.length; i++)
        {
            String connectString;
            if (i == 1)
            {
                connectString = "?";
            } else
            {
                connectString = "&";
            }
            postUrl = postUrl + connectString + keys[i] + "=" + params[i];

        }

        L.d("httpGet mid  = " + sharedPreferencesUtil.getUid());
        L.d("httpGet oauth_token  = " + sharedPreferencesUtil.getIsLogin());
        L.d("httpGet oauth_token_secret  = " + sharedPreferencesUtil.getTokenSecret());
        L.d("httpGet deviceId  = " + sharedPreferencesUtil.getDevicedId());

        String deviceString = "deviceId=" + sharedPreferencesUtil.getDevicedId();
        if (params.length == 1)
        {
            postUrl = postUrl + "?" + deviceString;
        } else
        {
            postUrl = postUrl + "&" + deviceString;
        }

        if (!postUrl.contains("loginAction"))
        {
            String mid = "mid=" + sharedPreferencesUtil.getUid();
            String oauth_token = "oauth_token=" + sharedPreferencesUtil.getIsLogin();
            String oauth_token_secret = "oauth_token_secret=" + sharedPreferencesUtil.getTokenSecret();
            postUrl = postUrl + "&" + mid + "&" + oauth_token + "&" + oauth_token_secret;
        }

        L.d(postUrl+"&debug=1");
    }

    /**
     * 调用ok请求返回结果
     *
     * @throws IOException
     */
    private void okHttpRequest() throws IOException
    {
        int timeoutConnection = 60;
        // OkHttpClient 初始化配置
        OkHttpClient client = new OkHttpClient();
        client.newBuilder().connectTimeout(timeoutConnection, TimeUnit.SECONDS);
        client.newBuilder().readTimeout(timeoutConnection, TimeUnit.SECONDS);
        client.newBuilder().writeTimeout(timeoutConnection, TimeUnit.SECONDS);

        //Request Builder 配置
        Request.Builder builder = new Request.Builder();
        if (this.objectTag != null)
        {
            builder.tag(this.objectTag);
        }
        builder.url(postUrl);

        Request request = builder.build();
        //Call 回去返回结果
        Call call = client.newCall(request);
        call.enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                L.d("call onFailure : " + e.toString());
                final String s = e.toString();
                mPlatform.execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        setHttpResultString(s);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                if (call.isCanceled())
                {
                    L.d("call is canceled");
                } else
                {
                    sendSuccessResultCallback(response);
                }
                if (HttpGetOld.this.objectTag != null)
                {
                    BaseActivityLifecycleCallbacks.cancelCall(HttpGetOld.this.objectTag.getClass(), call);
                }
            }
        });
        if (this.objectTag != null)
        {
            BaseActivityLifecycleCallbacks.putCall(this.objectTag.getClass(), call);
        }

    }


    /**
     * 设置回调 返回给主线程 请求服务器成功的回调
     *
     * @param response response
     */
    private String resultString;
    private JSONObject jsonO;

    private void sendSuccessResultCallback(final Response response)
    {
        if (mHttpResult == null)
        {
            L.e("HttpResult should not be null");
            return;
        }
        try
        {
            resultString = response.body().string();
            mPlatform.execute(new Runnable()
            {
                @Override
                public void run()
                {

                    //返回结果进行解密
                    try
                    {
                        String jsonStrDes = Decrypt_Utils.decode(BaseConstants.HTTP_KEY, resultString);
                        jsonO = new JSONObject(jsonStrDes);
                        L.d("ResultString" + jsonStrDes);
                        int code = 0;
                        if (jsonO.has("code"))
                        {
                            code = jsonO.getInt("code");
                        }
                        String msg = "";
                        if (jsonO.has("msg") && jsonO.getString("msg") != null)
                        {
                            msg = jsonO.getString("msg");
                        }
                        Object bean = null;
                        if (code == BaseConstants.HTTP_CODE_SUCCESS)
                        {
                            if (classObj != null)
                            {
                                bean = new Gson().fromJson(jsonO.toString(), classObj);
                            }
                        }
                        mHttpResult.getCallback(requestCode, code, msg, bean);

                    } catch (Exception e)
                    {
                        mHttpResult.getCallback(requestCode, BaseConstants.HTTP_CODE_5000, e.toString(), null);
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e)
        {
            L.e("mHttpResult result call back response.body().string " + e.toString());
            e.printStackTrace();
        }

    }

    /**
     * 设置失败的回调
     */
    public void setHttpResultString(String resultString)
    {
        if (TextUtils.isEmpty(resultString))
        {
            resultString = "接收数据错误!";
        }
        L.e("请求出错：" + resultString);
        mHttpResult.getCallback(requestCode, BaseConstants.HTTP_CODE_4000, resultString, null);
    }


    public String getStringData()
    {
        return jsonO.toString();
    }
}
