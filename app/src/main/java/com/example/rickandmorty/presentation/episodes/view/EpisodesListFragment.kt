package com.example.rickandmorty.presentation.episodes.view

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
import com.example.rickandmorty.presentation.characters.adapters.LoadMoreAdapter
import com.example.rickandmorty.presentation.episodes.adapters.EpisodeAdapter
import com.example.rickandmorty.presentation.episodes.utils.SearchCategoriesEpisodes
import com.example.rickandmorty.presentation.episodes.viewmodel.EpisodeViewModel
import com.example.rickandmorty.presentation.recycleviewList.GridItemDecorator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class EpisodesListFragment : Fragment() {

    private lateinit var binding: FragmentEpisodesListBinding
    private lateinit var episodeAdapter: EpisodeAdapter
    private val viewModel: EpisodeViewModel by activityViewModel()
    private var searchCategory: SearchCategoriesEpisodes = SearchCategoriesEpisodes.NAME


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEpisodesListBinding.inflate(layoutInflater, container, false)
        binding.placeholder.visibility = View.GONE
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        initClickListeners()
        observeData()

    }
    private fun initUI() {
        initAdapter()
        initSpinner()
    }
    private fun initAdapter() {
        episodeAdapter = EpisodeAdapter()



     episodeAdapter.onItemClickListener = { episode ->

            val action = EpisodesListFragmentDirections.actionEpisodesListFragmentToEpisodesDetailsFragment(episode.id)
            findNavController().navigate(action)



        }
        binding.recyclerViewItems.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            addItemDecoration(GridItemDecorator(2, 10, 10))
            adapter =
                episodeAdapter.withLoadStateFooter(LoadMoreAdapter { episodeAdapter.retry() })



        }
    }

    private fun initSpinner() {
        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.item_spinner_selected, SearchCategoriesEpisodes.values())
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
        binding.spinnerCategory.adapter = spinnerAdapter
    }
    @SuppressLint("ClickableViewAccessibility")
    private fun initClickListeners() {



        binding.btSearch.setOnClickListener {
            val searchText = binding.inputTextSearch.text.toString().toLowerCase()
            if (searchText.isEmpty()) {
                Toast.makeText(context, "Type search request",Toast.LENGTH_SHORT). show()
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

