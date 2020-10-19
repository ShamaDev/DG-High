package com.dghigh.liva.second

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.dghigh.liva.R
import com.dghigh.liva.Repository
import kotlinx.android.synthetic.main.fragment_second.*

class SecondFragment : Fragment(), OnClickSecond {

    val adapter: SecondAdapter = SecondAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerSecond.setHasFixedSize(true)
        recyclerSecond.layoutManager = GridLayoutManager(context, 3)
        recyclerSecond.adapter = adapter

        back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onClickListener(pic: Int) {
        Repository.pic = pic
        findNavController().navigate(R.id.action_testFragment_to_detailFragment)
    }
}