package com.app.http;

import android.content.Context;
import android.text.TextUtils;

import com.app.utils.BaseConstants;
import com.app.utils.BaseSharedPreferencesUtil;
import com.app.utils.Decrypt_Utils;
import com.app.utils.L;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * utils 里面有其他形式的httpPost
 */
public class HttpPost
{
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private InterfaceHttpResult mHttpResult;
    private Object objectTag;
    private Platform mPlatform;
    private Class<?> classObj;
    private String postUrl;
    private Map<String, String> postMap;
    private String[] params;
    private int requestCode;
    private Map<String, String> postImages;
    private Map<String, String> fileParams;
    private BaseSharedPreferencesUtil sharedPreferencesUtil;


    public HttpPost(Context context, int requestCode, InterfaceHttpResult listener, Class<?> className, String... params)
    {
        this(context, requestCode, listener, className, null, params);
    }

    /**
     * httpPost 构造
     *
     * @param context    baseActivity
     * @param listener   服务器 回调接口
     * @param className  返回结果 强转对象
     * @param postImages 图片请求参数
     * @param params     请求参数 第一位是url
     */

    public HttpPost(Context context, int requestCode, InterfaceHttpResult listener, Class<?> className, Map<String, String> postImages, String... params)
    {
        this.requestCode = requestCode;
        this.objectTag = context.getClass().getSimpleName();
        this.mHttpResult = listener;
        this.mPlatform = Platform.getInstance();
        this.classObj = className;
        this.params = params;
        this.postImages = postImages;
        // 获取用户的信息一些信息
        this.sharedPreferencesUtil = new BaseSharedPreferencesUtil(context);
    }


    public void execute()
    {
        execute(0);
    }

    /**
     * 添加请求code
     *
     * @param requestCode 请求码默认是0
     */
    public void execute(int requestCode)
    {
        this.requestCode = requestCode;
        if (params == null || params.length == 0)
        {
            L.e("params is null or params length is 0");
            setHttpResultString("");
            return;
        }

        postMap = new HashMap<>();
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

        resetPostMap(params, keys);

        try
        {
            okHttpRequest();
        } catch (IOException e)
        {
            L.e("HttpGetOld okHttpRequest IOException " + e.toString());
            e.printStackTrace();
        }
    }

    //获取请求的url 和 params参数
    private void resetPostMap(String[] params, String[] keys)
    {

        postUrl = keys[0];
        L.d("postUrl = " + postUrl);
        //params 第一位是url  后面的是value
        //url 包含请求地址和请求需要的key
        for (int i = 1; i < params.length; i++)
        {
            postMap.put(keys[i], params[i]);
            L.d("httpPost " + keys[i] + " = " + params[i]);
        }

        L.d("httpPost mid  = " + sharedPreferencesUtil.getUid());
        L.d("httpPost oauth_token  = " + sharedPreferencesUtil.getIsLogin());
        L.d("httpPost oauth_token_secret  = " + sharedPreferencesUtil.getTokenSecret());
        L.d("httpPost deviceId  = " + sharedPreferencesUtil.getDevicedId());

        postMap.put("mid", sharedPreferencesUtil.getUid());
        postMap.put("oauth_token", sharedPreferencesUtil.getIsLogin());
        postMap.put("oauth_token_secret", sharedPreferencesUtil.getTokenSecret());
        postMap.put("deviceId", sharedPreferencesUtil.getDevicedId());
    }

    /**
     * 调用ok post 请求返回结果
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
        // TODO: 2016/12/7 需要cookieJar ？
//        client.newBuilder().cookieJar(new CookieManager(new PersistentCookieStore(context), CookiePolicy.ACCEPT_ALL));

        MultipartBody.Builder multipartEntityBuilder = new MultipartBody.Builder();
        multipartEntityBuilder.setType(MultipartBody.FORM);

        //添加params
        addParams(multipartEntityBuilder);

        if (postImages != null)
        {
            Iterator<Map.Entry<String, String>> strings = postImages.entrySet().iterator();
            while (strings.hasNext())
            {
                Map.Entry<String, String> entry = strings.next();
                String key = entry.getKey();
                String value = entry.getValue();
                File file = new File(value);
                multipartEntityBuilder.addFormDataPart(key, file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
            }
        }


        //获取requestBody
        RequestBody body = multipartEntityBuilder.build();
        //设置请求url和body 获取request
        Request.Builder builde = new Request.Builder();

        if (this.objectTag != null)
        {
            builde.tag(this.objectTag);
        }
        Request request = builde
                .url(postUrl)
                .post(body)
                .build();

        //Call 回去返回结果
        Call call = client.newCall(request);
        call.enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                final String s = "call " + e.toString();
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
                    L.e("call is canceled");
                } else
                {
                    sendSuccessResultCallback(response);
                }
                if (HttpPost.this.objectTag != null)
                {
                    BaseActivityLifecycleCallbacks.cancelCall(HttpPost.this.objectTag.getClass(), call);
                }
            }
        });
        if (this.objectTag != null)
        {
            BaseActivityLifecycleCallbacks.putCall(this.objectTag.getClass(), call);
        }

    }

    private void addParams(MultipartBody.Builder builder)
    {
        if (postMap != null)
        {
            Iterator<Map.Entry<String, String>> strings = postMap.entrySet().iterator();
            while (strings.hasNext())
            {
                Map.Entry<String, String> entry = strings.next();
                String key = entry.getKey();
                Object value = entry.getValue();
                builder.addFormDataPart(key, value + "");
            }
        }
    }

    /**
     * 设置回调 返回给主线程
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
            e.printStackTrace();
        }

    }

    /**
     * 设置回调内容
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

    public void setFileParams(Map<String, String> fileParams)
    {
        this.fileParams = fileParams;
    }
}
