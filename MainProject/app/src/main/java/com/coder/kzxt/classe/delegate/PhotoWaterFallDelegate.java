package com.coder.kzxt.classe.delegate;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.classe.beans.PhotoBeanResult;
import com.coder.kzxt.login.activity.LoginActivity;
import com.coder.kzxt.poster.activity.Show_Image_Activity;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.views.CustomNewDialog;
import com.coder.kzxt.views.StgImageView;
import java.util.List;

/**
 * Created by wangtinshun on 2017/3/17.
 */

public class PhotoWaterFallDelegate extends PullRefreshDelegate<PhotoBeanResult.PhotoBean> {

    private Context mContext;
    private Integer[] drawableResources;
    private SharedPreferencesUtil spu;
    private String identity;
    private String classId;

    public PhotoWaterFallDelegate(Context context,String classId, String role) {
        super(R.layout.item_classphoto_water);
        this.mContext = context;
        this.identity = role;
        this.classId = classId;
        spu = new SharedPreferencesUtil(context);
        drawableResources = new Integer[]{
                R.drawable.poster_template1,
                R.drawable.poster_template2,
                R.drawable.poster_template3,
                R.drawable.poster_template4,
                R.drawable.poster_template5,
                R.drawable.poster_template6,
                R.drawable.poster_template7,
                R.drawable.poster_template8,
                R.drawable.poster_template9
        };
    }


    @Override
    public void initCustomView(BaseViewHolder convertView, List<PhotoBeanResult.PhotoBean> data, final int position) {

        LinearLayout photoInfo = (LinearLayout) convertView.findViewById(R.id.class_photo_info);
        ImageView userFace = (ImageView) convertView.findViewById(R.id.userFace);
        TextView userName = (TextView) convertView.findViewById(R.id.userName);
        StgImageView stgImage = (StgImageView) convertView.findViewById(R.id.stgImage);
        TextView likeCount = (TextView) convertView.findViewById(R.id.like_count);
        LinearLayout likeLy = (LinearLayout) convertView.findViewById(R.id.likeLy);
        final ImageView likeImage = (ImageView) convertView.findViewById(R.id.likeImage);
        RelativeLayout photoBottom = convertView.findViewById(R.id.photo_bottom);

        final PhotoBeanResult.PhotoBean photoBean = data.get(position);

        GlideUtils.loadCircleHeaderOfCommon(mContext, photoBean.getUploadAvatar(), userFace);

        userName.setText(photoBean.getUploadName());
        likeCount.setText(photoBean.getGoodNum());
        if (photoBean.getIsGood().equals("1")) {
            likeImage.setImageResource(R.drawable.like_select);
        } else {
            likeImage.setImageResource(R.drawable.like_unselect);
        }

        if (photoBean.getPicWidth().equals("0") || photoBean.getPicHeight().equals("0")) {
            stgImage.mWidth = stgImage.mHeight = 1;
        } else {
            double a = Double.valueOf(photoBean.getPicHeight()) / Double.valueOf(photoBean.getPicWidth());
            if (a > 2) {
                stgImage.mWidth = 1;
                stgImage.mHeight = 2;
                stgImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else if (a < 0.3) {
                stgImage.mWidth = 2;
                stgImage.mHeight = 1;
                stgImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                stgImage.mWidth = Integer.valueOf(photoBean.getPicWidth());
                stgImage.mHeight = Integer.valueOf(photoBean.getPicHeight());
                stgImage.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) stgImage.getLayoutParams();
        lp.height = stgImage.mHeight;
        stgImage.setLayoutParams(lp);
        // notify 的时候防止 图片在此加载 注销showImageOnLoading
        GlideUtils.loadPoseters(mContext, photoBean.getPictureUrl(), drawableResources[position % 8], stgImage);

        //查看大图
        stgImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Show_Image_Activity.class);
                intent.putExtra("imgurl", photoBean.getPictureUrl());
                mContext.startActivity(intent);
            }
        });
        if (identity.equals("header") || identity.equals("admin") || identity.equals("owner")) {
            //长按删除
            stgImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longPressDelete(photoBean, position);
                    return true;
                }
            });
            likeLy.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longPressDelete(photoBean, position);
                    return true;
                }
            });
        } else {

        }

        if (photoBean.getSenderId().equals(String.valueOf(spu.getUid()))) {
            //长按删除
            stgImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longPressDelete(photoBean, position);
                    return true;
                }
            });
            likeLy.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longPressDelete(photoBean, position);
                    return true;
                }
            });
        } else {

        }

        likeLy.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (TextUtils.isEmpty(spu.getIsLogin())) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                    return;
                }
                if(onItemClickListener != null){
                    onItemClickListener.setLikePhoto(photoBean.getPhotoId());
                }
                int likeNum = Integer.valueOf(photoBean.getGoodNum());
                if (photoBean.getIsGood().equals("1")) {
                    likeImage.setImageResource(R.drawable.posters_like);
                    Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.img_scale_in);
                    likeImage.startAnimation(animation);
                    photoBean.setGoodNum((likeNum - 1) + "");
                    photoBean.setIsGood("0");
                } else {
                    likeImage.setImageResource(R.drawable.posters_like_down);
                    Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.img_scale_in);
                    likeImage.startAnimation(animation);
                    photoBean.setGoodNum((likeNum + 1) + "");
                    photoBean.setIsGood("1");
                }
                notifyDataSetChanged();
            }
        });
        //点击底部响应事件
        photoBottom.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                longPressDelete(photoBean, position);
                return true;
            }
        });
    }

    /**
     * 长按删除
     * @param photoBean
     * @param position
     */
    private void longPressDelete(final PhotoBeanResult.PhotoBean photoBean, final int position) {
        final CustomNewDialog listDialog = new CustomNewDialog(mContext);
        listDialog.setMessage(mContext.getResources().getString(R.string.dele_photo));
        listDialog.show();
        listDialog.setRightClick(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.deletePhoto(classId, photoBean.getPhotoId(), position);
                }
                if (listDialog.isShowing()) {
                    listDialog.cancel();
                }
            }
        });
    }

    private OnItemClickCallback onItemClickListener;

    public interface OnItemClickCallback{
        void deletePhoto(String classId,String photoId,int position); //长按删除照片
        void setLikePhoto(String photoId);
    }

    public void setOnItemClickListener(OnItemClickCallback listener){
        this.onItemClickListener = listener;
    }

}
