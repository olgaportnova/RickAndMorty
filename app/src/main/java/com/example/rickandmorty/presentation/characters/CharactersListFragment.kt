package com.example.rickandmorty.presentation.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmorty.databinding.FragmentCharactersListBinding
import com.example.rickandmorty.presentation.recycleviewList.GridItemDecorator
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class CharactersListFragment : Fragment() {

    private lateinit var binding: FragmentCharactersListBinding

    lateinit var characterAdapter: CharacterAdapter
    private val viewModel: CharacterViewModel by activityViewModel()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentCharactersListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            characterAdapter = CharacterAdapter()
            val bottomSheetContainer = binding.standardBottomSheet
            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

            lifecycleScope.launchWhenCreated {
                viewModel.charactersList.collect {
                    characterAdapter.submitData(it)
                }
            }

            binding.btFilter.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }

//            binding.btnApply.setOnClickListener {
//
//            }
//
//            characterAdapter.setOnItemClickListener {
//                val direction = MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(it.id)
//                findNavController().navigate(direction)
//            }

            lifecycleScope.launchWhenCreated {
                characterAdapter.loadStateFlow.collect{
                    val state = it.refresh
                    binding.progressBar.isVisible = state is LoadState.Loading
                }
            }


            recyclerViewItems.apply {
                layoutManager =  GridLayoutManager(requireContext(), 2)
                recyclerViewItems.addItemDecoration(GridItemDecorator(2, 10,10))
                adapter = characterAdapter
            }

            recyclerViewItems.adapter=characterAdapter.withLoadStateFooter(
                LoadMoreAdapter{
                    characterAdapter.retry()
                }
            )

        }
    }

}