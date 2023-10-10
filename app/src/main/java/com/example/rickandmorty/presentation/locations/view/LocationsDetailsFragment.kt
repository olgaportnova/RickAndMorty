package com.example.rickandmorty.presentation.locations.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rickandmorty.databinding.FragmentLocationsDetailsBinding
import com.example.rickandmorty.domain.characters.model.Characters
import com.example.rickandmorty.domain.locations.model.Locations
import com.example.rickandmorty.presentation.characters.viewmodel.CharactersViewModel
import com.example.rickandmorty.presentation.episodes.adapters.CharacterAdapterDetailsScreen
import com.example.rickandmorty.presentation.locations.viewmodel.LocationViewModel
import com.example.rickandmorty.presentation.main.adapters.GridItemDecorator
import com.example.rickandmorty.utils.NetworkUtils
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class LocationsDetailsFragment : Fragment() {

    private lateinit var binding: FragmentLocationsDetailsBinding
    private val viewModelLocations: LocationViewModel by activityViewModel()
    private val viewModelCharacters: CharactersViewModel by activityViewModel()

    private lateinit var adapter: CharacterAdapterDetailsScreen
    private var locationId: Int? = null
    private var location: Locations? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocationsDetailsBinding.inflate(inflater, container, false)
        locationId = arguments?.getInt(ARG_LOCATION_ID)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (NetworkUtils.isNetworkAvailable(requireContext())) {
            binding.placeholderNoInternet.visibility = View.GONE
            lifecycleScope.launch {
                location = viewModelLocations.getLocation(locationId!!)
                location?.let {
                    val listOfCharactersId = it.residents.mapNotNull { url ->
                        url.split("/").last().toIntOrNull()
                    }
                    viewModelCharacters.getMultipleCharacters(listOfCharactersId)
                    initUI(location)
                }
            }
        }
        else {
                lifecycleScope.launch {
                    binding.placeholderNoInternet.visibility = View.VISIBLE
                    location = viewModelLocations.getLocationFromDb(locationId!!)
                    initUI(location)
                    location?.let {
                        val listOfCharactersId = it.residents.mapNotNull { url ->
                            url.split("/").last().toIntOrNull()
                        }
                        viewModelCharacters.getMultipleCharactersFromDb(listOfCharactersId)
                    }
                }
            }

        adapter =
            CharacterAdapterDetailsScreen(listener = object : CharacterAdapterDetailsScreen.Listener {
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

        setupOnClickListeners()
        setupObservers()
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenStarted {
            viewModelCharacters.charactersSearchResult.collect { charactersList ->
                adapter.submitList(charactersList)
            }
        }
    }


    private fun setupOnClickListeners() {
        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }


    private fun initUI(location: Locations?) {
        binding.apply {

            name.text = location?.name
            created.text = parseISO8601Date(location?.created).toString()
            type.text = location?.type
            dimension.text = location?.dimension

            setVisibility(name)
            setVisibility(created)
            setVisibility(dimension)
        }
    }



    private fun parseISO8601Date(iso8601Date: String?): Date {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        return format.parse(iso8601Date) ?: Date()
    }


    private fun setVisibility(view: View) {
        view.visibility = if ((view as? TextView)?.text.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    companion object {
        private const val ARG_LOCATION_ID = "locationId"

        fun newInstance(locationId: Int): LocationsDetailsFragment  {
            return LocationsDetailsFragment ().apply {
                arguments = Bundle().apply {
                    putInt(ARG_LOCATION_ID, locationId)
                }
            }
        }
    }
}
