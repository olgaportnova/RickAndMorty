package com.example.rickandmorty.presentation.characters.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FragmentCharactersListBinding
import com.example.rickandmorty.domain.characters.model.utils.Gender
import com.example.rickandmorty.domain.characters.model.utils.Status
import com.example.rickandmorty.presentation.characters.adapters.CharacterAdapter
import com.example.rickandmorty.presentation.characters.adapters.LoadMoreAdapter
import com.example.rickandmorty.presentation.characters.viewmodel.CharactersViewModel
import com.example.rickandmorty.presentation.recycleviewList.GridItemDecorator
import com.example.rickandmorty.presentation.characters.utils.SearchCategories
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class CharactersListFragment : Fragment() {

    private lateinit var binding: FragmentCharactersListBinding
    private lateinit var characterAdapter: CharacterAdapter
    private val viewModel: CharactersViewModel by activityViewModel()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private var genderState : Gender = Gender.NONE
    private var statusState : Status = Status.NONE
    private var searchCategory: SearchCategories = SearchCategories.NAME


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharactersListBinding.inflate(layoutInflater, container, false)
        binding.placeholder.visibility = View.GONE
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        initClickListeners()
        observeData()
        restorePreviousState()

    }
    private fun initUI() {
        initAdapter()
        initBottomSheet()
        initSpinner()
    }
    private fun initAdapter() {
        characterAdapter = CharacterAdapter()
        binding.recyclerViewItems.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            addItemDecoration(GridItemDecorator(2, 10, 10))
            adapter =
                characterAdapter.withLoadStateFooter(LoadMoreAdapter { characterAdapter.retry() })


        }
    }
    private fun initBottomSheet() {
        val bottomSheetContainer = binding.standardBottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

    }
    private fun initSpinner() {
        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.item_spinner_selected, SearchCategories.values())
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
        binding.spinnerCategory.adapter = spinnerAdapter
    }
    @SuppressLint("ClickableViewAccessibility")
    private fun initClickListeners() {

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
                    viewModel.сlearTextSearchField()
                }
            }
            false
        }

        binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                searchCategory = SearchCategories.values()[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
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
            if (loadState.refresh is LoadState.Loading) {
                binding.placeholder.visibility = View.GONE
                 binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE

                if (characterAdapter.itemCount == 0 && loadState.refresh is LoadState.NotLoading) {
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

