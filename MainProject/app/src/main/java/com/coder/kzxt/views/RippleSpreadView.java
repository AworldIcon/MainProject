package com.coder.kzxt.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 支付宝咻一咻功能
 * Created by MaShiZhao on 2017/5/22
 */

public class RippleSpreadView extends View
{


    private Paint paint;
    private int maxWidth;
    private int iasd;
    // 是否运行
    private boolean isStarting = false;
    private List<String> alphaList = new ArrayList<String>();
    private List<String> startWidthList = new ArrayList<String>();
//	private int drawable;
//

    public RippleSpreadView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public RippleSpreadView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    public RippleSpreadView(Context context)
    {
        super(context);
        init(context);
    }

    private void init(Context context)
    {
        paint = new Paint();
        // 设置博文的颜色
        paint.setColor(0x0002CCFA);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(2);
        alphaList.add("255");// 圆心的不透明度
        startWidthList.add("0");

        double screenSizeOfDevice = DensityUtil.getScreenSizeOfDevice((BaseActivity) context);
        if (screenSizeOfDevice > 4 && screenSizeOfDevice < 5)
        {
            setMaxWidth(510);
            setIasd(1);
        } else if (screenSizeOfDevice > 5 && screenSizeOfDevice < 6)
        {
            setMaxWidth(1100);
            setIasd(2);
        } else if (screenSizeOfDevice > 6)
        {
            setMaxWidth(1100);
            setIasd(2);
        } else if (screenSizeOfDevice < 4)
        {
            setMaxWidth(510);
            setIasd(1);
        }
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        setBackgroundColor(Color.TRANSPARENT);// 颜色：完全透明
        // 依次绘制 同心圆
        for (int i = 0; i < alphaList.size(); i++)
        {
            int alpha = (int) Double.parseDouble(alphaList.get(i));
            // 圆半径
            int startWidth = Integer.parseInt(startWidthList.get(i));
            paint.setAlpha(alpha);
            // 这个半径决定你想要多大的扩散面积
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, startWidth + 80, paint);
            // 同心圆扩散
            if (isStarting && alpha > 0 && startWidth < getMaxWidth())
            {
                alphaList.set(i, (alpha - 0.5) + "");
                startWidthList.set(i, (startWidth + getIasd()) + "");
            }

        }
        if (isStarting && Integer.parseInt(startWidthList.get(startWidthList.size() - 1)) == getMaxWidth() / 10)
        {
            alphaList.add("255");
            startWidthList.add("0");
        }

        // 同心圆数量达到10个，删除最外层圆
        if (isStarting && startWidthList.size() == 10)
        {
            startWidthList.remove(0);
            alphaList.remove(0);
        }
        // 刷新界面
        invalidate();
    }

    public int getMaxWidth()
    {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth)
    {
        this.maxWidth = maxWidth;
    }

    public int getIasd()
    {
        return iasd;
    }

    public void setIasd(int iasd)
    {
        this.iasd = iasd;
    }

    // 执行动画
    public void start()
    {
        isStarting = true;
    }

    // 停止动画
    public void stop()
    {
        isStarting = false;
    }

    // 判断是都在不在执行
    public boolean isStarting()
    {
        return isStarting;
    }
}
