package com.ravenliao.navigationshareelementexample.ui.detail

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ravenliao.navigationshareelementexample.databinding.FragmentPagerBinding
import com.ravenliao.navigationshareelementexample.model.POSITION_KEY
import com.ravenliao.navigationshareelementexample.model.getDrawableList

class PagerFragment : Fragment() {
    private lateinit var binding: FragmentPagerBinding
    private lateinit var adapter: ItemAdapter

    private var clickPosition: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            clickPosition = it.getString(POSITION_KEY)

            setTransition()
        }
    }

    private fun setTransition() {

        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)
        postponeEnterTransition()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPagerBinding.inflate(layoutInflater, container, false)
        initPager()
        return binding.root
    }

    private fun initPager() {
        adapter = ItemAdapter(this).apply {
            setList(requireContext().getDrawableList())
        }

        DetailFragment.setCallBack { imageView, pos ->
            if (pos == clickPosition) {
                ViewCompat.setTransitionName(imageView, pos)
                startPostponedEnterTransition()
            }
        }

        binding.viewpager.apply {
            adapter = this@PagerFragment.adapter
            clickPosition?.also { pos ->
                setCurrentItem(pos.toInt(), false)
            }
        }
    }

    class ItemAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        private var list: List<Drawable> = emptyList()


        @SuppressLint("NotifyDataSetChanged")
        fun setList(list: List<Drawable>) {
            this.list = list
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int = list.size

        override fun createFragment(position: Int): Fragment =
            DetailFragment.newInstance(list[position], position)

    }
}