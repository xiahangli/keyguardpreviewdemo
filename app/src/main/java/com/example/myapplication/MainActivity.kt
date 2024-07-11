package com.example.myapplication

import android.content.Context
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
        val strings = arrayListOf("item1", "item2", "item3", "item4"
            ,"item5","item6","item7","item8"
            ,"item9","item10","item11","item12"
        )
        val llm = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = llm
        val psh = PagerSnapHelper()
        val itemAnimator = DefaultItemAnimator()
        recyclerView.itemAnimator = itemAnimator
        psh.attachToRecyclerView(recyclerView)
        val verticalAdapter = VerticalAdapter(this, strings)
        // 设置adapter会在源码中清空缓存，这里只是加载一个空的rv,不会触发onCreateViewHolder
        recyclerView.adapter = verticalAdapter
        val recycledViewPool = RecycledViewPool()
        val field:Field = RecycledViewPool::class.java.getDeclaredField("DEFAULT_MAX_SCRAP")
        field.isAccessible = true
        recycledViewPool.setMaxRecycledViews(1, 12)
        // 预加载12个textView
        for ( i in 0..11) {
            val view = LayoutInflater.from(this).inflate(android.R.layout.simple_list_item_1,  null)
            val vh = MyVH(view)
            var declaredField = vh.javaClass.superclass.getDeclaredField("mItemViewType")
            declaredField.isAccessible = true
            declaredField.set(vh, 1)
            recycledViewPool.putRecycledView(vh)
        }
        recyclerView.setRecycledViewPool(recycledViewPool)
        var recycledViewPool1 = recyclerView.recycledViewPool
//        Log.i(TAG, "onCreate: recycledViewPo $recycledViewPool1")
        // 触发UI刷新，显示页面,这里就不走onCreateViewHolder了，直接走bind的逻辑
        verticalAdapter.notifyDataSetChanged()
    }

    inner class MyVH(item: View) : ViewHolder(item) {

    }
    inner class VerticalAdapter(val context: Context, val strings: ArrayList<String>) :
        RecyclerView.Adapter<MyVH>() {
        public val TYPE_NORMAL:Int = 1
        public val TYPE_TOP:Int = 2
        public val TYPE_BOTTOM:Int = 3

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVH {
            var view :View
            if(viewType == TYPE_TOP){
                view = LayoutInflater.from(context)
                    .inflate(R.layout.vertical_rv_item_1, parent, false)
            }else if (viewType ==TYPE_BOTTOM){
                view = LayoutInflater.from(context).inflate(R.layout.vertical_rv_item_2,
                    parent,false)
            }else {
                view = LayoutInflater.from(context)
                    .inflate(android.R.layout.simple_list_item_1, parent, false)
            }

            Log.i(TAG, "onCreateViewHolder: view " + view)
            return MyVH(view)
        }

        override fun getItemCount(): Int {
            return strings.size
        }

        override fun getItemViewType(position: Int): Int {
//            if (position == 0){
//                return TYPE_TOP
//            }else if (position == 1){
//                return TYPE_BOTTOM
//            }
            Log.i(TAG, "getItemViewType: TYPE_NORMAL")
            return TYPE_NORMAL
        }

        override fun onBindViewHolder(holder: MyVH, position: Int) {
            var textView = holder.itemView.findViewById<TextView>(android.R.id.text1)
            textView.text = strings[position]
           textView.setOnClickListener {
               strings.removeAt(6)
               notifyItemRemoved(6)
               notifyDataSetChanged()
           }
        }
    }
}