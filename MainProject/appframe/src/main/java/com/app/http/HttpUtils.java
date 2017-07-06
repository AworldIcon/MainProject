package com.app.http;

import android.content.Context;

import com.app.utils.BaseConstants;
import com.app.utils.BaseSharedPreferencesUtil;
import com.app.utils.L;
import com.app.utils.LogWriter;
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
 * utils
 */
public class HttpUtils
{
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    // 四种提交方式  get post put 添加 delete 删除
    public enum METHOD
    {
        GET, POST, PUT, DELETE
    }

    /**
     * 请求结果回调
     */
    private HttpCallBack httpResult;

    /**
     * 请求摄者tag 用于取消该网络请求
     */
    private Object objectTag;
    private Platform mPlatform;

    /**
     * 请求的code
     */
    private int requestCode;
    /**
     * 解析对象
     */
    private Class<?> classObj;

    /**
     * 请求的url 明文的  http://192.168.3.8/login
     */
    private String postUrl;

    /**
     * 放到body里面的参数和公共参数 需要加密的内容
     */
    private Map<String, String> postMap;

    /**
     * 提交的文件的集合 例如图片，视频
     */
    private Map<String, String> fileParams;

    /**
     * share里面存储的数据 用户信息 主要是token
     */
    private BaseSharedPreferencesUtil sharedPreferencesUtil;

    private String[] params;

    /**
     * path的请求参数 注意 这个是有顺序的
     */
    private String[] pathParams;

    /**
     * query的请求参数  明文 ？后面需要拼接的
     */
    private Map<String, String> queryParams;

    /**
     * body的请求参数放在body里面的内容，post put delete
     * 先阶段body的value只支持string 如果jsonArray是其它形式的并非string 需要添加个信的body类型
     */
    private Map<String, String[]> bodyParams;

    private String bodyString;

    private String signString;

    private METHOD method;

    private Context mContext;


    public HttpUtils(Context context, int requestCode, HttpCallBack listener, Class<?> className, METHOD method, String postUrl, String bodyString, String signString)
    {
        this(context, requestCode, listener, className, method, null, postUrl, bodyString, signString);
    }

    /**
     * httpPost 构造
     *
     * @param context    baseActivity
     * @param listener   服务器 回调接口
     * @param className  返回结果 强转对象
     * @param postImages 图片请求参数
     */

    public HttpUtils(Context context, int requestCode, HttpCallBack listener, Class<?> className, METHOD method, Map<String, String> postImages, String postUrl, String bodyString, String signString)
    {
        this.mContext = context;
        this.requestCode = requestCode;
        this.objectTag = context.getClass().getSimpleName();
        this.httpResult = listener;
        this.method = method;
        this.mPlatform = Platform.getInstance();
        this.classObj = className;
        this.postUrl = postUrl;
        this.bodyString = bodyString;
        this.signString = signString;
        this.fileParams = postImages;
        // 获取用户的信息一些信息
        this.sharedPreferencesUtil = new BaseSharedPreferencesUtil(context);
        this.postMap = new HashMap<>();

    }


