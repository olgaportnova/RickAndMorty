package com.example.rickandmorty.presentation.characters.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.rickandmorty.data.db.utils.EpisodeConverter
import com.example.rickandmorty.databinding.FragmentCharactersDetailsBinding
import com.example.rickandmorty.domain.characters.model.Characters
import com.example.rickandmorty.domain.episodes.model.Episodes
import com.example.rickandmorty.presentation.characters.adapters.EpisodeAdapterDetailsScreen
import com.example.rickandmorty.presentation.characters.viewmodel.CharactersViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class CharactersDetailsFragment : Fragment() {

    private lateinit var binding: FragmentCharactersDetailsBinding

    private val viewModel: CharactersViewModel by activityViewModel()
    private var listOfEpisodesId = mutableListOf<Int>()

    // нужно будет перенсти в другой слой

    private val episodeConverter = EpisodeConverter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharactersDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // тут реализовать получение списка эпизодов
        val character: Characters? = arguments?.getParcelable(ARG_CHARACTER)
        val episodes = character!!.episode
        val numbersList = episodes.map { url ->
            val parts = url.split("/")
            parts.last().toIntOrNull()
        }


        numbersList.forEach { number ->
            number?.let {
                listOfEpisodesId.add(it)
            }
        }



        lifecycleScope.launch {
            val finalListWithEpisodes = viewModel.getEpisodes(listOfEpisodesId.toList())

            withContext(Dispatchers.Main) {
                val adapter = EpisodeAdapterDetailsScreen(finalListWithEpisodes, object : EpisodeAdapterDetailsScreen.Listener {
                    override fun onClick(episodes: Episodes) {
                        // Обработка клика на элементе списка (если нужно)
                    }
                })
                binding.rvEpisodes.adapter = adapter
            }
        }

        initUI(character)



    }

    private fun initUI(character: Characters) {
        binding.apply {
            Glide.with(imageView)
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

            name.visibility = if (name.text.isEmpty()) View.GONE else View.VISIBLE
            status.visibility = if (status.text.isEmpty()) View.GONE else View.VISIBLE
            species.visibility = if (species.text.isEmpty()) View.GONE else View.VISIBLE
            type.visibility = if (type.text.isEmpty()) View.GONE else View.VISIBLE
            gender.visibility = if (gender.text.isEmpty()) View.GONE else View.VISIBLE
            origin.visibility = if (origin.text.isEmpty()) View.GONE else View.VISIBLE
            location.visibility = if (location.text.isEmpty()) View.GONE else View.VISIBLE


        }
    }


    companion object {
        private const val ARG_CHARACTER = "characterId"

        fun newInstance(character: Characters): CharactersDetailsFragment {
            val args = Bundle()
            args.putParcelable(ARG_CHARACTER, character)
            val fragment = CharactersDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
