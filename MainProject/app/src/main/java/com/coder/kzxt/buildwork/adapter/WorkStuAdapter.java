package com.coder.kzxt.buildwork.adapter;

import android.content.Context;
import com.coder.kzxt.buildwork.entity.FinishWorkNum;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import java.util.List;

/**
 * Created by pc on 2017/3/28.
 */

public class WorkStuAdapter extends PullRefreshAdapter<FinishWorkNum.ItemsBean> {
    public WorkStuAdapter(Context context, List<FinishWorkNum.ItemsBean> data, WorkStuDelegate orderListDelegate) {
        super(context, data, orderListDelegate);
    }


}
