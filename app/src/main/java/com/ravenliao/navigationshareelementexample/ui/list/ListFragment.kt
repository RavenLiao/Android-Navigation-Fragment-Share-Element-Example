package com.ravenliao.navigationshareelementexample.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
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

const val FRAGMENT_TRANSITION_EXTRA="moveTransition"

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

                    names?.first()?.also { name ->
                        sharedElements?.apply {
                            clear()
                            val view =
                                (binding.list.findViewHolderForAdapterPosition(pos.toInt()) as ListAdapter.ItemHolder).binding.ivImg

                            ViewCompat.setTransitionName(view, name)
                            put(name, view)
                        }
                    }
                }
            }
        })
    }

    private fun initList() {
        adapter = ListAdapter { holder, position ->
            val positionString = position.toString()

            if (positionString == returnPosition) {
                startPostponedEnterTransition()
            }

            holder.itemView.setOnClickListener {


                setFragmentResultListener(RETURN_POSITION_KEY) { key, bundle ->
                    clearFragmentResultListener(requestKey = key)
                    //接收返回的position
                    returnPosition = bundle.getString(POSITION_KEY)
                    executeReturnTransition()
                }

                ViewCompat.setTransitionName(holder.binding.ivImg, positionString)

                //此处的Extra与TransitionName无关,这个name会在pager处的callback中传入
                val extras = FragmentNavigatorExtras(holder.binding.ivImg to FRAGMENT_TRANSITION_EXTRA)
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

        layoutManager = GridLayoutManager(context, 2)

        binding.list.apply {
            adapter = this@ListFragment.adapter
            layoutManager = this@ListFragment.layoutManager
        }
    }

    private fun executeReturnTransition() {
        val pos = returnPosition?.toIntOrNull() ?: return
        postponeEnterTransition()
        binding.list.doOnPreDraw {
            layoutManager.apply {
                if (pos !in findFirstVisibleItemPosition()..findLastVisibleItemPosition()) {
                    //若返回位置不可视，则先滚动到一半的位置
                    scrollToPositionWithOffset(pos, binding.list.height shr 1)
                } else {
                    startPostponedEnterTransition()
                }
            }
        }
    }


}