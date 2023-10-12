package com.example.rickandmorty.presentation.characters.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FragmentCharactersDetailsBinding
import com.example.rickandmorty.domain.characters.model.Characters
import com.example.rickandmorty.domain.episodes.model.Episodes
import com.example.rickandmorty.presentation.characters.adapters.EpisodeAdapterDetailsScreen
import com.example.rickandmorty.presentation.characters.viewmodel.CharactersViewModel
import com.example.rickandmorty.presentation.main.view.BaseFragmentDetails
import com.example.rickandmorty.utils.event.EventObserver
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class CharactersDetailsFragment :
    BaseFragmentDetails<FragmentCharactersDetailsBinding, CharactersViewModel>(
        FragmentCharactersDetailsBinding::inflate
    ) {
    override val viewModel: CharactersViewModel by activityViewModel()
    private lateinit var adapter: EpisodeAdapterDetailsScreen
    private var characterId: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        characterId = arguments?.getInt(ARG_CHARACTER_ID)

        viewModel.checkNetworkAvailability(requireContext())
        viewModel.loadCharacter(characterId)

        adapter =
            EpisodeAdapterDetailsScreen(listener = object : EpisodeAdapterDetailsScreen.Listener {
                override fun onClick(episode: Episodes) {
                    val action =
                        CharactersDetailsFragmentDirections.actionCharactersDetailsFragmentToEpisodesDetailsFragment(
                            episode.id
                        )
                    findNavController().navigate(action)
                }
            })
        binding.rvEpisodes.layoutManager = LinearLayoutManager(context)
        binding.rvEpisodes.adapter = adapter

        setupOnClickListeners()
        setupObservers()
    }
    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.episodeSearchResult.collect { episodesList ->
                if (episodesList.isNullOrEmpty()) {
                    binding.placeholderNoEpisodes.visibility = View.VISIBLE
                    binding.rvEpisodes.visibility = View.GONE
                } else {
                    adapter.submitList(episodesList)
                    binding.placeholderNoEpisodes.visibility = View.GONE
                    binding.rvEpisodes.visibility = View.VISIBLE
                }
            }
        }
        viewModel.isNetworkAvailable.observe(viewLifecycleOwner) { isNetworkAvailable ->
            handleNetworkVisibility(isNetworkAvailable)
        }
        viewModel.character.observe(viewLifecycleOwner) { character ->
            character?.let {
                initUI(it)
            }
        }
        viewModel.navigateToDetails.observe(viewLifecycleOwner, EventObserver {
            val action = CharactersDetailsFragmentDirections
                .actionCharactersDetailsFragmentToLocationsDetailsFragment(it)
            findNavController().navigate(action)
        })
        viewModel.showToast.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
    }
    private fun handleNetworkVisibility(isNetworkAvailable: Boolean) {
        binding.placeholderNoInternet.visibility =
            if (isNetworkAvailable) View.GONE else View.VISIBLE
    }
    private fun setupOnClickListeners() {
        setBackButtonClickListener(binding.back)

        binding.location.setOnClickListener {
            setOnClickListenerWithNavigation(binding.location, LOCATION)
        }
        binding.origin.setOnClickListener {
            setOnClickListenerWithNavigation(binding.origin, ORIGIN)
        }
    }
    private fun setOnClickListenerWithNavigation(view: View, type: Int) {
        view.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.navigateToDetails(type)
            }
        }
    }

    private fun initUI(character: Characters?) {
        binding.apply {
            Glide.with(this@CharactersDetailsFragment)
                .load(character?.image)
                .placeholder(R.drawable._no_photo)
                .centerCrop()
                .into(imageView)

            name.text = character?.name
            status.text = character?.status
            species.text = character?.species
            type.text = character?.type
            gender.text = character?.gender
            origin.text = character?.origin?.name
            location.text = character?.location?.name

            setVisibility(name, status, species, type, gender, origin, location)
        }
    }

}

