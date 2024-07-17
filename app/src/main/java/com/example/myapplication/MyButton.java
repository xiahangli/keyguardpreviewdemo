package com.example.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.OverScroller;
import android.widget.Scroller;

public class MyButton extends Button {
    private static final String TAG = "MyButton";

       @Override
       public void computeScroll() {
           super.computeScroll();
           if (mScroller.computeScrollOffset()){
               Log.i(TAG, "computeScroll: currY " + mScroller.getCurrY());
                scrollTo(mScroller.getCurrX(),mScroller.getCurrY());

           }
       }
    OverScroller mScroller;

       public MyButton(Context context) {
           super(context);
       }

       public MyButton(Context context, AttributeSet attrs) {
           super(context, attrs);
           setLayerType(View.LAYER_TYPE_SOFTWARE, null);

           mScroller = new OverScroller(context);
//           mScroller = new Scroller(context);
       }

    public void startScroll() {
        Log.i(TAG, "onClick: getScrollX " + getScrollX()
                + " getScrollY " + getScrollY()
        );
        // dy>0内容上移,8s
        mScroller.startScroll(0,-50,0,900,8040);
        invalidate();
    }
}