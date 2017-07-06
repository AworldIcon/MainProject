package com.coder.kzxt.dialog.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import com.coder.kzxt.activity.R;


/**
 * 右边是确定 左边是取消 默认是两个按钮
 */

public class CustomDialog extends Dialog
{

    private TextView message;
    private TextView title;
    private View grayLine;
    private TextView rightTextView;
    private TextView leftTextView;
    private Context context;

    public CustomDialog(final Context context)
    {
        this(context, R.layout.dlg_custom_view, Gravity.CENTER);

        title = (TextView) this.findViewById(R.id.title);
        grayLine = (View) this.findViewById(R.id.grayLine);
        message = (TextView) this.findViewById(R.id.message);
        leftTextView = (TextView) this.findViewById(R.id.leftTextView);
        rightTextView = (TextView) this.findViewById(R.id.rightTextView);
        setLeftClick(null);
        setRightClick(null);
    }

    public CustomDialog(final Context context, final int layout)
    {
        this(context, layout, Gravity.CENTER);
    }

    public CustomDialog(final Context context, final int layout, final int theme)
    {
        this(context, layout, R.style.DialogBlack, theme);
    }

    public CustomDialog(final Context context, final int layout, final int theme, int gravity)
    {
        super(context, theme);
        this.setContentView(layout);
        final Window dialogWindow = this.getWindow();
        dialogWindow.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogWindow.setGravity(gravity);
        this.setCanceledOnTouchOutside(true);
        this.context = context;
    }

    // 只有默认布局才有下面的事件

    /**
     * 控制titleLy 是否显示 如果显示下面按钮有两个 一般用于选择 不显示 下面按钮就一个确定 一般用于提示
     */

    public void showOneButton()
    {
        title.setVisibility(View.GONE);
        grayLine.setVisibility(View.GONE);
        leftTextView.setVisibility(View.GONE);
    }

    /**
     * 设置主要内容
     */
    public void setMessage(String messageString)
    {
        if (this.message != null && messageString != null)
        {
            message.setText(messageString);
        }
    }

    /**
     * 设置标题 不设置默认温馨提示
     *
     * @param string
     */
    public void setTitleText(String string)
    {
        if (this.title != null && string != null)
        {
            title.setText(string);
        }
    }

    /**
     * 显示Title
     *
     * @param string
     */
    public void setTitleVisibility(String string)
    {
        if (this.title != null && string != null)
        {
            title.setVisibility(View.VISIBLE);
            title.setText(string);
        }
    }

    /**
     * 设置右边按钮的内容 默认确定
     *
     * @param string
     */
    public void setRightText(String string)
    {
        if (this.rightTextView != null && string != null)
        {
            rightTextView.setText(string);
        }
    }

    /**
     * 设置右边按钮的颜色
     */
    public void setRightTextColor(int color)
    {
        if (this.rightTextView != null && color != 0)
        {
            rightTextView.setTextColor(color);
        }
    }


    /**
     * 设置左边按钮的内容 默认取消
     *
     * @param string
     */
    public void setLeftText(String string)
    {
        if (this.leftTextView != null && string != null)
        {
            leftTextView.setText(string);
        }
    }

    /**
     * 设置左边边按钮的颜色
     */
    public void setLeftTextColor(int color)
    {
        if (this.title != null && color != 0)
        {
            title.setTextColor(context.getResources().getColor(color));
        }
    }

    /**
     * 设置左边 取消按钮 的响应事件 默认取消对话框
     */
    public void setLeftClick(View.OnClickListener click)
    {
        if (click == null)
        {
            leftTextView.setOnClickListener(new View.OnClickListener()
            {

                public void onClick(View v)
                {
                    CustomDialog.this.dismiss();
                }
            });
            return;
        }
        if (this.leftTextView != null)
        {
            leftTextView.setOnClickListener(click);
        }
    }

    /**
     * 设置右边 确定按钮 的响应事件 默认取消对话框
     */
    public void setRightClick(View.OnClickListener click)
    {
        if (click == null)
        {
            rightTextView.setOnClickListener(new View.OnClickListener()
            {

                public void onClick(View v)
                {
                    CustomDialog.this.dismiss();
                }
            });
            return;
        }
        if (this.rightTextView != null)
        {
            rightTextView.setOnClickListener(click);
        }
    }


}
