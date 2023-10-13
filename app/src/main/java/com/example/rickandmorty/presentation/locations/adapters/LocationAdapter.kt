package com.example.rickandmorty.presentation.locations.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.databinding.ItemViewRecycleEpisodeLocationBinding
import com.example.rickandmorty.domain.locations.model.Locations
import com.example.rickandmorty.presentation.main.adapters.BaseAdapter

class LocationAdapter : BaseAdapter<Locations, LocationAdapter.ViewHolder>(
    createDefaultDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemViewRecycleEpisodeLocationBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun bindViewHolder(holder: ViewHolder, item: Locations) {
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemViewRecycleEpisodeLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Locations) {
            binding.apply {
                field1.text = item.name
                field2.text = "Type - ${item.type}"
                field3.text = "Dimension - ${item.dimension}"
            }
            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }
}


