package com.coder.kzxt.dialog.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.coder.kzxt.activity.R;

/**
 * Created by pc on 2017/2/27.
 */

public class MyPublicDialog extends ProgressDialog {

    public MyPublicDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public static Dialog createLoadingDialog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.loading_layout);// 加载布局
        Dialog loadingDialog = new Dialog(context,R.style.loading_dialog);// 创建自定义样式dialog
        //Dialog loadingDialog = new Dialog(context);// 创建自定义样式dialog
        // loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setCanceledOnTouchOutside(false); //可以用“返回键”取消
        loadingDialog.getWindow().setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));// 设置布局
        loadingDialog.getWindow().setGravity(Gravity.CENTER);
        return loadingDialog;
    }
}
