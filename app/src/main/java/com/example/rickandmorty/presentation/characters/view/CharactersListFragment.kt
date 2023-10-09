package com.example.rickandmorty.presentation.characters.view

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FragmentCharactersListBinding
import com.example.rickandmorty.domain.characters.model.utils.Gender
import com.example.rickandmorty.domain.characters.model.utils.Status
import com.example.rickandmorty.presentation.characters.adapters.CharacterAdapter
import com.example.rickandmorty.presentation.characters.viewmodel.CharactersViewModel
import com.example.rickandmorty.presentation.main.BaseFragmentList
import com.example.rickandmorty.presentation.recycleviewList.GridItemDecorator
import com.example.rickandmorty.utils.SearchCategories
import com.example.rickandmorty.utils.SearchCategoriesCharacters
import com.example.rickandmorty.utils.SearchCategoriesLocations
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class CharactersListFragment : BaseFragmentList<FragmentCharactersListBinding, CharactersViewModel>(
        FragmentCharactersListBinding::inflate
    ) {

    private var characterAdapter = CharacterAdapter()
    override val viewModel: CharactersViewModel by activityViewModel()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private var genderState : Gender = Gender.NONE
    private var statusState : Status = Status.NONE
    private var searchCategory: SearchCategoriesCharacters = SearchCategoriesCharacters.NAME


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        initClickListeners()
        observeData()
        restorePreviousState()

    }
    private fun initUI() {
        binding.placeholder.visibility = View.GONE
        initAdapter(binding.recyclerViewItems, characterAdapter,2,GridItemDecorator(2, 10, 10))
        initSpinnerItemSelectedListener(binding.spinnerCategory,ArrayAdapter(requireContext(), R.layout.item_spinner_selected, SearchCategoriesLocations.values())) { position ->
            searchCategory = SearchCategoriesCharacters.values()[position]
        }
        initBottomSheet()
    }

    override fun updateListWithSearch(searchText: String, searchCategories: SearchCategories) {
        viewModel.updateListWithSearch(searchCategories, searchText)
    }
    override fun clearTextSearchField() {
        viewModel.clearTextSearchField()
    }

    private fun initBottomSheet() {
        val bottomSheetContainer = binding.standardBottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

    }


    private fun initClickListeners() {

        initSearchButton(binding.btSearch,searchCategory,binding.inputTextSearch)
        initClearButton(binding.inputTextSearch)

        characterAdapter.onItemClickListener = { character ->
            val bundle = Bundle().apply {
                putInt(CharactersDetailsFragment.ARG_CHARACTER_ID, character.id) }
            findNavController().navigate(R.id.charactersDetailsFragment, bundle)
        }

        binding.btFilter.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            updateRadioGroupStatusThatLastData(statusState)
            updateRadioGroupGenderThatLastData(genderState)
        }

        binding.btnApply.setOnClickListener {
            viewModel.setGenderState(genderState)
            viewModel.setStatusState(statusState)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.rgGender.setOnCheckedChangeListener { radioGroup, idThatSelected ->
            genderState =
                when (idThatSelected) {
                    binding.rbFemale.id -> Gender.FEMALE
                    binding.rbMale.id -> Gender.MALE
                    binding.rbUnknownGender.id -> Gender.UNKNOWN
                    binding.rbGenderless.id -> Gender.GENDERLESS
                    else -> Gender.NONE
                }
        }

        binding.rgStatus.setOnCheckedChangeListener { radioGroup, idThatSelected ->
            statusState =
                when (idThatSelected) {
                    binding.rbAlive.id -> Status.ALIVE
                    binding.rbDead.id -> Status.DEAD
                    binding.rbUnknown.id -> Status.UNKNOWN
                    else -> Status.NONE
                }
        }

        binding.btnCancel.setOnClickListener {
            viewModel.setStatusState(Status.NONE)
            viewModel.setGenderState(Gender.NONE)
            binding.rgGender.clearCheck()
            binding.rgStatus.clearCheck()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        }

    }
    private fun observeData() {

        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { characterState ->
                if (characterState.isFilter) {
                    binding.btFilter.setBackgroundResource(R.drawable.bt_search_filter_selected)
                } else {
                    binding.btFilter.setBackgroundResource(R.drawable.bt_search_filter_black)
                }
            }
        }

        characterAdapter.addLoadStateListener { loadState ->
            handleLoadState(loadState, binding.recyclerViewItems, binding.placeholder, binding.progressBar)
        }




        lifecycleScope.launch {
            viewModel.getListData().collectLatest {
                characterAdapter.submitData(it)
            }
        }
    }
    private fun restorePreviousState() {
        updateRadioGroupStatusThatLastData(statusState)
        updateRadioGroupGenderThatLastData(genderState)
    }
    private fun updateRadioGroupGenderThatLastData(genderState: Gender) {
        binding.rgGender.check(
            when (genderState) {
                Gender.MALE -> binding.rbMale.id
                Gender.FEMALE -> binding.rbFemale.id
                Gender.GENDERLESS -> binding.rbGenderless.id
                Gender.UNKNOWN -> binding.rbUnknownGender.id
                else -> -1
            }
        )
    }
    private fun updateRadioGroupStatusThatLastData(statusState: Status) {
        binding.rgStatus.check(
            when (statusState) {
                Status.ALIVE -> binding.rbAlive.id
                Status.DEAD -> binding.rbDead.id
                Status.UNKNOWN -> binding.rbUnknown.id
                else -> -1
            }
        )
    }

}

