package com.coder.kzxt.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.coder.kzxt.activity.R;
import java.util.ArrayList;


/**
 * 搜索历史记录适配器
 * Created by wangtingshun on 2017/3/13.
 */

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.MyViewHolder> {


    private Context mContext;
    private ArrayList<String> historyData;

    public SearchHistoryAdapter(Context context, ArrayList<String> data) {
        this.mContext = context;
        this.historyData = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(mContext).inflate(R.layout.item_searchhistory, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final String historyItem = historyData.get(position);
        holder.searchHistoryText.setText(historyItem);
        holder.searchHistoryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null){
                    onClickListener.onSearchHistoryItemClick(historyItem);
                }
            }
        });
        holder.searchHistoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null){
                    onClickListener.onDeleteHistoryItemClick(historyItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyData.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView searchHistoryText;
        private ImageView searchHistoryImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            searchHistoryText = (TextView) itemView.findViewById(R.id.searchhistory_text);
            searchHistoryImage = (ImageView) itemView.findViewById(R.id.searchhistory_img);
        }
    }

    private OnItemClickInterface onClickListener;

    public interface OnItemClickInterface{
        void onSearchHistoryItemClick(String historyItem ); //点击搜索历史记录的item
        void onDeleteHistoryItemClick(String historyItem);  //点击删除历史记录的item
    }

    public void setOnItemClickListener(OnItemClickInterface listener){
        this.onClickListener = listener;
    }
}
