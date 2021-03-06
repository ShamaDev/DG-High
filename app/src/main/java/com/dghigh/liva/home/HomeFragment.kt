package com.dghigh.liva.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.dghigh.liva.R
import com.dghigh.liva.Repository
import kotlinx.android.synthetic.main.fragment_first.*

class HomeFragment : Fragment(), OnClick {

    val adapter: HomeAdapter = HomeAdapter(this)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerHome.setHasFixedSize(true)
        recyclerHome.layoutManager = GridLayoutManager(context, 3)
        recyclerHome.adapter = adapter

//        start.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_testFragment)
//        }
    }

    override fun onClickListener(position: Int) {
        Repository.position = position
        findNavController().navigate(R.id.action_FirstFragment_to_testFragment)
    }
}