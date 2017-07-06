package com.coder.kzxt.views;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 *此布局检测软件盘是否关闭
 */
public class ResizeLayout extends RelativeLayout {
	
	public ResizeLayout(Context context) {  
        super(context);  
    }  

   public ResizeLayout(Context context, AttributeSet attrs, int defStyle) {  
       super(context, attrs, defStyle);  
   }  

    public ResizeLayout(Context context, AttributeSet attrs) {  
       super(context, attrs);  
    }  
 
   public void setOnResizeListener(OnResizeListener l) {  
        mListener = l;  
   }  
 
   @Override  
   protected void onSizeChanged(int w, int h, int oldw, int oldh) {  
       super.onSizeChanged(w, h, oldw, oldh);  
       if (mListener != null) {  
           mListener.OnResize(w, h, oldw, oldh);  
      }  
   }  
 
   private OnResizeListener mListener;  
  
   public interface OnResizeListener {  
       void OnResize(int w, int h, int oldw, int oldh);  
   }; 

}
