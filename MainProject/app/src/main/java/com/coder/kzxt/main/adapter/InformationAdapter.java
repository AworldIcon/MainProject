package com.coder.kzxt.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.app.utils.L;
import com.coder.kzxt.main.beans.MainModelBean;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.utils.DateUtil;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.webview.activity.TextView_Link_Activity;
import java.util.List;

/**
 * 资讯
 */

public class InformationAdapter extends HolderBaseAdapter<MainModelBean.ItemsBean.ListBean> {

    private Context mContext;
    public InformationAdapter(Context mContext, List<MainModelBean.ItemsBean.ListBean> CourseLiveBeanList) {
        super();
        this.mContext = mContext;
        this.data = CourseLiveBeanList;
    }

    public Bitmap stringtoBitmap(String string){
        //将字符串转换成Bitmap类型
        Bitmap bitmap=null;
        try {
            byte[]bitmapArray;
            bitmapArray=Base64.decode(string, Base64.DEFAULT);
            bitmap= BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }
    @Override
    public ViewHolder getViewHolder(View convertView, ViewGroup parent, final int position) {
        final MainModelBean.ItemsBean.ListBean bean = data.get(position);
        ViewHolder mViewHolder = null;
        ImageView imageView1 = null, imageView2 = null;
        RelativeLayout imageRy = null;
        L.d(bean.getTitle()+" information adapter "+bean.getImgs().toString());
        if (bean.getImgs().size() == 1||bean.getImgs().size() == 2) {
            mViewHolder = ViewHolder.get(mContext, null, parent, R.layout.item_information_style);

        } else {
            mViewHolder = ViewHolder.get(mContext, null, parent, R.layout.item_information_style2);
            imageRy = (RelativeLayout) mViewHolder.findViewById(R.id.imageRy);
            imageView1 = (ImageView) mViewHolder.findViewById(R.id.image1);
            imageView2 = (ImageView) mViewHolder.findViewById(R.id.image2);
            if (bean.getImgs().size() == 0) {
                // why  null 指针
                imageRy.setVisibility(View.GONE);
            } else {
                imageRy.setVisibility(View.VISIBLE);
                imageView1.setVisibility(View.VISIBLE);
                imageView2.setVisibility(View.VISIBLE);
                if(bean.getImgs().get(1).contains("base64")){
                    imageView1.setImageBitmap(stringtoBitmap(bean.getImgs().get(1).split("base64,")[1].trim()));
                }else {
                    GlideUtils.loadCourseImg(mContext,bean.getImgs().get(1),imageView1);
                }
                if(bean.getImgs().get(2).contains("base64")){
                    imageView2.setImageBitmap(stringtoBitmap(bean.getImgs().get(2).split("base64,")[1].trim()));
                }else {
                    GlideUtils.loadCourseImg(mContext,bean.getImgs().get(2),imageView2);
                }
            }

        }

        if (bean.getImgs().size() >0) {
            final ImageView imageView0 = mViewHolder.findViewById(R.id.image0);
            if(bean.getImgs().get(0).contains("base64")){
                imageView0.setImageBitmap(stringtoBitmap(bean.getImgs().get(0).split("base64,")[1].trim()));
            }else {
                GlideUtils.loadCourseImg(mContext,bean.getImgs().get(0),imageView0);
            }
        }
        TextView title = mViewHolder.findViewById(R.id.title);
        TextView comeFrom = mViewHolder.findViewById(R.id.comeFrom);
        TextView time = mViewHolder.findViewById(R.id.time);
        title.setText(bean.getTitle().replaceAll("&quot;","”"));
        comeFrom.setText(mContext.getResources().getString(R.string.come_from) + bean.getSource());

        time.setText(DateUtil.getDateStrDot(Long.valueOf(bean.getTime())));

        mViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TextView_Link_Activity.class);
                intent.putExtra("web_url", bean.getSourceUrl());
                intent.putExtra("title", bean.getTitle().replaceAll("&quot;","”"));
                mContext.startActivity(intent);
//                EToast.makeText(mContext,"资讯"+position, Toast.LENGTH_SHORT).show();

            }
        });

        return mViewHolder;
    }


}
