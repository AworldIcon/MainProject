package com.coder.kzxt.gambit.activity.adapter;

import android.content.Context;

import com.coder.kzxt.gambit.activity.bean.Gambit;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;

import java.util.HashMap;
import java.util.List;

/**
 * Created by pc on 2017/3/14.
 */

public class GambitPraAdapter extends PullRefreshAdapter<HashMap<String, String>>
{
public static final int PROMOTION_IMAGE_ONE = 1;
public static final int PROMOTION_IMAGE_OTHER = 2;

public GambitPraAdapter(Context context, List<HashMap<String, String>> data, GambitPraDelegate orderListDelegate)
        {
        super(context, data, orderListDelegate);
        }


}
