/*
* Copyright (C) 2013 Peng fei Pan <sky@xiaopan.me>
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.coder.kzxt.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 线性自动换行布局
 * Created by wangtingshun on 2017/2/21.
 */
public class LinearLineWrapLayout extends ViewGroup {

    private static final int PADDING_HOR = 16;//水平方向padding
    private static final int PADDING_VERTICAL = 10;//垂直方向padding
    private static final int SIDE_MARGIN = 15;//左右间距
    private static final int TEXT_MARGIN = 30;

    public LinearLineWrapLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinearLineWrapLayout(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int autualWidth = r - l;
        int x = SIDE_MARGIN;// 横坐标开始
        int y = 0;//纵坐标开始
        int rows = 1;
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            int width = view.getMeasuredWidth();
            int height = view.getMeasuredHeight();
            x += width + TEXT_MARGIN;
            if (x > autualWidth) {
                x = width + SIDE_MARGIN;
                rows++;
            }
            y = rows * (height + TEXT_MARGIN);
            if (i == 0) {
                view.layout(x - width - TEXT_MARGIN, y - height, x - TEXT_MARGIN, y);
            } else {
                view.layout(x - width, y - height, x, y);
            }
//            onClickEventListener.OnClickEvent(view);
        }
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int x = 0;//横坐标
        int y = 0;//纵坐标
        int rows = 1;//总行数
        int specWidth = MeasureSpec.getSize(widthMeasureSpec);
        int actualWidth = specWidth - SIDE_MARGIN * 2;//实际宽度
        int childCount = getChildCount();
        for (int index = 0; index < childCount; index++) {
            View child = getChildAt(index);
            child.setPadding(PADDING_HOR, PADDING_VERTICAL, PADDING_HOR, PADDING_VERTICAL);
            child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            x += width + TEXT_MARGIN;
            if (x > actualWidth) {//换行
                x = width;
                rows++;
            }
            y = rows * (height + TEXT_MARGIN);
        }
        setMeasuredDimension(actualWidth, y);
    }

    private OnClickEventListener onClickEventListener;

    public void setOnClickEventListener(OnClickEventListener eventListener) {
        this.onClickEventListener = eventListener;
    }

    public interface OnClickEventListener {
        void OnClickEvent(View view);
    }

}
