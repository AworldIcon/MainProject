package com.coder.kzxt.sign.adapter;

import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.recyclerview.adapter.BaseDelegate;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.sign.beans.SignInfoResult;
import com.coder.kzxt.sign.beans.SignTagBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by MaShiZhao on 2017/5/24
 */

public class MarkStudentDelegate extends BaseDelegate<SignTagBean>
{
    private HashMap<String, String> ids;

    public MarkStudentDelegate(SignInfoResult.SignInfoBean signBean)
    {
        super(R.layout.item_sign_tag);
        ids = new HashMap<>();
        for (SignTagBean signTagBean : signBean.getRecord().getTags())
        {
            ids.put(signTagBean.getId(), signTagBean.getName());
        }
    }

    @Override
    public void initCustomView(BaseViewHolder holder, List<SignTagBean> data, int position)
    {

        CheckBox checkBox = holder.findViewById(R.id.checkBox);

        final SignTagBean signTagBean = data.get(position);
        signTagBean.setSelect(ids.containsKey(data.get(position).getId()));
        checkBox.setText(signTagBean.getName());
        if (signTagBean.getSelect())
        {
            checkBox.setChecked(true);
        } else
        {
            checkBox.setChecked(false);
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                signTagBean.setSelect(isChecked);
            }
        });

    }


}
