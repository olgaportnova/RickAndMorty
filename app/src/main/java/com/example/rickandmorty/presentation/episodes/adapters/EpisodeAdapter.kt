package com.example.rickandmorty.presentation.episodes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.databinding.ItemViewRecycleEpisodeBinding
import com.example.rickandmorty.domain.episodes.model.Episodes
import com.example.rickandmorty.presentation.main.adapters.BaseAdapter


class EpisodeAdapter : BaseAdapter<Episodes, EpisodeAdapter.ViewHolder>(
    createDefaultDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemViewRecycleEpisodeBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun bindViewHolder(holder: ViewHolder, item: Episodes) {
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemViewRecycleEpisodeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Episodes) {
            binding.apply {
                episodeName.text = item.name
                episodeCode.text = "Episode # - ${item.episode} -"
                episodeAirDate.text = "Air date - ${item.air_date} -"
            }
            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }

    }
}


