package com.example.rickandmorty.presentation.characters.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.rickandmorty.databinding.FragmentCharactersDetailsBinding
import com.example.rickandmorty.domain.characters.model.Characters
import com.example.rickandmorty.domain.episodes.model.Episodes
import com.example.rickandmorty.presentation.characters.adapters.EpisodeAdapterDetailsScreen
import com.example.rickandmorty.presentation.characters.viewmodel.CharactersViewModel
import com.example.rickandmorty.presentation.episodes.viewmodel.EpisodeViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class CharactersDetailsFragment : Fragment() {

    private lateinit var binding: FragmentCharactersDetailsBinding
    private val viewModelCharacter: CharactersViewModel by activityViewModel()
    private val viewModelEpisode: EpisodeViewModel by activityViewModel()

    private lateinit var adapter: EpisodeAdapterDetailsScreen
    private var characterId: Int? = null
    private var character: Characters? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharactersDetailsBinding.inflate(inflater, container, false)
        characterId = arguments?.getInt(ARG_CHARACTER_ID)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        lifecycleScope.launch {
            character = viewModelCharacter.getCharacter(characterId!!)
            character?.let {
                val listOfEpisodesId = it.episode.mapNotNull { url ->
                    url.split("/").last().toIntOrNull()
                }
                viewModelEpisode.getMultipleEpisodes(listOfEpisodesId)

                // После получения данных вызываем initUI
                initUI(character)
            }
        }

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
        lifecycleScope.launchWhenStarted {
            viewModelEpisode.episodeSearchResult.collect { episodesList ->
                adapter.submitList(episodesList)
            }
        }
    }

    private fun setupOnClickListeners() {

        binding.back.setOnClickListener {
            requireActivity().onBackPressed()

        }
    }


    private fun initUI(character: Characters?) {
        binding.apply {
            Glide.with(this@CharactersDetailsFragment)
                .load(character?.image)
                .centerCrop()
                .into(imageView)

            name.text = character?.name
            status.text = character?.status
            species.text = character?.species
            type.text = character?.type
            gender.text = character?.gender
            origin.text = character?.origin?.name
            location.text = character?.location?.name

            setVisibility(name)
            setVisibility(status)
            setVisibility(species)
            setVisibility(type)
            setVisibility(gender)
            setVisibility(origin)
            setVisibility(location)
        }
    }

    private fun setVisibility(view: View) {
        view.visibility = if ((view as? TextView)?.text.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    companion object {
        const val ARG_CHARACTER_ID = "characterId"

        fun newInstance(characterId: Int): CharactersDetailsFragment {
            return CharactersDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_CHARACTER_ID, characterId)
                }
            }
        }
    }


}

