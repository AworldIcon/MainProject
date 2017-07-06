package com.coder.kzxt.utils;

import android.content.Context;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by MaShiZhao on 2017/3/24
 */

public class SearchHistoryUtils
{
    private SharedPreferencesUtil sharedPreferencesUtil;
    private String preferencesKey;
    private String dataHistoryString;

    public SearchHistoryUtils(Context context, String preferencesKey)
    {
        this.sharedPreferencesUtil = new SharedPreferencesUtil(context);
        this.preferencesKey = preferencesKey;
        this.dataHistoryString = sharedPreferencesUtil.getSearchHistory(preferencesKey);
    }


    public List<String> getHistory()
    {
        return Arrays.asList(dataHistoryString.split(","));
    }


    public void clearHistory()
    {
        sharedPreferencesUtil.clearSearchHistory(preferencesKey);
        this.dataHistoryString = sharedPreferencesUtil.getSearchHistory(preferencesKey);

    }

    public List<String> resetHistory(String searchString)
    {

        if (TextUtils.isEmpty(searchString))
            return getHistory();

        if (this.dataHistoryString.contains(searchString))
        {
            dataHistoryString = dataHistoryString.replace(searchString + ",", "");
        }
        dataHistoryString = searchString + "," + dataHistoryString;
        sharedPreferencesUtil.setSearchHistory(this.preferencesKey, dataHistoryString);

        return getHistory();
    }


    public List<String> deleteHistoryItem(String searchString)
    {

        if (TextUtils.isEmpty(searchString))
            return getHistory();

        if (this.dataHistoryString.contains(searchString))
        {
            dataHistoryString = dataHistoryString.replace(searchString + ",", "");
        }
        sharedPreferencesUtil.setSearchHistory(this.preferencesKey, dataHistoryString);

        return getHistory();
    }
}
