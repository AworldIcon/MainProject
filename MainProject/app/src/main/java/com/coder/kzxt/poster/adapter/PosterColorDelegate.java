package com.coder.kzxt.poster.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.poster.beans.TextBgColor;
import com.coder.kzxt.recyclerview.adapter.BaseDelegate;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/3/11
 */

public class PosterColorDelegate extends BaseDelegate<TextBgColor.ColorBean> {
    public PosterColorDelegate() {
        super(R.layout.item_poster_color);
    }

    @Override
    public void initCustomView(BaseViewHolder mViewHolder, List<TextBgColor.ColorBean> data, int position) {

        ImageView selectImage = (ImageView) mViewHolder.findViewById(R.id.selectImage);
        TextView back = (TextView) mViewHolder.findViewById(R.id.back);
        TextBgColor.ColorBean hashMap = data.get(position);
        final String color = hashMap.getColor();
        back.setBackgroundColor(Color.parseColor(color));

        if (getAdapter().getSelectPosition() == position) {
            selectImage.setVisibility(View.VISIBLE);
        } else {
            selectImage.setVisibility(View.GONE);
        }
    }
}
