package com.example.myapplication

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
import androidx.recyclerview.widget.RecyclerView.ViewHolder
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
//            private var mIsUp :Boolean = false
            var prevPage: View? = null
            var nextPage: View? = null
            var sumY: Float = 0f
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                var horizontalLLM = verticalAdapter.getHorizontalLm()
                var findLastVisibleItemPosition = horizontalLLM!!.findLastVisibleItemPosition()
                var prePageVH = verticalAdapter.getBottomRecyclerView()?.findViewHolderForAdapterPosition(findLastVisibleItemPosition) as? MyVH
                var nextPageVH = verticalAdapter.getBottomRecyclerView()?.findViewHolderForAdapterPosition(findLastVisibleItemPosition) as? MyVH
                prevPage = prePageVH?.itemView
                nextPage = nextPageVH?.itemView
                sumY = 0f
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.i(TAG, "onScrollStateChanged: newState is idle")
                        val animator = ValueAnimator.ofFloat(0f, 1.0f)
                        animator.duration = 300
                        animator.addUpdateListener {
                            var value = it.animatedValue as Float
                        }
                        animator.start()
                }
                Log.i(
                    TAG, "onScrollStateChanged: newState $newState" +
                            " findLastVisibleItemPosition $findLastVisibleItemPosition lastVisibleChild $prevPage"
                )
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                sumY += dy // 内容上划，dy>0
                if (dy < 0) {
                    if (sumY >= 150f) sumY = 150f
                }else {
                }
            }
        })
    }

    inner class MyVH(item: View) : ViewHolder(item)


    inner class VerticalAdapter(val context: Context) :
        RecyclerView.Adapter<MyVH>() {
        private var bottomRecycler: RecyclerView? = null
        public val TYPE_NORMAL: Int = 1
        public val TYPE_TOP: Int = 2
        public val TYPE_BOTTOM: Int = 3
        var topRecycler:RecyclerView?=null

        override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
            super.onAttachedToRecyclerView(recyclerView)
            topRecycler = recyclerView;
        }

        fun getBottomRecyclerView(): RecyclerView? {
            return bottomRecycler
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
                bottomRecycler = view.findViewById<RecyclerView>(R.id.horizontal_rv)
                horizontalLLM = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                bottomRecycler!!.layoutManager = horizontalLLM
                val pager = PagerSnapHelper()
                pager.attachToRecyclerView(bottomRecycler)
                bottomRecycler!!.addItemDecoration(MyItemDecorator())
                bottomRecycler!!.itemAnimator = DefaultItemAnimator()
                horizontalLLM!!.findLastVisibleItemPosition()
                horiAdapter = HorizontalAdapter()
                bottomRecycler!!.adapter = horiAdapter
            }
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

        override fun onBindViewHolder(holder: MyVH, position: Int) {}

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
                outRect.set(110,20,20,20)
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

        inner class VH(item: View) : ViewHolder(item) {

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
            itemView.text = strings[position]
        }
    }
}