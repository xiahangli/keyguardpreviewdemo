package com.example.myapplication

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VerticalRecyclerView : RecyclerView {
    private var tv: TextView?=null
    private var mIsScrolling: Boolean = false
    private var x0: Float = 0f
    private var y0: Float = 0f


    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0) {
        Log.i(TAG, "constructor: 2")
    }

    constructor(context: Context, attr: AttributeSet?, defStyleRes: Int) : super(
        context,
        attr,
        defStyleRes
    ) {
        Log.i(TAG, "constructor: 3")
    }

    constructor(context: Context) : this(context, null, 0) {}

    private val mTouchSlop: Int = ViewConfiguration.get(context).scaledTouchSlop

    init {
        Log.i(TAG, "init")
    }

    var first = false

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        var dispatchTouchEvent = super.dispatchTouchEvent(ev)
        Log.i(TAG, "dispatchTouchEvent: ev ${ev.action} $dispatchTouchEvent")
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        var onInterceptTouchEvent = super.onInterceptTouchEvent(e)
        Log.i(TAG, "dispatchTouchEvent: ev ${e.action} $onInterceptTouchEvent")
        return super.onInterceptTouchEvent(e)
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        var onTouchEvent = super.onTouchEvent(e)
        Log.i(TAG, "onTouchEvent: ev ${e.action} $onTouchEvent")
        return onTouchEvent
    }

   companion object{
       const val  TAG = "VerticalRecyclerView"
   }
}