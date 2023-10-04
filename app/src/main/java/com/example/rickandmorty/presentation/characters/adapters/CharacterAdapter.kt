package com.example.rickandmorty.presentation.characters.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.ItemViewRecycleCharacterBinding
import com.example.rickandmorty.domain.characters.model.Characters


class CharacterAdapter:  PagingDataAdapter<Characters, CharacterAdapter.ViewHolder>(differCallback
)
   {

    private lateinit var binding: ItemViewRecycleCharacterBinding
    private lateinit var context: Context

       var onItemClickListener: ((Characters) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemViewRecycleCharacterBinding.inflate(inflater, parent, false)
        context = parent.context
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    inner class ViewHolder (private val binding: ItemViewRecycleCharacterBinding) : RecyclerView.ViewHolder(binding.root) {

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
                onItemClickListener?.invoke(item)
            }
        }



        fun setOnItemClickListener(listener: (Characters) -> Unit) {
            onItemClickListener = listener
        }
    }




       companion object {
           val differCallback = object : DiffUtil.ItemCallback<Characters>() {
               override fun areItemsTheSame(oldItem: Characters, newItem: Characters): Boolean {
                   return oldItem.id == newItem.id
               }

               override fun areContentsTheSame(oldItem: Characters, newItem: Characters): Boolean {
                   return oldItem == newItem
               }
           }
       }
}
