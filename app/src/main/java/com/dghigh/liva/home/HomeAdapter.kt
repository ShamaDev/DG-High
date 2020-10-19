package com.dghigh.liva.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dghigh.liva.R

class HomeAdapter(_onClick: OnClick) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    var onClick = _onClick

    val listPic = listOf(
        R.drawable.item1,
        R.drawable.item2,
        R.drawable.item3,
        R.drawable.item4,
        R.drawable.item5,
        R.drawable.item6,
        R.drawable.item7,
        R.drawable.item8,
        R.drawable.item9
    )
    val listName = listOf(
        "Футбол",
        "Хоккей",
        "Теннис",
        "Бейсбол",
        "Крикет",
        "Баскетбол",
        "Волейбол",
        "ММА",
        "Гольф"
    )

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.nameHome)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_home, parent, false))
    }

    override fun onBindViewHolder(holder: HomeAdapter.ViewHolder, position: Int) {
        holder.itemView.setBackgroundResource(listPic[position])
        holder.name.text = listName[position]

        holder.itemView.setOnClickListener {
            onClick.onClickListener(position)
        }
    }

    override fun getItemCount() = listPic.size
}