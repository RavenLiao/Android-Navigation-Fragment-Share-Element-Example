package com.ravenliao.navigationshareelementexample.ui.detail

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.app.SharedElementCallback
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ravenliao.navigationshareelementexample.databinding.FragmentPagerBinding
import com.ravenliao.navigationshareelementexample.model.POSITION_KEY
import com.ravenliao.navigationshareelementexample.model.getDrawableList
import com.ravenliao.navigationshareelementexample.ui.list.RETURN_POSITION_KEY

class PagerFragment : Fragment() {
    private lateinit var binding: FragmentPagerBinding
    private lateinit var adapter: ItemAdapter

    private var clickPosition: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            clickPosition = it.getString(POSITION_KEY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPagerBinding.inflate(layoutInflater, container, false)
        initPager()
        setTransition()
        return binding.root
    }

    private fun setTransition() {
        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: MutableList<String>?,
                sharedElements: MutableMap<String, View>?
            ) {
                val nowPosition = binding.viewpager.currentItem

                names?.first()?.also { name ->
                    sharedElements?.apply {
                        clear()
                        val imageView = adapter.getFragment(nowPosition).getImageView()
                        ViewCompat.setTransitionName(imageView, nowPosition.toString())
                        put(name, imageView)
                    }
                }
            }
        })

        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)
        postponeEnterTransition()

        setBackPress()
    }

    private fun setBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            setFragmentResult(
                RETURN_POSITION_KEY,
                Bundle().apply {
                    putString(POSITION_KEY, binding.viewpager.currentItem.toString())
                }
            )
            findNavController().navigateUp()
        }
    }

    private fun initPager() {
        adapter = ItemAdapter(this).apply {
            setList(requireContext().getDrawableList())
        }

        DetailFragment.setCallBack { _, pos ->

            if (pos == clickPosition) {
                startPostponedEnterTransition()
                clickPosition = null
            }
        }

        binding.viewpager.apply {
            adapter = this@PagerFragment.adapter
            offscreenPageLimit = 1
            clickPosition?.also { pos ->
                setCurrentItem(pos.toInt(), false)
            }
        }
    }

    class ItemAdapter(private val fragment: Fragment) : FragmentStateAdapter(fragment) {
        private var list: List<Drawable> = emptyList()


        @SuppressLint("NotifyDataSetChanged")
        fun setList(list: List<Drawable>) {
            this.list = list
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int = list.size

        fun getFragment(position: Int): DetailFragment =
            fragment.childFragmentManager.findFragmentByTag("f${getItemId(position)}") as DetailFragment


        override fun createFragment(position: Int): Fragment =
            DetailFragment.newInstance(list[position], position)

    }
}
