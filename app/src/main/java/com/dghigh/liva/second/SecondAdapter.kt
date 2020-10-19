package com.dghigh.liva.second

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dghigh.liva.R
import com.dghigh.liva.Repository

class SecondAdapter(_onClick: OnClickSecond) : RecyclerView.Adapter<SecondAdapter.ViewHolder>() {

    var onClick = _onClick

    val listPic = Repository.getListPic(Repository.position)


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SecondAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_second, parent, false))
    }

    override fun onBindViewHolder(holder: SecondAdapter.ViewHolder, position: Int) {
        holder.itemView.setBackgroundResource(listPic[position])

        holder.itemView.setOnClickListener {
            onClick.onClickListener(listPic[position])
        }
    }

    override fun getItemCount() = listPic.size
}