package com.coder.kzxt.course.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.recyclerview.adapter.BaseDelegate;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.utils.SearchHistoryUtils;

import java.util.List;

/**
 * Created by pc on 2017/3/29.
 */

public class SearchMoreCourseDelegate extends BaseDelegate<String>
{

    private Context context;
    private SearchHistoryUtils searchHistoryUtils;
    private TextView clear_history;
    public SearchMoreCourseDelegate(Context context, SearchHistoryUtils searchHistoryUtils, TextView clear_history)
    {
        super(R.layout.item_poseter_search_history);
        this.context = context;
        this.searchHistoryUtils=searchHistoryUtils;
        this.clear_history=clear_history;
    }

    @Override
    public void initCustomView(BaseViewHolder convertView, final List<String> data, final int position)
    {
        if(data.size()>0){
            clear_history.setVisibility(View.VISIBLE);
        }
        final TextView text = (TextView) convertView.findViewById(R.id.searchhistory_text);
        ImageView searchhistory_img = (ImageView) convertView.findViewById(R.id.searchhistory_img);


        text.setText(data.get(position));

        searchhistory_img.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                searchHistoryUtils.deleteHistoryItem(data.get(position));
                data.remove(position);
                notifyDataSetChanged();
                if(getAdapter().getItemCount()>0){
                    clear_history.setVisibility(View.VISIBLE);
                }else {
                    clear_history.setVisibility(View.GONE);
                }
            }
        });
    }
}
