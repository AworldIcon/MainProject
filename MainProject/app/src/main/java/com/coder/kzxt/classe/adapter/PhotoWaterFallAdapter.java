package com.coder.kzxt.classe.adapter;

import android.content.Context;

import com.coder.kzxt.classe.beans.PhotoBeanResult;
import com.coder.kzxt.classe.delegate.PhotoWaterFallDelegate;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import java.util.List;

/**
 * 相册瀑布流适配器
 * Created by wangtingshun on 2017/3/17.
 */

public class PhotoWaterFallAdapter extends PullRefreshAdapter<PhotoBeanResult.PhotoBean> {


    public PhotoWaterFallAdapter(Context context, List<PhotoBeanResult.PhotoBean> photos, PhotoWaterFallDelegate delegate) {
        super(context, photos, delegate);
    }

}
