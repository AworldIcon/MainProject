package com.coder.kzxt.poster.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.poster.activity.PosterActivity;
import com.coder.kzxt.poster.beans.PosterCategory;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.GlideUtils;

import java.util.List;


public class PosterTypeDelegate extends PullRefreshDelegate<PosterCategory.ItemsBean> {
    private Context mContext;

    public PosterTypeDelegate(Context context) {
        super(R.layout.item_poster_type);
        this.mContext = context;
    }

    @Override
    public void initCustomView(BaseViewHolder mViewHolder, final List<PosterCategory.ItemsBean> data, int position) {
        final PosterCategory.ItemsBean bean = data.get(position);
        TextView text = (TextView) mViewHolder.findViewById(R.id.text);
        ImageView image = (ImageView) mViewHolder.findViewById(R.id.image);
        text.setText(bean.getName());

        if(text.getText().toString().equals("全部")){
            image.setImageResource(R.drawable.poster_cata_all);
        }else {
            GlideUtils.loadPorstersType(mContext, bean.getLogo(), image);
        }


        mViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((PosterActivity) mContext).setPoseterType(bean.getName(), bean.getId());
            }
        });

    }
}
