package com.ravenliao.navigationshareelementexample.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ravenliao.navigationshareelementexample.R
import com.ravenliao.navigationshareelementexample.databinding.FragmentListBinding
import com.ravenliao.navigationshareelementexample.model.POSITION_KEY
import com.ravenliao.navigationshareelementexample.model.getDrawableList

class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private lateinit var adapter: ListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(layoutInflater, container, false)
        initList()
        return binding.root
    }

    private fun initList() {
        adapter = ListAdapter { holder, position ->
            holder.itemView.setOnClickListener {
                val positionString = position.toString()
                ViewCompat.setTransitionName(holder.binding.ivImg, positionString)
                val extras = FragmentNavigatorExtras(holder.binding.ivImg to positionString)
                findNavController().navigate(
                    R.id.action_listFragment_to_pagerFragment,
                    Bundle().apply {
                        putString(POSITION_KEY, positionString)
                    }, null, extras
                )
            }
        }.apply {
            setList(requireContext().getDrawableList())
        }

        binding.list.apply {
            adapter = this@ListFragment.adapter
            layoutManager = GridLayoutManager(context, 3)
        }
    }


}