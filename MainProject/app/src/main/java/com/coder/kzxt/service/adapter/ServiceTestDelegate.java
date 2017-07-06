package com.coder.kzxt.service.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.service.beans.ServiceFilesResult;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/6/6
 */

public class ServiceTestDelegate extends PullRefreshDelegate<ServiceFilesResult.Item>
{
    private Context mContext;

    public ServiceTestDelegate(Context context)
    {
        super(R.layout.item_service_test);
        this.mContext = context;
    }

    @Override
    public void initCustomView(BaseViewHolder holder, List<ServiceFilesResult.Item> data, int position)
    {
        ImageView testImage = holder.findViewById(R.id.testImage);
        TextView testName = holder.findViewById(R.id.testName);
        TextView testSize = holder.findViewById(R.id.testSize);

        ServiceFilesResult.Item item = data.get(position);
        testName.setText(item.getTitle());
        testSize.setText(item.getSize());
        if (item.getExt().equals("zip"))
            testImage.setImageResource(R.drawable.default_zip);
        else
            testImage.setImageResource(R.drawable.default_word);

    }
}
