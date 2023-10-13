package com.example.rickandmorty.presentation.episodes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.databinding.ItemViewRecycleEpisodeLocationBinding
import com.example.rickandmorty.domain.episodes.model.Episodes
import com.example.rickandmorty.presentation.main.adapters.BaseAdapter


class EpisodeAdapter : BaseAdapter<Episodes, EpisodeAdapter.ViewHolder>(
    createDefaultDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemViewRecycleEpisodeLocationBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun bindViewHolder(holder: ViewHolder, item: Episodes) {
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemViewRecycleEpisodeLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Episodes) {
            binding.apply {
                field1.text = item.name
                field2.text = "Episode # - ${item.episode} -"
                field3.text = "Air date - ${item.air_date} -"
            }
            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }

    }
}


