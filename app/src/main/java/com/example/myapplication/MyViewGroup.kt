package com.example.myapplication

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.TextView

class MyViewGroup : ViewGroup {
    private var tv: TextView?=null
    private var mIsScrolling: Boolean = false
    private var x0: Float = 0f
    private var y0: Float = 0f


    constructor(context: Context?, attr: AttributeSet?) : this(context, attr, 0) {
        Log.i(TAG, "constructor: 2")
    }

    constructor(context: Context?, attr: AttributeSet?, defStyleRes: Int) : super(
        context,
        attr,
        defStyleRes
    ) {
        Log.i(TAG, "constructor: 3")
        post {
            tv = findViewById<TextView>(R.id.textView)
            Log.i(TAG, "post: tv $tv")
            tv?.setOnTouchListener { v, event ->
                return@setOnTouchListener when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        Log.i(TAG, "tv onTouch : down parent $parent this $this getchild(0) ${getChildAt(0)}")
//                        getChildAt(0).parent.requestDisallowInterceptTouchEvent(true)
                        true

                    }
                    MotionEvent.ACTION_MOVE-> {
                        Log.i(TAG, "tv onTouch : move return true")
                        true
                    }
                    else ->
                        onTouchEvent(event)
                }
            }
        }

    }

    constructor(context: Context?) : this(context, null, 0)

    private val mTouchSlop: Int = ViewConfiguration.get(context).scaledTouchSlop

    init {
        Log.i(TAG, "init")
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        // This method only determines whether you want to intercept the motion.
        // If this method returns true, onTouchEvent is called and you can do
        // the actual scrolling there.
        return when (ev.actionMasked) {
            // Always handle the case of the touch gesture being complete.
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                // Release the scroll.
                lastY = ev.getY(0)
                mIsScrolling = false
                false // Don't intercept the touch event. Let the child handle it.
            }

            MotionEvent.ACTION_MOVE -> {
                Log.i(TAG, "onInterceptTouchEvent: move mIsScrolling=$mIsScrolling")

                if (mIsScrolling) {
                    // You're currently scrolling, so intercept the touch event.
                    lastY = ev.getY(0)
                    true
                } else {

                    // If the user drags their finger horizontally more than the
                    // touch slop, start the scroll.

                    // Left as an exercise for the reader.
                    val yDiff: Int = calculateDistanceY(ev).toInt()

                    // Touch slop is calculated using ViewConfiguration constants.
                    if (yDiff > mTouchSlop) {
                        // Start scrolling!
                        Log.i(TAG, "onInterceptTouchEvent:  mIsScrolling = true")
                        mIsScrolling = true
                        lastY = ev.getY(0)
                        true
                    } else {
                        lastY = ev.getY(0)
                        false
                    }

                }

            }

            else -> {
                // In general, don't intercept touch events. The child view
                // handles them.
                false
            }
        }
    }

    private fun calculateDistanceY(ev: MotionEvent): Float {
        return ev.getY(0) - lastY
    }

    var lastY = 0f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Here, you actually handle the touch event. For example, if the action
        // is ACTION_MOVE, scroll this container. This method is only called if
        // the touch event is intercepted in onInterceptTouchEvent.
        if (event.action == MotionEvent.ACTION_MOVE) {
            val y1 = event.getY(0)
            val yDiff = y1 - lastY
            lastY = y1
            getChildAt(0).translationY += yDiff
            Log.i(TAG, "onTouchEvent: tranlation ${getChildAt(0).translationY}")
        }
        return super.onTouchEvent(event)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        getChildAt(0).layout(left, top, right, bottom)
    }

    companion object {
        const val TAG = "MyViewGroup"
    }
}