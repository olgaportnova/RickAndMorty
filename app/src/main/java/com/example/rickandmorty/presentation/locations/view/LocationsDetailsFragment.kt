package com.example.rickandmorty.presentation.locations.view

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rickandmorty.databinding.FragmentLocationsDetailsBinding
import com.example.rickandmorty.domain.characters.model.Characters
import com.example.rickandmorty.domain.locations.model.Locations
import com.example.rickandmorty.presentation.episodes.adapters.CharacterAdapterDetailsScreen
import com.example.rickandmorty.presentation.locations.viewmodel.LocationViewModel
import com.example.rickandmorty.presentation.main.adapters.GridItemDecorator
import com.example.rickandmorty.presentation.main.view.BaseFragmentDetails
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LocationsDetailsFragment :
    BaseFragmentDetails<FragmentLocationsDetailsBinding, LocationViewModel>(
        FragmentLocationsDetailsBinding::inflate
    ) {

    override val viewModel: LocationViewModel by activityViewModel()

    private lateinit var adapter: CharacterAdapterDetailsScreen
    private var locationId: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationId = arguments?.getInt(ARG_LOCATION_ID)

        viewModel.checkNetworkAvailability(requireContext())
        viewModel.loadLocation(locationId)

        adapter =
            CharacterAdapterDetailsScreen(listener = object :
                CharacterAdapterDetailsScreen.Listener {
                override fun onClick(characters: Characters) {
                    val action =
                        LocationsDetailsFragmentDirections.actionLocationsDetailsFragmentToCharactersDetailsFragment(
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
                    binding.residentsInfo.visibility = View.GONE
                    binding.placeholderNoResidents.visibility = View.VISIBLE
                } else {
                    binding.rvCharacters.visibility = View.VISIBLE
                    binding.residentsInfo.visibility = View.VISIBLE
                    binding.placeholderNoResidents.visibility = View.GONE
                    adapter.submitList(charactersList)
                }
            }
        }
        viewModel.isNetworkAvailable.observe(viewLifecycleOwner) { isNetworkAvailable ->
            handleNetworkVisibility(isNetworkAvailable)
        }
        viewModel.location.observe(viewLifecycleOwner) { location ->
            location?.let {
                initUI(it)
            }
        }
    }

    private fun handleNetworkVisibility(isNetworkAvailable: Boolean) {
        binding.placeholderNoInternet.visibility =
            if (isNetworkAvailable) View.GONE else View.VISIBLE
    }

    private fun initUI(location: Locations?) {
        binding.apply {
            name.text = location?.name
            created.text = parseISO8601Date(location?.created).toString()
            type.text = location?.type
            dimension.text = location?.dimension

            setVisibility(name, created, dimension)
        }
    }

    private fun parseISO8601Date(iso8601Date: String?): Date {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        return format.parse(iso8601Date) ?: Date()
    }

}
