package com.example.rickandmorty.presentation.episodes.view

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FragmentEpisodesListBinding
import com.example.rickandmorty.presentation.episodes.adapters.EpisodeAdapter
import com.example.rickandmorty.presentation.episodes.viewmodel.EpisodeViewModel
import com.example.rickandmorty.presentation.main.BaseFragmentList
import com.example.rickandmorty.presentation.recycleviewList.GridItemDecorator
import com.example.rickandmorty.utils.SearchCategories
import com.example.rickandmorty.utils.SearchCategoriesEpisodes
import com.example.rickandmorty.utils.SearchCategoriesLocations
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class EpisodesListFragment : BaseFragmentList<FragmentEpisodesListBinding, EpisodeViewModel>(
        FragmentEpisodesListBinding::inflate
    ) {

    private val episodeAdapter = EpisodeAdapter()
    override val viewModel: EpisodeViewModel by activityViewModel()
    private var searchCategory: SearchCategoriesEpisodes = SearchCategoriesEpisodes.NAME


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        initClickListeners()
        observeData()

    }
    private fun initUI() {
        binding.placeholder.visibility = View.GONE
        initAdapter(binding.recyclerViewItems, episodeAdapter,2,GridItemDecorator(2, 10, 10))
        initSpinnerItemSelectedListener(binding.spinnerCategory,ArrayAdapter(requireContext(), R.layout.item_spinner_selected, SearchCategoriesLocations.values())) { position ->
            searchCategory = SearchCategoriesEpisodes.values()[position]
        }
    }

    override fun updateListWithSearch(searchText: String, searchCategories: SearchCategories) {
        viewModel.updateListWithSearch(searchCategories, searchText)
    }
    override fun clearTextSearchField() {
        viewModel.clearTextSearchField()
    }

    private fun initClickListeners() {

        episodeAdapter.onItemClickListener = { episode ->
            val action = EpisodesListFragmentDirections.actionEpisodesListFragmentToEpisodesDetailsFragment(episode.id)
            findNavController().navigate(action)
        }

        initSearchButton(binding.btSearch,searchCategory,binding.inputTextSearch)
        initClearButton(binding.inputTextSearch)


    }
    private fun observeData() {


        episodeAdapter.addLoadStateListener { loadState ->
            handleLoadState(loadState, binding.recyclerViewItems, binding.placeholder, binding.progressBar)
        }




        lifecycleScope.launch {
            viewModel.getListData().collectLatest {
                episodeAdapter.submitData(it)
            }
        }
    }





}

