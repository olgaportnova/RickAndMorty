package com.example.rickandmorty.presentation.characters.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import android.annotation.SuppressLint
import android.util.Log
import android.view.MotionEvent
import androidx.navigation.fragment.findNavController
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FragmentCharactersListBinding
import com.example.rickandmorty.domain.characters.model.Characters
import com.example.rickandmorty.presentation.characters.adapters.CharacterAdapter
import com.example.rickandmorty.presentation.characters.adapters.LoadMoreAdapter
import com.example.rickandmorty.domain.characters.model.utils.Gender
import com.example.rickandmorty.domain.characters.model.utils.Status
import com.example.rickandmorty.presentation.characters.viewmodel.CharactersViewModel
import com.example.rickandmorty.presentation.recycleviewList.GridItemDecorator
import com.example.rickandmorty.utils.SearchCategories
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class CharactersListFragment : Fragment() {

    private lateinit var binding: FragmentCharactersListBinding
    private lateinit var characterAdapter: CharacterAdapter
    private val viewModel: CharactersViewModel by activityViewModel()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private var genderState : Gender = Gender.NONE
    private var statusState : Status = Status.NONE
    private val searchCategories = arrayOf(SearchCategories.NAME,SearchCategories.SPECIES,SearchCategories.TYPE)
    private var isSearchCategorySelected: Boolean = false



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharactersListBinding.inflate(layoutInflater, container, false)
        return binding.root



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initClickListeners()
        initSpinnerSearchCategories()


        getListData()



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

    private fun getListData() {
        lifecycleScope.launch {
            viewModel.getListData().collectLatest {
                characterAdapter.submitData(it)
            }
        }
    }

    private fun initSpinnerSearchCategories() {
        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.item_spinner_selected, searchCategories)
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown)


        binding.spinnerCategory.adapter = spinnerAdapter
        binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                isSearchCategorySelected = true
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                isSearchCategorySelected = false
            }
        }


    }


    private fun initUI() {
        characterAdapter = CharacterAdapter()
        val bottomSheetContainer = binding.standardBottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        binding.recyclerViewItems.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            addItemDecoration(GridItemDecorator(2, 10, 10))
            adapter = characterAdapter
        }

        binding.recyclerViewItems.adapter = characterAdapter.withLoadStateFooter(
            LoadMoreAdapter {
                characterAdapter.retry()
            }
        )

    }


    @SuppressLint("ClickableViewAccessibility")
    private fun initClickListeners() {



        binding.btFilter.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            updateRadioGroupStatusThatLastData(statusState)
            updateRadioGroupGenderThatLastData(genderState)
        }



        binding.btnApply.setOnClickListener {
            var a = genderState
            var b = statusState
            viewModel.setGenderState(genderState)
            viewModel.setStatusState(statusState)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        }

        binding.btSearch.setOnClickListener {
            val searchText = binding.inputTextSearch.text.toString()
            val selectedCategory = binding.spinnerCategory.selectedItem.toString()
            if (!isSearchCategorySelected || searchText.isEmpty()) {
                Toast.makeText(context, "Set category and type search request",Toast.LENGTH_SHORT). show()
            } else {
  //          viewModel.updateCharactersListWithSearch(selectedCategory, searchText)
            }
        }


        binding.inputTextSearch.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = binding.inputTextSearch.compoundDrawablesRelative[2]
                if (drawableEnd != null && event.rawX >= binding.inputTextSearch.right - drawableEnd.bounds.width()) {
                    binding.inputTextSearch.text?.clear()
   //                 viewModel.updateCharactersListWithSearch(null,null)
                }
            }
            false
        }







    }







}

