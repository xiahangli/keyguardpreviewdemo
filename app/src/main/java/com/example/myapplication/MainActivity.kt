package com.example.myapplication

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.
import com.example.myapplication.databinding.ActivityMainBinding
import kotlin.math.abs

class MainActivity : Activity() {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.rv)
        val llm = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = llm
        val psh = PagerSnapHelper()
        recyclerView.itemAnimator = null
        psh.attachToRecyclerView(recyclerView)
        val verticalAdapter = VerticalAdapter(this)
        // 设置adapter会在源码中清空缓存，这里只是加载一个空的rv,不会触发onCreateViewHolder
        recyclerView.adapter = verticalAdapter
        recyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var mIsUp :Boolean = false
            var childAt: View? = null
            var sumY: Float = 0f
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                var horizontalLLM = verticalAdapter.getHorizontalLm()
                // 这个
                var findLastVisibleItemPosition = horizontalLLM!!.findLastVisibleItemPosition()
                val horizAdapter = verticalAdapter.getHorizontalAdapter()
                sumY = 0f
                // 这个只能拿到当前屏幕可见的index,超过2用2代替
                if (findLastVisibleItemPosition >= 2)
                    findLastVisibleItemPosition = 2
                childAt = horizontalLLM.getChildAt(findLastVisibleItemPosition)
                if (childAt==null) return
                if (newState == RecyclerView.SCROLL_STATE_IDLE ) {
                    Log.i(TAG, "onScrollStateChanged: newState is idle")
                    if (mIsUp){
//                        val animator = ObjectAnimator.ofFloat(childAt,0,1f)
                        val animator = ValueAnimator.ofFloat(0f, 1.0f)
                        animator.duration = 300
                        animator.addUpdateListener {
                            var values = it.animatedValue as Float
                            var lp = childAt!!.layoutParams as  MarginLayoutParams
                            lp.leftMargin = (150*(1f.minus(values))).toInt()
                            childAt!!.layoutParams = lp
                        }
                        animator.start()

                    }
                }
                Log.i(
                    TAG, "onScrollStateChanged: newState $newState" +
                            " findLastVisibleItemPosition $findLastVisibleItemPosition lastVisibleChild $childAt"
                )
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy < 0) {
//                    if (childAt == null)return

                    var horizontalLLM = verticalAdapter.getHorizontalLm()
                    var findLastVisibleItemPosition = horizontalLLM!!.findLastVisibleItemPosition()
                    childAt = horizontalLLM.getChildAt(2)
                    mIsUp=false
                    var layoutParams: MarginLayoutParams =
                        childAt!!.layoutParams as MarginLayoutParams
                    sumY = sumY.plus(abs(dy))
                    if (sumY >= 150f) sumY = 150f
//                    childAt!!.translationX = sumY.toFloat()
                    layoutParams.leftMargin = sumY.toInt()
                    childAt!!.layoutParams = layoutParams

                    Log.i(TAG, "onScrolled: child ${childAt} translateY $sumY")
                }else {
                    mIsUp=true
                }
            }
        })
        verticalAdapter.notifyDataSetChanged()
    }

    inner class MyVH(item: View) : RecyclerView.ViewHolder(item) {

    }


    inner class VerticalAdapter(val context: Context) :
        RecyclerView.Adapter<MyVH>() {
        private var recyclerView: RecyclerView? = null
        public val TYPE_NORMAL: Int = 1
        public val TYPE_TOP: Int = 2
        public val TYPE_BOTTOM: Int = 3

        fun getBottomRecyclerView(): RecyclerView? {
            return recyclerView
        }

        var horiAdapter: HorizontalAdapter? = null
        var horizontalLLM: LinearLayoutManager? = null
        fun getHorizontalAdapter(): HorizontalAdapter? {
            return horiAdapter
        }

        fun getHorizontalLm(): LinearLayoutManager? {
            return horizontalLLM
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVH {
            var view: View
            if (viewType == TYPE_TOP) {
                view = LayoutInflater.from(context)
                    .inflate(R.layout.vertical_rv_item_1, parent, false)
            } else {
                view = LayoutInflater.from(context)
                    .inflate(R.layout.vertical_rv_item_2, parent, false)
                recyclerView = view.findViewById<RecyclerView>(R.id.horizontal_rv)
                horizontalLLM = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                recyclerView!!.layoutManager = horizontalLLM
                val pager = PagerSnapHelper()
                pager.attachToRecyclerView(recyclerView)
                recyclerView!!.addItemDecoration(MyItemDecorator())
                recyclerView!!.itemAnimator = DefaultItemAnimator()
                horizontalLLM!!.findLastVisibleItemPosition()
                horiAdapter = HorizontalAdapter()
                recyclerView!!.adapter = horiAdapter
                recyclerView!!.viewTreeObserver.addOnGlobalLayoutListener(object :
                    OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        var findFirstVisibleItemPosition =
                            horizontalLLM!!.findFirstVisibleItemPosition()
                        var findLastVisibleItemPosition =
                            horizontalLLM!!.findLastVisibleItemPosition()
                        Log.i(
                            TAG,
                            "onGlobalLayout: findFirstVisibleItemPosition $findFirstVisibleItemPosition " +
                                    "findLastVisibleItemPosition $findLastVisibleItemPosition"
                        )

                    }
                })

            }

//            Log.i(TAG, "onCreateViewHolder: view " + view)
            return MyVH(view)
        }


        override fun getItemCount(): Int {
            return 2
        }

        override fun getItemViewType(position: Int): Int {
            if (position == 0) {
                return TYPE_TOP
            } else if (position == 1) {
                return TYPE_BOTTOM
            }
            Log.i(TAG, "getItemViewType: TYPE_NORMAL")
            return TYPE_NORMAL
        }

        override fun onBindViewHolder(holder: MyVH, position: Int) {
//            var textView = holder.itemView.findViewById<TextView>(android.R.id.text1)
//            textView.text = strings[position]
//           textView.setOnClickListener {
//               strings.removeAt(6)
//               notifyItemRemoved(6)
//               notifyDataSetChanged()
//           }
//            notify
        }

        fun getSecondItem(): View? {
            return null
        }
    }

    inner class MyItemDecorator : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            var pos = parent.getChildAdapterPosition(view)

            if (pos==0){
                outRect.set(100,20,20,20)
            }else{
                outRect.set(20, 20, 20, 20)

            }
        }

    }

    inner class HorizontalAdapter : RecyclerView.Adapter<HorizontalAdapter.VH>() {

        val strings = arrayListOf(
            "item0",
            "item1",
            "item2",
            "item3",
            "item4",
            "item5",
            "item6",
            "item7",
            "item8",
            "item9",
            "item10",
            "item11",
            "item12",
            "item13",
            "item14",
            "item15",
            "item16",
            "item17",
            "item18",
            "item19"
        )

        inner class VH(item: View) : RecyclerView.ViewHolder(item) {

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.horizontal_rv_item, parent, false)
            Log.i(TAG, "onCreateViewHolder: view $view")
            return VH(view)
        }

        override fun getItemCount(): Int {
            return strings.size
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            var itemView = holder.itemView.findViewById<TextView>(android.R.id.text1)
            if (position == 1) {
                var lp = itemView.layoutParams as MarginLayoutParams
                lp.leftMargin = 150
                itemView.layoutParams = lp

                // 动效
//                var animate = findViewById.animate()
//                animate.scaleX(0.5f).scaleY(.5f).withStartAction {
//                    findViewById.scaleX = 1f
//                    findViewById.scaleY = 1f
//                }.withEndAction {
//
//                }.duration = 20
//                animate.start()
            }

            itemView.text = strings[position]
        }
    }
}