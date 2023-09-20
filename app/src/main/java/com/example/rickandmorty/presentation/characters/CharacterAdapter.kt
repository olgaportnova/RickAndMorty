package com.example.rickandmorty.presentation.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.ItemViewRecycleViewBinding
import com.example.rickandmorty.domain.model.Characters

class CharacterAdapter(
) : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    private var charactersList = mutableListOf<Characters>()

    inner class CharacterViewHolder(private val binding: ItemViewRecycleViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(character: Characters) {
            binding.apply {
                itemHeader.text = character.name
                itemFiled1Value.text = character.species
                itemFiled2Value.text = character.status
                itemFiled3Value.text = character.gender
                Glide.with(root.context)
                    .load(character.image)
                    .placeholder(R.drawable.ic_characters)
                    .into(itemImage)
                 }
            }
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = ItemViewRecycleViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(binding)
    }

    override fun getItemCount(): Int = charactersList.size

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(charactersList[position])
    }

    fun addCharacters(characters: List<Characters>) {
        val positionStart = charactersList.size
        charactersList.addAll(characters)
        notifyItemRangeInserted(positionStart, characters.size)
    }



    interface Listener {
        fun onClick(character: Characters)
    }



}
