package com.example.rickandmorty.presentation.locations.view

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FragmentLocationsListBinding
import com.example.rickandmorty.presentation.locations.adapters.LocationAdapter
import com.example.rickandmorty.presentation.locations.viewmodel.LocationViewModel
import com.example.rickandmorty.presentation.main.view.BaseFragmentList
import com.example.rickandmorty.presentation.main.adapters.GridItemDecorator
import com.example.rickandmorty.utils.SearchCategories
import com.example.rickandmorty.utils.SearchCategoriesLocations
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class LocationsListFragment : BaseFragmentList<FragmentLocationsListBinding, LocationViewModel>(
    FragmentLocationsListBinding::inflate
) {
    private var locationsAdapter = LocationAdapter()
    override val viewModel: LocationViewModel by activityViewModel()
    private var searchCategory: SearchCategoriesLocations = SearchCategoriesLocations.NAME

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        firstLaunch()
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initClickListeners()
        observeData()
    }

    private fun initUI() {
        binding.placeholder.visibility = View.GONE
        initAdapter(binding.recyclerViewItems, locationsAdapter, 2, GridItemDecorator(2, 10, 10))
        initSpinnerItemSelectedListener(
            binding.spinnerCategory,
            ArrayAdapter(
                requireContext(),
                R.layout.item_spinner_selected,
                SearchCategoriesLocations.values()
            )
        ) { position ->
            searchCategory = SearchCategoriesLocations.values()[position]
        }
        setupSwipeToRefresh(binding.swipeRefreshLayout) {
            observeData()
        }
    }

    private fun firstLaunch() {
        clearTextSearchField()
    }

    private fun initClickListeners() {
        locationsAdapter.onItemClickListener = { episode ->
            val action =
                LocationsListFragmentDirections.actionLocationsListFragment2ToLocationsDetailsFragment(
                    episode.id
                )
            findNavController().navigate(action)
        }
        initSearchButton(binding.btSearch, searchCategory, binding.inputTextSearch)
        initClearButton(binding.inputTextSearch)
    }

    private fun observeData() {
        locationsAdapter.addLoadStateListener { loadState ->
            handleLoadState(
                loadState,
                binding.recyclerViewItems,
                binding.placeholder,
                binding.progressBar
            )
        }
        observeAndSubmitData(viewModel.getListData(), locationsAdapter)
    }

    override fun updateListWithSearch(searchText: String, searchCategories: SearchCategories) {
        viewModel.updateListWithSearch(searchCategories, searchText)
    }

    override fun clearTextSearchField() {
        viewModel.clearTextSearchField()
    }

}

