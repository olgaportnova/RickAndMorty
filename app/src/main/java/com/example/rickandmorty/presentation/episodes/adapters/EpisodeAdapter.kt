package com.example.rickandmorty.presentation.episodes.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.databinding.ItemViewRecycleEpisodeBinding
import com.example.rickandmorty.domain.episodes.model.Episodes


class EpisodeAdapter:  PagingDataAdapter<Episodes, EpisodeAdapter.ViewHolder>(differCallback) {

    private lateinit var binding: ItemViewRecycleEpisodeBinding
    private lateinit var context: Context

    var onItemClickListener: ((Episodes) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemViewRecycleEpisodeBinding.inflate(inflater, parent, false)
        context = parent.context
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
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
                onItemClickListener?.invoke(item)
            }
        }

    }

    companion object {
        val differCallback = object : DiffUtil.ItemCallback<Episodes>() {
            override fun areItemsTheSame(oldItem: Episodes, newItem: Episodes): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Episodes, newItem: Episodes): Boolean {
                return oldItem == newItem
            }
        }
    }

}