    /**
     * 调用ok post 请求返回结果
     *
     * @throws IOException
     */
    public void execute()
    {

        if (httpResult == null)
        {
            L.e("httpResult is null");
            return;
        }
//        if (this.classObj == null)
//        {
//            L.e("resultBean is null");
//            return;
//        }

        int timeoutConnection = 60;
        // OkHttpClient 初始化配置
        OkHttpClient client = new OkHttpClient();
        client.newBuilder().connectTimeout(timeoutConnection, TimeUnit.SECONDS);
        client.newBuilder().readTimeout(timeoutConnection, TimeUnit.SECONDS);
        client.newBuilder().writeTimeout(timeoutConnection, TimeUnit.SECONDS);

        //设置请求url和body 获取request
        Request.Builder builder = new Request.Builder();

        //设置header
        builder.addHeader("Api-Sign", signString);
        builder.addHeader("Api-Token", sharedPreferencesUtil.getToken());
        builder.addHeader("Device-Id", sharedPreferencesUtil.getDevicedId());
        builder.addHeader("Version-Name", sharedPreferencesUtil.getVersionName());
        MultipartBody.Builder multipartEntityBuilder = new MultipartBody.Builder();

        L.v("httpHeaderParams"+"Api-Sign"+"--"+signString);
        L.v("httpHeaderParams"+"Token"+"--"+sharedPreferencesUtil.getToken());
        L.v("httpHeaderParams"+"Device-Id"+"--"+sharedPreferencesUtil.getDevicedId());
        L.v("httpHeaderParams"+"Version-Name"+"--"+sharedPreferencesUtil.getVersionName());

        if (fileParams != null)
        {
            multipartEntityBuilder.setType(MultipartBody.FORM);
            Iterator<Map.Entry<String, String>> strings = fileParams.entrySet().iterator();
            while (strings.hasNext())
            {
                Map.Entry<String, String> entry = strings.next();
                String key = entry.getKey();
                String value = entry.getValue();
                File file = new File(value);
                multipartEntityBuilder.addFormDataPart(key, file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
            }

        }


        //设置tag
        if (this.objectTag != null)
        {
            builder.tag(this.objectTag);
        }

        //删除掉转义字符
//        bodyString = bodyString.replace("\\\\","\\").replace("\\/","/");
        LogWriter.d("bodyString", bodyString);
        //设置body
        RequestBody body = null;
        if (fileParams != null)
        {//只有上传图片时候不为空,且目前服务器传文件没有body体这种特殊的类型，参数直接拼接在url中了
            body = multipartEntityBuilder.build();
        } else
        {
            body = RequestBody.create(JSON, bodyString);
        }
        //根据实际情况去 请求
        Request request;
        if (!postUrl.startsWith("http"))
            postUrl = UrlsNew.URLHeader + postUrl;
        switch (method)
        {
            case GET:
                request = builder
                        .url(postUrl)
                        .build();
                break;
            case POST:
                request = builder
                        .url(postUrl)
                        .post(body)
                        .build();
                break;
            case PUT:
                // 修改
                request = builder
                        .url(postUrl)
                        .put(body)
                        .build();
                break;
            case DELETE:
                // 删除
                request = builder
                        .url(postUrl)
                        .delete(body)
                        .build();
                break;
            default:
                //默认是get
                request = builder
                        .url(postUrl)
                        .build();
                break;
        }


        //Call 回去返回结果
        Call call = client.newCall(request);
        call.enqueue(new Callback()
        {
            @Override
            public void onFailure(final Call call, IOException e)
            {
                final String s = "call " + e.toString();
                mPlatform.execute(new Runnable()
                {
                    @Override
                    public void run()
                    {

                        setOnErrorCallback(BaseConstants.HTTP_CODE_4000, s);

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                if (call.isCanceled())
                {
                    L.v("call is canceled");
                } else
                {
                    setOnSuccessCallback(response);
                }
                if (HttpUtils.this.objectTag != null)
                {
                    BaseActivityLifecycleCallbacks.cancelCall(HttpUtils.this.objectTag.getClass(), call);
                }
            }
        });
        if (this.objectTag != null)
        {
            BaseActivityLifecycleCallbacks.putCall(this.objectTag.getClass(), call);
        }

    }


    /**
     * 设置回调 返回给主线程
     *
     * @param response response
     */

    private String resultString = "";


    private void setOnSuccessCallback(final Response response)
    {
        L.d(method + " url : " + postUrl);
        sharedPreferencesUtil.setToken(response.headers().get("Api-Token"));

        try
        {
            resultString = response.body().string();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        L.d(" ResultString :" + resultString);

        try
        {
            JSONObject jsonO = new JSONObject(resultString);
            if (jsonO.has("code") && jsonO.has("message"))
            {
                final int code = jsonO.getInt("code");
                final String message = jsonO.getString("message");
                if (code == BaseConstants.HTTP_CODE_SUCCESS)
                {
                    //请求成功 但是没有传递解析对象 ，返回json字符串
                    if (classObj == null)
                    {
                        mPlatform.execute(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                httpResult.setOnSuccessCallback(requestCode, resultString);
                            }
                        });

                    } else
                    {
                        final Object bean = new Gson().fromJson(jsonO.toString(), classObj);
                        mPlatform.execute(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                httpResult.setOnSuccessCallback(requestCode, bean);
                            }
                        });
                    }

                } else
                {
                    mPlatform.execute(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            setOnErrorCallback(code, message);
                        }
                    });
                }
            } else
            {
                mPlatform.execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        setOnErrorCallback(BaseConstants.HTTP_CODE_4000, "返回json：" + resultString);
                    }
                });

            }

        } catch (final Exception e)
        {
            final String string = e.toString();
            mPlatform.execute(new Runnable()
            {
                @Override
                public void run()
                {
                    setOnErrorCallback(BaseConstants.HTTP_CODE_4000, "解析出错 " + string);
                }
            });
            e.printStackTrace();
        }

    }

    private void setOnErrorCallback(int code, String msg)
    {
        L.e("setOnErrorCallback code : " + code + " msg : " + msg);
        httpResult.setOnErrorCallback(requestCode, code, msg);
    }

}
