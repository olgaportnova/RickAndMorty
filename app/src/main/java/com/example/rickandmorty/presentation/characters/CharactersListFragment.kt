package com.example.rickandmorty.presentation.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FragmentCharactersListBinding
import com.example.rickandmorty.databinding.ItemViewRecycleViewBinding
import com.example.rickandmorty.domain.model.Characters
import com.example.rickandmorty.presentation.recycleviewList.GridItemDecorator
import com.example.rickandmorty.presentation.recycleviewList.ItemBinder
import com.example.rickandmorty.presentation.recycleviewList.ListFragmentAdapter
import com.example.rickandmorty.presentation.recycleviewList.setOnEndOfListReachedListener
import com.example.rickandmorty.utils.Resource
import org.koin.androidx.viewmodel.ext.android.viewModel


class CharactersListFragment : Fragment(R.layout.fragment_characters_list) {

    private val viewModel: CharacterViewModel by viewModel()
    private lateinit var binding: FragmentCharactersListBinding

    private val binder: ItemViewRecycleViewBinding.(Characters) -> Unit = { character ->
        itemHeader.text = character.name
        itemFiled1Value.text = character.species
        itemFiled2Value.text = character.status
        itemFiled3Value.text = character.gender
        Glide.with(root.context)
            .load(character.image)
            .placeholder(R.drawable.ic_characters)
            .into(itemImage)
    }

    private val adapter = ListFragmentAdapter(
        layoutInflater = { inflater, parent, attachToRoot ->
            ItemViewRecycleViewBinding.inflate(inflater, parent, attachToRoot)
        },
        binder = binder
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCharactersListBinding.bind(view)

        binding.recyclerViewItems.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = this@CharactersListFragment.adapter
            setOnEndOfListReachedListener {
                if (viewModel.hasMorePages) {
                    viewModel.fetchCharacters()
                }
            }
        }

        viewModel.characters.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Show loading indicator
                }

                is Resource.Success -> {
                    resource.data?.let { charactersList ->
                        adapter.addNewItems(charactersList)
                    }
                }

                is Resource.Error -> {
                    // Handle error...
                }
            }
        }
        viewModel.fetchCharacters()
    }
}