package com.coder.kzxt.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class StgImageView extends ImageView
{

	public int mWidth = 0;
	public int mHeight = 0;
	private int imageHeight = 0;

	public StgImageView(Context context) {
		super(context);
	}

	public StgImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int width = MeasureSpec.getSize(widthMeasureSpec);

		if (mWidth <= 0 || mHeight <= 0) {
			mWidth = mHeight = 1;
		}
		imageHeight = width * mHeight / mWidth;
		setMeasuredDimension(width, imageHeight);
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}

}
