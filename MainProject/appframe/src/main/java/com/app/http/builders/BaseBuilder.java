package com.app.http.builders;

import android.content.Context;
import android.text.TextUtils;

import com.app.http.HttpCallBack;
import com.app.utils.Encrypt;
import com.app.utils.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by MaShiZhao on 2017/1/6
 */
public abstract class BaseBuilder
{
    // 请求的context
    Context context;
    //请求的回调接口
    HttpCallBack interfaceHttpResult;
    //解析的对象
    Class<?> classObj;
    //请求url
    StringBuffer postUrl;

    /**
     * path的请求参数 注意 这个是有顺序的
     */
    private String[] pathParams;

    /**
     * query的请求参数  明文 ？后面需要拼接的
     */
    private Map<String, String> queryParams;
    private Map<String, String> fileNames;

    /**
     * body的请求参数放在body里面的内容，post put delete
     * 先阶段body的value只支持string 如果jsonArray是其它形式的并非string 需要添加个信的body类型
     */
    private Map<String, String[]> bodyParams;
    private String bodyString;

    //封装body对象 转化为string 公共参数 业务参数
    private JSONObject bodyJsonObject;

    /**
     * 放到body里面的参数和公共参数 需要加密的内容
     */
    private Map<String, String> postMap;


    //请求码
    int requestCode;
    //请求时间戳
    String time;

    public BaseBuilder(Context context)
    {
        this.context = context;
        this.time = System.currentTimeMillis() / 1000 + "";
        bodyString = "";
        this.bodyJsonObject = new JSONObject();
        this.queryParams = new HashMap<>();
        this.postMap = new HashMap<>();
        this.postMap.put("platform", "android");
        this.postMap.put("timestamp", time);
    }

    // 有顺序的 先请求的在前面
    public BaseBuilder setPath(String... path)
    {
        this.pathParams = path;
        return this;
    }

    public BaseBuilder setHttpResult(HttpCallBack interfaceHttpResult)
    {
        this.interfaceHttpResult = interfaceHttpResult;
        return this;
    }


    public BaseBuilder setClassObj(Class<?> classObj)
    {
        this.classObj = classObj;
        return this;
    }
    public BaseBuilder setFileNames(Map<String,String> fileNames){
        this.fileNames=fileNames;
        return this;
    }
    public Map<String,String> getFileNames(){
        return fileNames;
    }
    public BaseBuilder setUrl(String postUrl)
    {
        this.postUrl = new StringBuffer(postUrl);
        return this;
    }

    public BaseBuilder setRequestCode(int requestCode)
    {
        this.requestCode = requestCode;
        return this;
    }

    //添加query参数 需要加密的post也添加
    public BaseBuilder addQueryParams(String key, String value)
    {
//        try
//        {
//            bodyJsonObject.put(key, value);
//        } catch (JSONException e)
//        {
//            e.printStackTrace();
//        }
        this.queryParams.put(key, value);
        return this;
    }

    //arrays数组
    public BaseBuilder addBodyParam(String arrays)
    {

        this.bodyString = arrays;
        return this;
    }

    //添加body参数 需要加密的post也添加
    public BaseBuilder addBodyParam(String key, String... values)
    {
        try
        {
            if (values.length == 1)
            {

                String param = values[0];
                L.d("addBodyParam: param " + param);
                bodyJsonObject.put(key, param);
            } else
            {
                JSONArray jsonarray = new JSONArray();
                for (String param : values)
                {
                    jsonarray.put(param);
                }
                bodyJsonObject.put(key, jsonarray);
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return this;
    }

    protected String getUrlString()
    {
        //如果path 不为空时，对path数组进行拼接
        if (pathParams != null)
        {
            for (String path : pathParams)
            {
                this.postUrl.append("/").append(path);
            }
        }
        // 对参数进行封装 添加公共参数 时间戳和平台信息
        postUrl.append("?timestamp=").append(time).append("&platform=android");
        // 如果query存在数据时 需要拼接成明文
        if (queryParams != null)
        {
            Iterator<Map.Entry<String, String>> strings = queryParams.entrySet().iterator();
            while (strings.hasNext())
            {
                Map.Entry<String, String> entry = strings.next();
                String key = entry.getKey();
                String value = entry.getValue();
                this.postUrl.append("&").append(key).append("=").append(value);
                this.postMap.put(key, value);
            }
        }

        return postUrl.toString();
    }

    //获取bodyString
    protected String getBodyString()
    {
        if (!TextUtils.isEmpty(bodyString))
        {
            return bodyString;
        }

        // 如果body存在数据时 需要封装成string 放到body里面
        if (bodyParams != null)
        {
            Iterator<Map.Entry<String, String[]>> strings = bodyParams.entrySet().iterator();
            while (strings.hasNext())
            {
                Map.Entry<String, String[]> entry = strings.next();
                String key = entry.getKey();
                String[] value = entry.getValue();
                try
                {
                    //如果只有一个，当成string处理 否则当成数组处理
                    if (value.length == 1)
                    {
                        bodyJsonObject.put(key, value[0]);
                        this.postMap.put(key, value[0]);
                    } else
                    {
                        JSONArray jsonarray = new JSONArray();
                        for (String param : value)
                        {
                            jsonarray.put(param);
                        }
                        bodyJsonObject.put(key, jsonarray);
                        this.postMap.put(key, jsonarray.toString());
                    }
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
        if (bodyJsonObject.length() == 0)
        {
            return "";
        } else
        {
            return bodyJsonObject.toString();
        }
    }


    protected String getSignString()
    {
        if (!TextUtils.isEmpty(this.bodyString))
        {
            postMap.put("body", bodyString);
        }

        int i = bodyJsonObject.length();

        if (!TextUtils.isEmpty(bodyJsonObject.toString()) && i > 0)
        {
            postMap.put("body", bodyJsonObject.toString());
        }

        return Encrypt.desCode(postMap);
    }

    public abstract void build();

 }
