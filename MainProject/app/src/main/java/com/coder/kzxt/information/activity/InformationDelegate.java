package com.coder.kzxt.information.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.image.ImageLoad;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.main.beans.InformationResult;
import com.coder.kzxt.main.beans.MainModelBean;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.DateUtil;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.webview.activity.TextView_Link_Activity;

import java.util.List;

/**
 * Created by pc on 2017/3/3.
 *
 */

public class InformationDelegate extends PullRefreshDelegate<InformationResult.ItemsBean>
{

    private Context mContext;

    public InformationDelegate(Context context)
    {
        super(R.layout.item_information_style);
        this.mContext = context;
    }

    public Bitmap stringtoBitmap(String string){
        //将字符串转换成Bitmap类型
        Bitmap bitmap=null;
        try {
            byte[]bitmapArray;
            bitmapArray= Base64.decode(string, Base64.DEFAULT);
            bitmap= BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }
    @Override
    public int getCommonLayoutId(int viewType)
    {
        if (viewType == MyInformationAdapter.PROMOTION_IMAGE_ONE)
        {
            return R.layout.item_information_style;
        } else
        {
            return R.layout.item_information_style2;
        }
    }


    @Override
    public void initCustomView(BaseViewHolder holder, final List<InformationResult.ItemsBean> data, final int position)
    {
        final InformationResult.ItemsBean bean = data.get(position);
        ImageView imageView1 = null, imageView2 = null;
        RelativeLayout imageRy = null;
        if (bean.getImgs().size() == 1||bean.getImgs().size() == 2) {

        }else {
            imageRy = (RelativeLayout) holder.findViewById(R.id.imageRy);
            imageView1 = (ImageView) holder.findViewById(R.id.image1);
            imageView2 = (ImageView) holder.findViewById(R.id.image2);
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
if(position==0){
   // Log.d("zw--00",bean.getImgs().get(0)+"+++");
}
        if(bean.getImgs().size()>0){
            final ImageView imageView0 = holder.findViewById(R.id.image0);
            if(bean.getImgs().get(0).contains("base64")){
                imageView0.setImageBitmap(stringtoBitmap(bean.getImgs().get(0).split("base64,")[1].trim()));
            }else {
                GlideUtils.loadCourseImg(mContext,bean.getImgs().get(0),imageView0);
            }
        }


        TextView title = holder.findViewById(R.id.title);
        TextView comeFrom = holder.findViewById(R.id.comeFrom);
        TextView time = holder.findViewById(R.id.time);
        title.setText(bean.getTitle().replaceAll("&quot;","”"));
        comeFrom.setText(mContext.getResources().getString(R.string.come_from) + bean.getSource());

        time.setText(DateUtil.getDateStrDot(Long.valueOf(bean.getTime())));

        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TextView_Link_Activity.class);
                intent.putExtra("web_url", bean.getSourceUrl());
                intent.putExtra("title", bean.getTitle().replaceAll("&quot;","”"));
                mContext.startActivity(intent);
//                EToast.makeText(mContext,"资讯"+position, Toast.LENGTH_SHORT).show();

            }
        });

    }

}
