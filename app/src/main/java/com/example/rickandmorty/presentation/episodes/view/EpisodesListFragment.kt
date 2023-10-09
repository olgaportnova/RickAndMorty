package com.example.rickandmorty.presentation.episodes.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
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
        initSpinner(binding.spinnerCategory,ArrayAdapter(requireContext(), R.layout.item_spinner_selected, SearchCategoriesEpisodes.values()))
    }

    override fun updateListWithSearch(searchText: String, searchCategories: SearchCategories) {
        viewModel.updateListWithSearch(searchCategories, searchText)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initClickListeners() {

        episodeAdapter.onItemClickListener = { episode ->
            val action = EpisodesListFragmentDirections.actionEpisodesListFragmentToEpisodesDetailsFragment(episode.id)
            findNavController().navigate(action)
        }

        initSearchButton(binding.btSearch,searchCategory,binding.inputTextSearch)



        binding.inputTextSearch.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = binding.inputTextSearch.compoundDrawablesRelative[2]
                if (drawableEnd != null && event.rawX >= binding.inputTextSearch.right - drawableEnd.bounds.width()) {
                    binding.inputTextSearch.text?.clear()
                    viewModel.clearTextSearchField()
                }
            }
            false
        }

        binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                searchCategory = SearchCategoriesEpisodes.values()[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }



    }
    private fun observeData() {


        episodeAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                binding.placeholder.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE

                if (episodeAdapter.itemCount == 0 && loadState.refresh is LoadState.NotLoading) {
                    binding.recyclerViewItems.visibility = View.GONE
                    binding.placeholder.visibility = View.VISIBLE
                } else {
                    binding.recyclerViewItems.visibility = View.VISIBLE
                    binding.placeholder.visibility = View.GONE
                }

                // Обработка ошибок загрузки
                val errorState = loadState.refresh as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    Toast.makeText(
                        context,
                        "\uD83D\uDE28 Wooops ${it.error}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        }




        lifecycleScope.launch {
            viewModel.getListData().collectLatest {
                episodeAdapter.submitData(it)
            }
        }
    }





}

