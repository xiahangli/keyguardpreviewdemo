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
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : Activity() {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val topRv = binding.root.findViewById<RecyclerView>(R.id.rv)
        val verticalRvLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        topRv.layoutManager = verticalRvLayoutManager
        val verticalRvSnapHelper = PagerSnapHelper()
        topRv.itemAnimator = null
        verticalRvSnapHelper.attachToRecyclerView(topRv)
        val verticalAdapter = VerticalAdapter(this)
        // 设置adapter会在源码中清空缓存，这里只是加载一个空的rv,不会触发onCreateViewHolder
        topRv.adapter = verticalAdapter
        topRv.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        topRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var isTopRvBottomItem: Boolean = false

            //            private var mIsUp :Boolean = false
            var prevPage: View? = null
            var nextPage: View? = null
            var sumY: Float = 0f
            override fun onScrollStateChanged(toprv: RecyclerView, newState: Int) {
                super.onScrollStateChanged(toprv, newState)
                var horizontalLLM = verticalAdapter.getHorizontalLm()
                var findLastVisibleItemPosition = horizontalLLM!!.findLastVisibleItemPosition()
                var findFirstVisibleItemPosition = horizontalLLM!!.findFirstVisibleItemPosition()
                var preVH = verticalAdapter.getBottomRecyclerView()?.findViewHolderForAdapterPosition(findFirstVisibleItemPosition) as? HorizontalAdapter.VH
                var nextVH = verticalAdapter.getBottomRecyclerView()?.findViewHolderForAdapterPosition(findLastVisibleItemPosition) as? HorizontalAdapter.VH
                prevPage = preVH?.itemView
                nextPage = nextVH?.itemView
                if (findLastVisibleItemPosition==1)
                    prevPage=null

                sumY = 0f
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING){
                    isTopRvBottomItem = (toprv.layoutManager as? LinearLayoutManager)?.findFirstCompletelyVisibleItemPosition() == 1
                    Log.i(TAG, "onScrollStateChanged: isTopRvBottomItem $isTopRvBottomItem")
                }
                Log.i(TAG, "onScrollStateChanged: newState is $newState isAnimating ${toprv.isAnimating} isComputingLayout ${toprv.isComputingLayout}")

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 顶部到底部需要动画
                    var findSnapView = verticalRvSnapHelper.findSnapView(verticalRvLayoutManager)
                    var id = findSnapView?.id
                    if (id == R.id.bottom) {
                        Log.i(TAG, "onScrollStateChanged: findSnapView " + findSnapView + " id $id")
                        val animator = ValueAnimator.ofFloat(0f, 1.0f)
                        animator.duration = 300
                        animator.addUpdateListener {
                            var value = it.animatedValue as Float
                            nextPage?.run {
                                var transX = translationX
                                // 需要平移的具体为这个item本身已经平移的具体，平移到0

                                transX = transX*( 1-value)
                                translationX =  transX
                            }
                            prevPage?.run{
                                var transX = translationX
                                //itemOffset=120,transx是《=0的
                                transX = transX*(1-value)
                                translationX = transX
                            }
                        }
                        animator.start()
                    }

                }
//                Log.i(
//                    TAG, "onScrollStateChanged: newState $newState" +
//                            " findLastVisibleItemPosition $findLastVisibleItemPosition lastVisibleChild $prevPage"
//                )
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

//                sumY += dy // 内容上划，dy>0
//                if (sumY>=itemOffset)sumY=itemOffset
//                if (sumY<=-itemOffset)sumY=-itemOffset
//                if (sumY >= itemOffset) sumY = itemOffset
//                if (sumY < -itemOffset) sumY = -itemOffset
                Log.i(TAG, "onScrolled: prePage $prevPage")
                if (isTopRvBottomItem){//底部需要跟手
                    nextPage?.run {
                        var transX = translationX - dy
                        if (transX > itemOffset)
                            transX = itemOffset
                        if(transX<0)
                            transX=0f
                        translationX =transX
                    }
                    prevPage?.run {
                           var transX = translationX + dy
                        if (transX > 0)
                            transX = 0f
                        if(transX<-itemOffset)
                            transX=-itemOffset
                        translationX = transX
                    }
                }

//                if (dy < 0) {
//                }else {
//                }
            }
        })
    }


    inner class VerticalAdapter(val context: Context) :
        RecyclerView.Adapter<VerticalAdapter.MyVH>() {
        private var horizontalRecyclerView: RecyclerView? = null
        public val TYPE_NORMAL: Int = 1
        public val TYPE_TOP: Int = 2
        public val TYPE_BOTTOM: Int = 3
        var verticalRecyclerView: RecyclerView? = null


        inner class MyVH(item: View) : ViewHolder(item)
        override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
            super.onAttachedToRecyclerView(recyclerView)
            verticalRecyclerView = recyclerView;
        }

        fun getBottomRecyclerView(): RecyclerView? {
            return horizontalRecyclerView
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
                horizontalRecyclerView = view.findViewById<RecyclerView>(R.id.horizontal_rv)

                horizontalLLM = InterceptableLinearLayoutManager(context, RecyclerView.HORIZONTAL, false, {
                    val isAnimating = verticalRecyclerView?.isAnimating
                    Log.i(TAG, "onCreateViewHolder: isAnimating $isAnimating")
                     isAnimating == true
                },{
                    var linearLayoutManager =
                        verticalRecyclerView?.layoutManager as? LinearLayoutManager
                    // 如果linearLayoutManager 为null怎么样
                     linearLayoutManager?.findLastCompletelyVisibleItemPosition() == 1
//                    linearLayoutManager
                })
                horizontalRecyclerView!!.layoutManager = horizontalLLM
                val pager = PagerSnapHelper()
                pager.attachToRecyclerView(horizontalRecyclerView)
                horizontalRecyclerView!!.addItemDecoration(MyItemDecorator())
                horizontalRecyclerView!!.itemAnimator = DefaultItemAnimator()
                horizontalLLM!!.findLastVisibleItemPosition()
                horiAdapter = HorizontalAdapter()
                horizontalRecyclerView!!.adapter = horiAdapter
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
    var itemOffset=96f
//    var invisibleItemOffset=20f

    inner class MyItemDecorator : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            var pos = parent.getChildAdapterPosition(view)

            if (pos == 0) {
                outRect.set(110, 20, 20, 20)
            } else {
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

        override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
            super.onAttachedToRecyclerView(recyclerView)
            recyclerView.post{
                itemOffset = (recyclerView.width - recyclerView.getChildAt(0).width - 20)/2f
            }
        }

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
            if (position==1)
            {
                itemView.translationX = itemOffset
            }
        }
    }

   inner class InterceptableLinearLayoutManager(context: Context,@RecyclerView.Orientation orientation: Int=RecyclerView.VERTICAL,
                                                reverseorder:Boolean=false,var verticalScrolling :()->Boolean
   ,var isBottomPage :()->Boolean) :
        LinearLayoutManager(context,orientation,reverseorder) {

       override fun canScrollHorizontally(): Boolean {
           val isVer = verticalScrolling()
           val isBot = isBottomPage()

           Log.i(TAG, "canScrollHorizontally: verticalScrolling $isVer isBottomPage $isBot")
           return !isVer &&isBot&& super.canScrollHorizontally()
       }
//       override fun canScrollVertically(): Boolean {
//           return !verticalScrolling() && super.canScrollVertically()
//       }
    }
}