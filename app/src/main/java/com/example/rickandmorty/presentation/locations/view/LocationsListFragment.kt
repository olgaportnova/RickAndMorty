package com.example.rickandmorty.presentation.locations.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FragmentEpisodesListBinding
import com.example.rickandmorty.databinding.FragmentLocationsListBinding
import com.example.rickandmorty.presentation.characters.adapters.LoadMoreAdapter
import com.example.rickandmorty.presentation.episodes.adapters.EpisodeAdapter
import com.example.rickandmorty.presentation.episodes.utils.SearchCategoriesEpisodes
import com.example.rickandmorty.presentation.episodes.view.EpisodesListFragmentDirections
import com.example.rickandmorty.presentation.episodes.viewmodel.EpisodeViewModel
import com.example.rickandmorty.presentation.locations.adapters.LocationAdapter
import com.example.rickandmorty.presentation.locations.utils.SearchCategoriesLocations
import com.example.rickandmorty.presentation.locations.viewmodel.LocationViewModel
import com.example.rickandmorty.presentation.main.BaseFragmentList
import com.example.rickandmorty.presentation.recycleviewList.GridItemDecorator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class LocationsListFragment : BaseFragmentList<FragmentLocationsListBinding, LocationViewModel>(
    FragmentLocationsListBinding::inflate
) {
    private var locationsAdapter = LocationAdapter()
    override val viewModel: LocationViewModel by activityViewModel()
    private var searchCategory: SearchCategoriesLocations = SearchCategoriesLocations.NAME

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        initClickListeners()
        observeData()

    }
    private fun initUI() {
        binding.placeholder.visibility = View.GONE
        initAdapter(binding.recyclerViewItems, locationsAdapter,2,GridItemDecorator(2, 10, 10))
        initSpinner(binding.spinnerCategory,ArrayAdapter(requireContext(), R.layout.item_spinner_selected, SearchCategoriesLocations.values()))
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initClickListeners() {

        locationsAdapter.onItemClickListener = { episode ->
            val action = LocationsListFragmentDirections.actionLocationsListFragment2ToLocationsDetailsFragment(episode.id)
            findNavController().navigate(action)
        }

        binding.btSearch.setOnClickListener {
            val searchText = binding.inputTextSearch.text.toString().toLowerCase()
            if (searchText.isEmpty()) {
                Toast.makeText(context, "Type search request", Toast.LENGTH_SHORT). show()
            } else {
                viewModel.updateCharactersListWithSearch(searchCategory, searchText)
            }
        }

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
                searchCategory = SearchCategoriesLocations.values()[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }



    }
    private fun observeData() {

        locationsAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                binding.placeholder.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE

                if (locationsAdapter.itemCount == 0 && loadState.refresh is LoadState.NotLoading) {
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
                locationsAdapter.submitData(it)
            }
        }
    }

}

