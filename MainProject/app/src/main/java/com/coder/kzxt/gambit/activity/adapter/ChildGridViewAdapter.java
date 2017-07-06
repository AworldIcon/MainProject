package com.coder.kzxt.gambit.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.gambit.activity.ClassGambitParticularsActivity;
import com.coder.kzxt.gambit.activity.ViewPagePicsNetAct;
import com.coder.kzxt.utils.GlideUtils;

import java.util.ArrayList;

/**
 * Created by pc on 2017/3/14.
 */

public class ChildGridViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> list;

    public  ChildGridViewAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.gambit_gridview_item, null);
        }

        ImageView post_iv = (ImageView) convertView.findViewById(R.id.post_iv);
        String img = list.get(position);

        //imageLoader.displayImage(img, post_iv, ImageLoaderOptions.posterTemplate9);
        GlideUtils.loadPorstersImg(parent.getContext(),img,post_iv);


        post_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewPagePicsNetAct.class);
                intent.putStringArrayListExtra("imgurl", list);
                intent.putExtra("index", position);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
