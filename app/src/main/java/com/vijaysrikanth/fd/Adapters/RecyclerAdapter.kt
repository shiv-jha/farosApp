package com.vijaysrikanth.fd

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sgs.citytax.util.IClickListener
import com.vijaysrikanth.fd.api.Response.GetLayoutList

class RecyclerAdapter(val listener: IClickListener) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    var mGetLayoutList : List<GetLayoutList> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_adapter,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mGetLayoutList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.tvName.text = mGetLayoutList.get(position).layout_name
//        Glide.with(context).load(movieList.get(position).image)
//                .apply(RequestOptions().centerCrop())
//                .into(holder.image)
        holder.llContainer.setOnClickListener {
            listener.onClick(it, position, mGetLayoutList.get(position))
        }
    }

    fun setListItems(list: List<GetLayoutList>){
        this.mGetLayoutList = list;
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.title)
        val llContainer: LinearLayout = itemView.findViewById(R.id.llContainer)
    }
}