package com.example.rickandmorty.presentation.episodes.view

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rickandmorty.databinding.FragmentEpisodesDetailsBinding
import com.example.rickandmorty.domain.characters.model.Characters
import com.example.rickandmorty.domain.episodes.model.Episodes
import com.example.rickandmorty.presentation.episodes.adapters.CharacterAdapterDetailsScreen
import com.example.rickandmorty.presentation.episodes.viewmodel.EpisodeViewModel
import com.example.rickandmorty.presentation.main.adapters.GridItemDecorator
import com.example.rickandmorty.presentation.main.view.BaseFragmentDetails
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class EpisodesDetailsFragment :
    BaseFragmentDetails<FragmentEpisodesDetailsBinding, EpisodeViewModel>(
        FragmentEpisodesDetailsBinding::inflate
    ) {

    override val viewModel: EpisodeViewModel by activityViewModel()
    private lateinit var adapter: CharacterAdapterDetailsScreen
    private var episodeId: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        episodeId = arguments?.getInt(ARG_EPISODE_ID)

        viewModel.checkNetworkAvailability(requireContext())
        viewModel.loadEpisode(episodeId)

        adapter =
            CharacterAdapterDetailsScreen(listener = object :
                CharacterAdapterDetailsScreen.Listener {
                override fun onClick(characters: Characters) {
                    val action =
                        EpisodesDetailsFragmentDirections.actionEpisodesDetailsFragmentToCharactersDetailsFragment(
                            characters.id
                        )
                    findNavController().navigate(action)
                }
            })
        binding.rvCharacters.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvCharacters.addItemDecoration(GridItemDecorator(2, 10, 10))
        binding.rvCharacters.adapter = adapter

        setBackButtonClickListener(binding.back)
        setupObservers()
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.charactersSearchResult.collect { charactersList ->
                if (charactersList.isNullOrEmpty()) {
                    binding.rvCharacters.visibility = View.GONE
                    binding.episodesInfo.visibility = View.GONE
                    binding.placeholderNoResidents.visibility = View.VISIBLE
                } else {
                    binding.rvCharacters.visibility = View.VISIBLE
                    binding.episodesInfo.visibility = View.VISIBLE
                    binding.placeholderNoResidents.visibility = View.GONE
                    adapter.submitList(charactersList)
                }
            }
        }
        viewModel.isNetworkAvailable.observe(viewLifecycleOwner) { isNetworkAvailable ->
            handleNetworkVisibility(isNetworkAvailable)
        }
        viewModel.episode.observe(viewLifecycleOwner) { episode ->
            episode?.let {
                initUI(it)
            }
        }
    }

    private fun handleNetworkVisibility(isNetworkAvailable: Boolean) {
        binding.placeholderNoInternet.visibility =
            if (isNetworkAvailable) View.GONE else View.VISIBLE
    }

    private fun initUI(episode: Episodes?) {
        binding.apply {
            name.text = episode?.name
            airDate.text = episode?.air_date
            code.text = episode?.episode
            setVisibility(name, airDate, code)
        }
    }
}
