package com.coder.kzxt.base.delegate;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.beans.TextImageBean;
import com.coder.kzxt.recyclerview.adapter.BaseDelegate;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.utils.GlideUtils;
import java.util.List;

/**
 * Created by MaShiZhao on 2017/5/9
 */

public class TextImageDelegate extends BaseDelegate<TextImageBean>
{
    private Context mContext;

    public TextImageDelegate(Context context)
    {
        super(R.layout.item_text_img);
        this.mContext = context;
    }

    @Override
    public void initCustomView(BaseViewHolder holder, List<TextImageBean> data, int position)
    {
        TextView text = holder.findViewById(R.id.text);
        ImageView img = holder.findViewById(R.id.img);
        TextImageBean contentBean = data.get(position);
            if (contentBean.getType() == 1)
            {
                img.setVisibility(View.GONE);
                text.setVisibility(View.VISIBLE);
                text.setText(contentBean.getContent());
            } else if (contentBean.getType() == 2)
            {
                img.setVisibility(View.VISIBLE);
                text.setVisibility(View.GONE);
                GlideUtils.loadQuestionsImg(img.getContext(), contentBean.getContent(), img);
            } else
            {
                img.setVisibility(View.GONE);
                text.setVisibility(View.GONE);
            }
    }
}
