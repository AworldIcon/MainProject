package com.coder.kzxt.sign.adapter;

import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.recyclerview.adapter.BaseDelegate;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.sign.beans.SignTagBean;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/6/2
 */

public class TagDelegate extends BaseDelegate<SignTagBean>
{

    public TagDelegate()
    {
        super(R.layout.item_sign_mark);
    }

    @Override
    public void initCustomView(BaseViewHolder holder, List<SignTagBean> data, int position)
    {
        TextView textView = holder.findViewById(R.id.textView);
        textView.setText(data.get(position).getName());
    }
}