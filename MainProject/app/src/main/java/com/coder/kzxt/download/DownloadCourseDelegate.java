package com.coder.kzxt.download;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.recyclerview.adapter.BaseDelegate;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.utils.GlideUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/3/21.
 */

public class DownloadCourseDelegate extends BaseDelegate {

    private Context context;
    private ArrayList<HashMap<String,String>> list;


    public DownloadCourseDelegate(Context context,ArrayList<HashMap<String,String>> list) {
        super(R.layout.download_centre_item);
        this.list = list;
        this.context =context;
    }

    @Override
    public void initCustomView(BaseViewHolder holder, List data, int position) {
        super.initCustomView(holder, data, position);

        ImageView download_centre_course_img = (ImageView) holder.findViewById(R.id.download_centre_course_img);
        ImageView download_centre_img = (ImageView) holder.findViewById(R.id.download_centre_img);
        ImageView download_centre_red_img = (ImageView) holder.findViewById(R.id.download_centre_red_img);
        TextView download_centre_course_name = (TextView) holder.findViewById(R.id.download_centre_course_name);
        TextView download_centre_number_text = (TextView) holder.findViewById(R.id.download_centre_number_text);
        TextView download_centre_mb_text = (TextView) holder.findViewById(R.id.download_centre_mb_text);
        HashMap<String,String> hashMap = (HashMap<String, String>) data.get(position);

        final String treeid = hashMap.get("treeid");
        final String treename = hashMap.get("treename");
        final String treepicture = hashMap.get("treepicture");
        String coursenumber = hashMap.get("coursenumber");
        String allFileSize = hashMap.get("allFileSize");
        String centre_red = hashMap.get("centre_red");


        GlideUtils.loadCourseImg(context,treepicture,download_centre_course_img);
        if (!TextUtils.isEmpty(treename)) {
            if (treename.length() > 27) {
                download_centre_course_name.setText(treename.substring(0, 26) + "...");
            } else {
                download_centre_course_name.setText(treename);
            }
        }

        if (centre_red.equals("1")) {
            download_centre_red_img.setVisibility(View.VISIBLE);
            download_centre_number_text.setTextColor(context.getResources().getColor(R.color.first_theme));
        } else {
            download_centre_red_img.setVisibility(View.GONE);
            download_centre_number_text.setTextColor(context.getResources().getColor(R.color.font_gray));
        }

        download_centre_number_text.setText(coursenumber);
        download_centre_mb_text.setText(allFileSize);


    }
}
