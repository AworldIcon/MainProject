package com.coder.kzxt.sign.adapter;

import android.content.Context;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.recyclerview.adapter.BaseDelegate;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/5/22
 */

public class MapLocationDelegate extends BaseDelegate<PoiItem>
{
    public MapLocationDelegate(Context context)
    {
        super(R.layout.item_map_location);
    }

    @Override
    public void initCustomView(BaseViewHolder holder, List<PoiItem> data, int position)
    {
        TextView adName = (TextView) holder.findViewById(R.id.adName);
        TextView snippet = (TextView) holder.findViewById(R.id.snippet);

        PoiItem bean = data.get(position);
        adName.setText(bean.getTitle());
        snippet.setText(bean.getSnippet());
    }
}
