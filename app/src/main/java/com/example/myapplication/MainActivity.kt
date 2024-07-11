package com.example.myapplication

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.myapplication.databinding.ActivityMainBinding
import java.lang.reflect.Field

class MainActivity : AppCompatActivity() {
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
        val itemAnimator = DefaultItemAnimator()
        recyclerView.itemAnimator = itemAnimator
        psh.attachToRecyclerView(recyclerView)
        val verticalAdapter = VerticalAdapter(this)
        // 设置adapter会在源码中清空缓存，这里只是加载一个空的rv,不会触发onCreateViewHolder
        recyclerView.adapter = verticalAdapter
        verticalAdapter.notifyDataSetChanged()
    }

    inner class MyVH(item: View) : ViewHolder(item) {

    }



    inner class VerticalAdapter(val context: Context) :
        RecyclerView.Adapter<MyVH>() {
        public val TYPE_NORMAL: Int = 1
        public val TYPE_TOP: Int = 2
        public val TYPE_BOTTOM: Int = 3

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVH {
            var view: View
            if (viewType == TYPE_TOP) {
                view = LayoutInflater.from(context)
                    .inflate(R.layout.vertical_rv_item_1, parent, false)
            }  else {
                view = LayoutInflater.from(context)
                    .inflate(R.layout.vertical_rv_item_2, parent, false)
                val recyclerView = view.findViewById<RecyclerView>(R.id.horizontal_rv)
                val vllm = LinearLayoutManager(context, RecyclerView.HORIZONTAL,false)
                recyclerView.layoutManager = vllm
                val pager = PagerSnapHelper()
                pager.attachToRecyclerView(recyclerView)
                recyclerView.addItemDecoration(MyItemDecorator())
                recyclerView.adapter = HorizontalAdapter()
            }

            Log.i(TAG, "onCreateViewHolder: view " + view)
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
        }
    }

    inner class MyItemDecorator : ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
//            parent.getChildAdapterPosition(view)
            outRect.set(20,20,20,20)
        }

    }

    inner class HorizontalAdapter : RecyclerView.Adapter<HorizontalAdapter.VH>() {

        val strings = arrayListOf("item1","item2","item3","item4","item5","item6")

        inner class VH(item:View) :RecyclerView.ViewHolder(item){

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.horizontal_rv_item, parent, false)
            return VH(view)
        }

        override fun getItemCount(): Int {
            return strings.size
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            holder.itemView.findViewById<TextView>(android.R.id.text1).setText(strings[position])
        }
    }
}