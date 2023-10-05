package com.example.rickandmorty.presentation.characters.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.rickandmorty.databinding.FragmentCharactersDetailsBinding
import com.example.rickandmorty.domain.characters.model.Characters
import com.example.rickandmorty.domain.episodes.model.Episodes
import com.example.rickandmorty.presentation.characters.adapters.EpisodeAdapterDetailsScreen
import com.example.rickandmorty.presentation.characters.viewmodel.CharactersViewModel
import com.example.rickandmorty.presentation.episodes.view.EpisodesListFragmentDirections
import com.example.rickandmorty.presentation.episodes.viewmodel.EpisodeViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class CharactersDetailsFragment : Fragment() {

    private lateinit var binding: FragmentCharactersDetailsBinding
    private val viewModelCharacter: CharactersViewModel by activityViewModel()
    private val viewModelEpisode: EpisodeViewModel by activityViewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharactersDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val character: Characters = arguments?.getParcelable("character") ?: return


        character?.let {
            val listOfEpisodesId = it.episode.mapNotNull { url ->
                url.split("/").last().toIntOrNull()
            }

            lifecycleScope.launch {
                val finalListWithEpisodes = viewModelEpisode.getMultipleEpisodes(listOfEpisodesId) ?: emptyList()
                val adapter = EpisodeAdapterDetailsScreen(finalListWithEpisodes, object : EpisodeAdapterDetailsScreen.Listener {
                    override fun onClick(episode: Episodes) {
                        val action = CharactersDetailsFragmentDirections.actionCharactersDetailsFragmentToEpisodesDetailsFragment(episode)
                        findNavController().navigate(action)
                    }
                })
                binding.rvEpisodes.layoutManager = LinearLayoutManager(context)
                binding.rvEpisodes.adapter = adapter

            }

            initUI(it)
            setupOnClickListeners()
        }
    }

    private fun setupOnClickListeners() {

        binding.back.setOnClickListener {
            requireActivity().onBackPressed()

        }
    }


    private fun initUI(character: Characters) {
        binding.apply {
            Glide.with(this@CharactersDetailsFragment)
                .load(character.image)
                .centerCrop()
                .into(imageView)

            name.text = character.name
            status.text = character.status
            species.text = character.species
            type.text = character.type
            gender.text = character.gender
            origin.text = character.origin?.name
            location.text = character.location?.name

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
        private const val ARG_CHARACTER = "characterId"

        fun newInstance(character: Characters): CharactersDetailsFragment {
            return CharactersDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_CHARACTER, character)
                }
            }
        }
    }
}
