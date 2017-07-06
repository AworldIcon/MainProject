package com.coder.kzxt.stuwork.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.app.http.HttpGetOld;
import com.app.http.InterfaceHttpResult;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.dialog.util.MyPublicDialog;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.Urls;

/**
 * Created by pc on 2017/2/27.
 * post请求发送收藏试题
 */

public class TestPaperFavorites {
    private Context context;
    private Dialog dialog;

    public TestPaperFavorites(Context context) {
        super();
        this.context = context;
    }
    public void comit_favourite(final String isFavorites, final TextView textView, String testId, String questionId, final Handler handler, String isCenter){
        dialog = MyPublicDialog.createLoadingDialog(context);
        dialog.show();
        if(isFavorites.equals("0")){
            HttpGetOld httpGet=new HttpGetOld(context, context, new InterfaceHttpResult() {
                @Override
                public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.cancel();
                    }
                    if(code==1000){
                        if (isFavorites.equals("0")) {
                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorites_select, 0, 0, 0);
                            Toast.makeText(context, "收藏成功", Toast.LENGTH_SHORT).show();
                            handler.sendEmptyMessage(Constants.FAVORITES);
                        }
                    }
                }
            },null, Urls.FAVOURITE_QUESTION,testId,questionId,isCenter);
            httpGet.excute();
        }else {
            HttpGetOld httpGet=new HttpGetOld(context, context, new InterfaceHttpResult() {
                @Override
                public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.cancel();
                    }
                    if(code==1000){
                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorites, 0, 0, 0);
                            Toast.makeText(context, "取消成功", Toast.LENGTH_SHORT).show();
                            handler.sendEmptyMessage(Constants.CANCE_FAVORITES);
                    }
                }
            },null, Urls.UN_FAVOURITE_QUESTION,testId,questionId,isCenter);
            httpGet.excute();
        }

    }
}
