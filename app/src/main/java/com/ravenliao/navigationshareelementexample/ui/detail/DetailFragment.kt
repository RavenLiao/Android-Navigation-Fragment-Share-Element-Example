package com.ravenliao.navigationshareelementexample.ui.detail

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import com.ravenliao.navigationshareelementexample.databinding.FragmentDetailBinding
import com.ravenliao.navigationshareelementexample.model.POSITION_KEY


private const val DRAWABLE_KEY = "drawable"

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private var drawable: Drawable? = null
    private var position: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            drawable =
                it.getParcelable<Bitmap>(DRAWABLE_KEY)?.toDrawable(requireContext().resources)
            position = it.getInt(POSITION_KEY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        initImg()
        return binding.root
    }

    private fun initImg() {
        binding.ivDetail.setImageDrawable(drawable)
        afterInitImg?.invoke(binding.ivDetail, position.toString())
    }

    companion object {
        @JvmStatic
        private var afterInitImg: ((ImageView, String) -> Unit)? = null

        @JvmStatic
        fun setCallBack(cb: ((ImageView, String) -> Unit)) {
            afterInitImg = cb
        }

        @JvmStatic
        fun newInstance(drawable: Drawable, position: Int) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(DRAWABLE_KEY, drawable.toBitmap())
                    putInt(POSITION_KEY, position)
                }
            }
    }
}