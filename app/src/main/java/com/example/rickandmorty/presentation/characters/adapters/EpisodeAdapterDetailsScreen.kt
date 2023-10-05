package com.example.rickandmorty.presentation.characters.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.databinding.ItemViewEpisodeDetailsRecycleViewBinding
import com.example.rickandmorty.domain.episodes.model.Episodes

class EpisodeAdapterDetailsScreen(
    private val listener: Listener? = null
) : ListAdapter<Episodes, EpisodeAdapterDetailsScreen.EpisodeViewHolder>(EpisodeDiffCallback()) {

    inner class EpisodeViewHolder(private val binding: ItemViewEpisodeDetailsRecycleViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(episode: Episodes) {
            binding.apply {
                episodeName.text = episode.name
                episodeNumber.text = "Episode # - ${episode.episode} -"
                episodeAirDate.text = "Air date - ${episode.air_date} -"

            }

            binding.root.setOnClickListener {
                listener?.onClick(episode)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EpisodeViewHolder {
        val binding = ItemViewEpisodeDetailsRecycleViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EpisodeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface Listener {
        fun onClick(episode: Episodes)
    }

    private class EpisodeDiffCallback : DiffUtil.ItemCallback<Episodes>() {
        override fun areItemsTheSame(oldItem: Episodes, newItem: Episodes): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Episodes, newItem: Episodes): Boolean {
            return oldItem == newItem
        }
    }
}
