package com.example.rickandmorty.presentation.episodes.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rickandmorty.databinding.FragmentEpisodesDetailsBinding
import com.example.rickandmorty.domain.characters.model.Characters
import com.example.rickandmorty.domain.episodes.model.Episodes
import com.example.rickandmorty.presentation.characters.viewmodel.CharactersViewModel
import com.example.rickandmorty.presentation.episodes.adapters.CharacterAdapterDetailsScreen
import com.example.rickandmorty.presentation.episodes.viewmodel.EpisodeViewModel
import com.example.rickandmorty.presentation.recycleviewList.GridItemDecorator
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class EpisodesDetailsFragment : Fragment() {

    private lateinit var binding: FragmentEpisodesDetailsBinding
    private val viewModel: EpisodeViewModel by activityViewModel()
    private val viewModelCharacters: CharactersViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEpisodesDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val episode: Episodes = arguments?.getParcelable(ARG_EPISODE) ?: return


        episode?.let {
            val listOfCharactersId = it.characters.mapNotNull { url ->
                url.split("/").last().toIntOrNull()
            }
            Log.d("TAG123", "listOfEpisodesId $listOfCharactersId")

                lifecycleScope.launch {
                val finalListWithCharacters = viewModelCharacters.getCharacters(listOfCharactersId)

                    Log.d("TAG123", "finalListWithCharacters $finalListWithCharacters")
                val adapter = CharacterAdapterDetailsScreen(finalListWithCharacters, object : CharacterAdapterDetailsScreen.Listener {
                    override fun onClick(characters: Characters) {
                        // Обработка клика на элементе списка (если нужно)
                    }
                })
                binding.rvCharacters.layoutManager = GridLayoutManager(requireContext(), 2)
                binding.rvCharacters.addItemDecoration(GridItemDecorator(2, 10, 10))
                binding.rvCharacters.adapter = adapter

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


    private fun initUI(episode: Episodes) {
        binding.apply {

            name.text = episode.name
            airDate.text = episode.air_date
            code.text = episode.episode

            setVisibility(name)
            setVisibility(airDate)
            setVisibility(code)
        }
    }

    private fun setVisibility(view: View) {
        view.visibility = if ((view as? TextView)?.text.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    companion object {
        private const val ARG_EPISODE = "episode"

        fun newInstance(episode: Episodes): EpisodesDetailsFragment  {
            return EpisodesDetailsFragment ().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_EPISODE, episode)
                }
            }
        }
    }
}
