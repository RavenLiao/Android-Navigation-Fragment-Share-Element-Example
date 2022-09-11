package com.ravenliao.navigationshareelementexample.ui.list

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ravenliao.navigationshareelementexample.databinding.ItemListBinding

class ListAdapter(private val afterItemBinding: (holder: ItemHolder, position: Int) -> Unit) :
    RecyclerView.Adapter<ListAdapter.ItemHolder>() {
    private var list: List<Drawable> = emptyList()
    private lateinit var inflate: LayoutInflater


    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<Drawable>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        if (!this::inflate.isInitialized) {
            inflate = LayoutInflater.from(parent.context)
        }
        return ItemHolder(ItemListBinding.inflate(inflate, parent, false))
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(list[position], position, afterItemBinding)
    }

    class ItemHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            drawable: Drawable,
            position: Int,
            afterItemBinding: (holder: ItemHolder, position: Int) -> Unit
        ) {
            binding.ivImg.setImageDrawable(drawable)
            afterItemBinding.invoke(this, position)
        }

    }
}