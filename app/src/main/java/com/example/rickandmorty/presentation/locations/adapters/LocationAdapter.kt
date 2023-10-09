package com.example.rickandmorty.presentation.locations.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.databinding.ItemViewRecycleLocationBinding
import com.example.rickandmorty.domain.locations.model.Locations
import com.example.rickandmorty.presentation.main.BaseAdapter


class LocationAdapter : BaseAdapter<Locations, LocationAdapter.ViewHolder>(
    createDefaultDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemViewRecycleLocationBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun bindViewHolder(holder: ViewHolder, item: Locations) {
        holder.bind(item)
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
                onItemClick(item)
            }
        }
    }
}


