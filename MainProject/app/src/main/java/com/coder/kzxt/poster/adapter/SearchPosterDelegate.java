package com.coder.kzxt.poster.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.poster.activity.SearchPosterActivity;
import com.coder.kzxt.recyclerview.adapter.BaseDelegate;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/3/10
 */

public class SearchPosterDelegate extends BaseDelegate<String>
{

    private Context context;
    public SearchPosterDelegate(Context context)
    {
        super(R.layout.item_poseter_search_history);
        this.context = context;
    }

    @Override
    public void initCustomView(BaseViewHolder convertView, final List<String> data, final int position)
    {
        final TextView text = (TextView) convertView.findViewById(R.id.searchhistory_text);
        ImageView searchhistory_img = (ImageView) convertView.findViewById(R.id.searchhistory_img);


        text.setText(data.get(position));

        searchhistory_img.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((SearchPosterActivity) context).removeItem(data.get(position));
                data.remove(position);
                notifyDataSetChanged();
            }
        });
    }
}
