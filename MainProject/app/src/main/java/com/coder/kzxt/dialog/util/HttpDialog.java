package com.coder.kzxt.dialog.util;

import android.content.Context;
import com.coder.kzxt.activity.R;


/**
 * Created by MaShiZhao on 2017/1/16
 */
public class HttpDialog extends CustomDialog
{
    public HttpDialog(Context context)
    {
        this(context, R.layout.dlg_http);
    }

    public HttpDialog(Context context, int layout)
    {
        super(context, layout);
    }
}
