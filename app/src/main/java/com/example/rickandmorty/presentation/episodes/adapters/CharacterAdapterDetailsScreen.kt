package com.example.rickandmorty.presentation.episodes.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.ItemViewEpisodeDetailsRecycleViewBinding
import com.example.rickandmorty.databinding.ItemViewRecycleCharacterBinding
import com.example.rickandmorty.domain.characters.model.Characters
import com.example.rickandmorty.domain.episodes.model.Episodes
import com.example.rickandmorty.presentation.characters.adapters.EpisodeAdapterDetailsScreen

class CharacterAdapterDetailsScreen(
    private val listener: Listener? = null

) :  ListAdapter<Characters, CharacterAdapterDetailsScreen.CharacterViewHolder>(
    CharacterDiffCallback()
) {


    inner class CharacterViewHolder(private val binding: ItemViewRecycleCharacterBinding) :
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

            binding.root.setOnClickListener {
                listener?.onClick(character)
            }
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CharacterViewHolder {
        val binding = ItemViewRecycleCharacterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return this.CharacterViewHolder(binding)
    }


    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    interface Listener {
        fun onClick(character: Characters)
    }

    private class CharacterDiffCallback : DiffUtil.ItemCallback<Characters>() {
        override fun areItemsTheSame(oldItem: Characters, newItem:Characters): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Characters, newItem: Characters): Boolean {
            return oldItem == newItem
        }
    }

}
