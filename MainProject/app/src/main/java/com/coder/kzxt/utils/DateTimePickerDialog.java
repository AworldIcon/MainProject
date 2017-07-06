package com.coder.kzxt.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.coder.kzxt.activity.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaShiZhao on 2017/6/20
 */

public class DateTimePickerDialog
{

    private Context mContext;
    private PopupWindow pop;
    private View view;
    private RelativeLayout activity_publish_work_time;
    private TimePicker timePicker;
    private DatePicker datePicker;
    private TextView confirm, cancle;
    private DatePickerClickListener datePickerClickListener;
    private TimePickerClickListener timePickerClickListener;


    public DateTimePickerDialog(Context context)
    {
        this.mContext = context;

        initView();
        initClick();
        initPopupWindow();
    }

    /**
     * 初始化界面里面的布局
     */
    private void initView()
    {
        view = LayoutInflater.from(mContext).inflate(R.layout.time_picker_pop, null);
        activity_publish_work_time = (RelativeLayout) view.findViewById(R.id.activity_publish_work_time);
        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        confirm = (TextView) view.findViewById(R.id.confirm);
        cancle = (TextView) view.findViewById(R.id.cancel);
        timePicker.setIs24HourView(true);
        resizePikcer(timePicker);
        resizePikcer(datePicker);
    }

    /**
     * 初始化pop
     */
    private void initPopupWindow()
    {
        pop = new PopupWindow(mContext);
        pop.setContentView(view);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.color.trans_half));
        pop.setFocusable(true);
    }

    /**
     * 调整FrameLayout大小
     *
     * @param tp
     */
    private void resizePikcer(FrameLayout tp)
    {
        List<NumberPicker> npList = findNumberPicker(tp);
        for (NumberPicker np : npList)
        {
            resizeNumberPicker(np);
        }
    }


    /**
     * 得到viewGroup里面的numberpicker组件
     *
     * @param viewGroup
     * @return
     */
    private List<NumberPicker> findNumberPicker(ViewGroup viewGroup)
    {
        List<NumberPicker> npList = new ArrayList<NumberPicker>();
        View child = null;
        if (null != viewGroup)
        {
            for (int i = 0; i < viewGroup.getChildCount(); i++)
            {
                child = viewGroup.getChildAt(i);
                if (child instanceof NumberPicker)
                {
                    npList.add((NumberPicker) child);
                } else if (child instanceof LinearLayout)
                {
                    List<NumberPicker> result = findNumberPicker((ViewGroup) child);
                    if (result.size() > 0)
                    {
                        return result;
                    }
                }
            }
        }
        return npList;
    }

    /*
     * 调整numberpicker大小
     */
    private void resizeNumberPicker(NumberPicker np)
    {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(30, 0, 30, 0);
        np.setLayoutParams(params);
    }

    private void initClick()
    {
        cancle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (datePickerClickListener != null && datePicker != null)
                {
                    datePickerClickListener.onDateClick(datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth());
                }
                if (timePickerClickListener != null && timePicker != null)
                {
                    timePickerClickListener.onTimeClick(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                }
                dismiss();
            }
        });
    }


    /**
     * 隐藏时分 只显示年月日
     */
    public void setTimeGone()
    {
        if (timePicker != null)
            timePicker.setVisibility(View.GONE);
    }


    /**
     * 可设置dateicker的初始值
     */
    public void updateDate(int year, int month, int day)
    {
        if (datePicker != null)
            datePicker.updateDate(year, month, day);
    }

    /**
     * 显示pop
     */
    public void show()
    {
        if (pop != null && view != null)
        {
            view.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_bottom_in_2));
            pop.showAtLocation(activity_publish_work_time, Gravity.BOTTOM, 0, 0);
        }
    }

    /**
     * 隐藏pop
     */
    public void dismiss()
    {
        if (pop != null)
        {
            pop.dismiss();
        }
    }

    public void setDatePickerClickListener(DatePickerClickListener datePickerClickListener)
    {
        this.datePickerClickListener = datePickerClickListener;
    }

    public void setTimePickerClickListener(TimePickerClickListener timePickerClickListener)
    {
        this.timePickerClickListener = timePickerClickListener;
    }


    /**
     * 年月日的回调
     */
    public interface DatePickerClickListener
    {
        public void onDateClick(int year, int month, int day);

    }


    /**
     * 时分的回调
     */
    public interface TimePickerClickListener
    {
        public void onTimeClick(int hour, int minute);
    }

}
