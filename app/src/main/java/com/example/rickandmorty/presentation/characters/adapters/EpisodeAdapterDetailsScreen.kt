package com.example.rickandmorty.presentation.characters.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.databinding.ItemViewEpisodeDetailsRecycleViewBinding
import com.example.rickandmorty.domain.episodes.model.Episodes

class EpisodeAdapterDetailsScreen(
    private val episodes: List<Episodes>,
    private val listener: Listener? = null

) : RecyclerView.Adapter<EpisodeAdapterDetailsScreen.EpisodeViewHolder>() {


    inner class EpisodeViewHolder(private val binding: ItemViewEpisodeDetailsRecycleViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(episode: Episodes) {
            binding.apply {
                episodeName.text = episode.name
                episodeNumber.text = "Номер эпизода - ${episode.episode} -"
                episodeAirDate.text = "Дата релиза - ${episode.air_date} -"

            }

            binding.root.setOnClickListener {
                listener?.onClick(episode)
            }
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EpisodeAdapterDetailsScreen.EpisodeViewHolder {
        val binding = ItemViewEpisodeDetailsRecycleViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return this.EpisodeViewHolder(binding)
    }

    override fun getItemCount(): Int = episodes.size

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        holder.bind(episodes[position])
    }


    interface Listener {
        fun onClick(episode: Episodes)
    }

}
