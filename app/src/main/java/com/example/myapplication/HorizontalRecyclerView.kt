package com.example.myapplication

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class HorizontalRecyclerView : RecyclerView {

    constructor(context: Context, attr: AttributeSet?) : super(context, attr)

    constructor(context: Context, attr: AttributeSet?, defStyleRes: Int) : super(context, attr, defStyleRes)

    constructor(context: Context) : super(context)

    init {
        Log.i(TAG, "init")
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        var dispatchTouchEvent = super.dispatchTouchEvent(ev)
        Log.i(TAG, "dispatchTouchEvent: ev ${ev.action} $dispatchTouchEvent")
        return dispatchTouchEvent
    }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        var onInterceptTouchEvent = super.onInterceptTouchEvent(e)
        Log.i(TAG, "dispatchTouchEvent: ev ${e.action} $onInterceptTouchEvent")
        return onInterceptTouchEvent
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        var onTouchEvent = super.onTouchEvent(e)
        Log.i(TAG, "onTouchEvent: ev ${e.action} $onTouchEvent")
        return onTouchEvent
    }

   companion object{
       const val  TAG = "HorizontalRecyclerView"
   }
}