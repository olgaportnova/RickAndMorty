package com.example.rickandmorty.presentation.locations.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.databinding.ItemViewRecycleEpisodeBinding
import com.example.rickandmorty.databinding.ItemViewRecycleLocationBinding
import com.example.rickandmorty.domain.locations.model.Locations


class LocationAdapter:  PagingDataAdapter<Locations, LocationAdapter.ViewHolder>(differCallback) {

    private lateinit var binding: ItemViewRecycleLocationBinding
    private lateinit var context: Context

    var onItemClickListener: ((Locations) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemViewRecycleLocationBinding.inflate(inflater, parent, false)
        context = parent.context
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    inner class ViewHolder(private val binding: ItemViewRecycleLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Locations) {
            binding.apply {
                locationName.text = item.name
                locationType.text = "Type - ${item.type}"
                locationDimension.text = "Dimension - ${item.dimension}"


            }

            binding.root.setOnClickListener {
                onItemClickListener?.invoke(item)
            }
        }

    }

    companion object {
        val differCallback = object : DiffUtil.ItemCallback<Locations>() {
            override fun areItemsTheSame(oldItem: Locations, newItem: Locations): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Locations, newItem: Locations): Boolean {
                return oldItem == newItem
            }
        }
    }

}

