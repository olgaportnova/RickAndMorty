package com.example.rickandmorty.presentation.characters.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.ItemViewRecycleCharacterBinding
import com.example.rickandmorty.domain.characters.model.Characters
import com.example.rickandmorty.presentation.main.adapters.BaseAdapter


class CharacterAdapter : BaseAdapter<Characters, CharacterAdapter.ViewHolder>(
    createDefaultDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemViewRecycleCharacterBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun bindViewHolder(holder: ViewHolder, item: Characters) {
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemViewRecycleCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Characters) {
            binding.apply {
                itemHeader.text = item.name
                itemFiled1Value.text = item.species
                itemFiled2Value.text = item.status
                itemFiled3Value.text = item.gender
                Glide.with(root.context)
                    .load(item.image)
                    .placeholder(R.drawable.ic_characters)
                    .into(itemImage)
            }
            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }
}

