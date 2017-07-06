package com.coder.kzxt.classe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.classe.activity.ShowImageActivity;
import com.coder.kzxt.utils.GlideUtils;

import java.util.List;

/**
 * Created by wangtingshun on 2017/6/23.
 * 回复图片的adapter
 */
public class ReplyImageAdapter extends BaseAdapter {

    private List<String> imageList;
    private Context mContext;

    public ReplyImageAdapter(Context context, List<String> imageList) {
        this.mContext = context;
        this.imageList = imageList;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_topic_gridview, null);
        final ImageView imageView = (ImageView) view.findViewById(R.id.topic_reply_image);
        final String imageUrl = imageList.get(position);
        GlideUtils.loadCourseImg(mContext, imageUrl, imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowImageActivity.gotoActivity(mContext, imageUrl);
            }
        });
        return view;
    }


}
