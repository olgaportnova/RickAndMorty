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
import android.view.MotionEvent
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FragmentCharactersListBinding
import com.example.rickandmorty.presentation.characters.adapters.CharacterAdapter
import com.example.rickandmorty.presentation.characters.viewmodel.CharacterViewModel
import com.example.rickandmorty.presentation.characters.adapters.LoadMoreAdapter
import com.example.rickandmorty.domain.characters.model.utils.Gender
import com.example.rickandmorty.domain.characters.model.utils.Status
import com.example.rickandmorty.presentation.recycleviewList.GridItemDecorator
import com.example.rickandmorty.utils.SearchCategories
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class CharactersListFragment : Fragment() {

    private lateinit var binding: FragmentCharactersListBinding
    private lateinit var characterAdapter: CharacterAdapter
    private val viewModel: CharacterViewModel by activityViewModel()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private var genderState : Gender = Gender.NONE
    private var statusState : Status = Status.NONE
    private val searchCategories = arrayOf(SearchCategories.NAME,SearchCategories.SPECIES,SearchCategories.TYPE)
    private var isSearchCategorySelected: Boolean = false
    private var isSearchFieldEmpty: Boolean = false





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
        observeCharacterList()
    }

    private fun initSpinnerSearchCategories() {
        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.item_spinner_selected, searchCategories)
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown)


        binding.spinnerCategory.adapter = spinnerAdapter
        binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = parent?.getItemAtPosition(position).toString()
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

        binding.inputTextSearch.setOnClickListener {
            clearText()
        }

        binding.btFilter.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }



        binding.btnApply.setOnClickListener {
            viewModel.updateCharactersListWithFilters(genderState, statusState)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            refreshCharacterList()
        }

        binding.btSearch.setOnClickListener {
            val searchText = binding.inputTextSearch.text.toString()
            val selectedCategory = binding.spinnerCategory.selectedItem.toString()
            if (!isSearchCategorySelected || searchText.isEmpty()) {
                Toast.makeText(context, "Set category and type search request",Toast.LENGTH_SHORT). show()
            } else {
            viewModel.updateCharactersListWithSearch(selectedCategory, searchText)
            refreshCharacterList() }
        }


        binding.inputTextSearch.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = binding.inputTextSearch.compoundDrawablesRelative[2]
                if (drawableEnd != null && event.rawX >= binding.inputTextSearch.right - drawableEnd.bounds.width()) {
                    binding.inputTextSearch.text?.clear()
                    viewModel.updateCharactersListWithSearch(null,null)
                    refreshCharacterList()
                }
            }
            false
        }


        binding.btnCancel.setOnClickListener {
            viewModel.updateCharactersListWithFilters(Gender.NONE, Status.NONE)
            binding.rbMale.isChecked=false
            binding.rbFemale.isChecked=false
            binding.rbGenderless.isChecked=false
            binding.rbUnknownGender.isChecked=false
            binding.rbAlive.isChecked=false
            binding.rbDead.isChecked=false
            binding.rbUnknown.isChecked=false
            refreshCharacterList()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }


        binding.rbMale.setOnCheckedChangeListener { _, isChecked ->
           if (isChecked) genderState = Gender.MALE
        }

        binding.rbFemale.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) genderState = Gender.FEMALE
        }

        binding.rbGenderless.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) genderState = Gender.GENDERLESS
        }

        binding.rbUnknownGender.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) genderState = Gender.UNKNOWN
        }

        binding.rbAlive.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) statusState = Status.ALIVE
        }

        binding.rbDead.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) statusState = Status.DEAD
        }

        binding.rbUnknown.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) statusState = Status.UNKNOWN
        }
    }




    private fun observeCharacterList() {
        lifecycleScope.launch {
            viewModel.charactersList.collect {
                characterAdapter.submitData(it)
            }
        }
    }

    private fun refreshCharacterList() {
        lifecycleScope.launch {
            viewModel.charactersList.collect {
                characterAdapter.submitData(it)
            }
        }
    }

    private fun clearText() {
        binding.inputTextSearch.text.clear()
        viewModel.updateCharactersListWithSearch("clear all", null)
    }


}
