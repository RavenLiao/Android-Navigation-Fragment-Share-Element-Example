package com.ravenliao.navigationshareelementexample.ui.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ravenliao.navigationshareelementexample.R
import com.ravenliao.navigationshareelementexample.databinding.FragmentListBinding
import com.ravenliao.navigationshareelementexample.model.POSITION_KEY
import com.ravenliao.navigationshareelementexample.model.getDrawableList

const val RETURN_POSITION_KEY = "return_position"

class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private lateinit var adapter: ListAdapter
    private lateinit var layoutManager: GridLayoutManager

    private var returnPosition: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(layoutInflater, container, false)
        initList()

        setTransition()
        return binding.root
    }

    private fun setTransition() {
        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: MutableList<String>?,
                sharedElements: MutableMap<String, View>?
            ) {
                val pos = returnPosition
                if (pos != null) {
                    returnPosition = null
                    names?.apply {
                        clear()
                        add(pos)
                    }

                    sharedElements?.apply {
                        clear()
                        val view =
                            (binding.list.findViewHolderForAdapterPosition(pos.toInt()) as ListAdapter.ItemHolder).binding.ivImg
                        put(pos, view)
                    }
                }
                Log.e("Exit ", "names:${names}  eles:$sharedElements")
            }
        })
    }

    private fun initList() {
        adapter = ListAdapter { holder, position ->
            val positionString = position.toString()
            ViewCompat.setTransitionName(holder.binding.ivImg, positionString)

            if (positionString == returnPosition) {
                startPostponedEnterTransition()
            }

            holder.itemView.setOnClickListener {


                setFragmentResultListener(RETURN_POSITION_KEY) { key, bundle ->
                    clearFragmentResultListener(requestKey = key)
                    //接收返回的position
                    returnPosition = bundle.getString(POSITION_KEY)
                    postponeEnterTransition()
                }

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

        layoutManager = GridLayoutManager(context, 3)

        binding.list.apply {
            adapter = this@ListFragment.adapter
            layoutManager = this@ListFragment.layoutManager
        }
    }


}