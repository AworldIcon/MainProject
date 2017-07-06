package com.coder.kzxt.dialog.util;

import android.content.Context;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.recyclerview.adapter.BaseDelegate;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/1/10
 */
public class CustomListAdapter extends BaseRecyclerAdapter<BaseString>
{
    public CustomListAdapter(Context context, List<BaseString> data )
    {

        super(context, data, new CustomListDelegate());
        this.mContext = context;
        this.mData = data;
    }

    public static  class CustomListDelegate extends BaseDelegate<BaseString>
    {
        public CustomListDelegate()
        {
            super(R.layout.item_custom_list);
        }

        @Override
        public void initView(BaseViewHolder holder, List<BaseString> baseStrings, int position, int s)
        {
            TextView textView = holder.findViewById(R.id.textView);
            textView.setText(baseStrings.get(position).getMsg());

        }
    }
}
