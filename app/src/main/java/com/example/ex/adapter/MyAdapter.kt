package com.example.ex.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ex.R
import com.example.ex.databinding.WorkItemBinding
import com.example.ex.view.NewActivity


class MyAdapter(
    var c: Context, var workList:ArrayList<WorkData>
): RecyclerView.Adapter<MyAdapter.WorkViewHolder>()
{

    inner class WorkViewHolder(var v:WorkItemBinding): RecyclerView.ViewHolder(v.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkViewHolder {
        val inflter = LayoutInflater.from(parent.context)
        val v = DataBindingUtil.inflate<WorkItemBinding>(
            inflter, R.layout.work_item,parent,
            false)
        return WorkViewHolder(v)
    }

    override fun onBindViewHolder(holder: WorkViewHolder, position: Int) {
        val newList = workList[position]
        holder.v.isWork = workList[position]
        holder.v.root.setOnClickListener {
            val Img = newList.image
            val Name = newList.name
            val Text = newList.text
            val mIntetn = Intent(c, NewActivity::class.java)
            mIntetn.putExtra("img",Img)
            mIntetn.putExtra("name",Name)
            mIntetn.putExtra("text",Text)
            c.startActivity(mIntetn)
        }
    }

    override fun getItemCount(): Int {
        return workList.size
    }

    fun updatework(workLists: ArrayList<WorkData>){
        this.workList = workLists
        notifyDataSetChanged()
    }

}