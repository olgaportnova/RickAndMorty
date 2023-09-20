package com.example.rickandmorty.presentation.recycleviewList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.databinding.ItemViewRecycleViewBinding


class ListFragmentAdapter<T, B : ItemViewRecycleViewBinding>(
    private val layoutInflater: (LayoutInflater, ViewGroup, Boolean) -> B,
    private val binder: B.(item: T) -> Unit
) : RecyclerView.Adapter<ListFragmentAdapter<T, B>.ItemViewHolder>() {

    private var listOfItems = mutableListOf<T>()

    inner class ItemViewHolder(private val binding: B) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: T) {
            binding.binder(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = layoutInflater(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int = listOfItems.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(listOfItems[position])
    }

    fun addNewItems(items: List<T>) {
        val positionStart = listOfItems.size
        listOfItems.addAll(items)
        notifyItemRangeInserted(positionStart, items.size)
    }
    
    interface Listener<T> {
        fun onClick(item: T)
    }
}

fun RecyclerView.setOnEndOfListReachedListener(listener: () -> Unit) {
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (!recyclerView.canScrollVertically(1) && dy > 0) {
                listener.invoke()
            }
        }
    })
}
