package com.example.rickandmorty.presentation.main.adapters

import android.annotation.SuppressLint
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T : Any, VH : RecyclerView.ViewHolder>(
    private val differCallback: DiffUtil.ItemCallback<T>
) : PagingDataAdapter<T, VH>(differCallback) {

    var onItemClickListener: ((T) -> Unit)? = null

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        item?.let { bindViewHolder(holder, it) }

        holder.itemView.setOnClickListener {
            item?.let { onItemClick(it) }
        }
    }

    abstract fun bindViewHolder(holder: VH, item: T)

    fun onItemClick(item: T) {
        onItemClickListener?.invoke(item)
    }

    companion object {
        fun <T> createDefaultDiffCallback(): DiffUtil.ItemCallback<T> {
            return object : DiffUtil.ItemCallback<T>() {
                override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
                    return oldItem?.hashCode() == newItem?.hashCode()
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }
}
