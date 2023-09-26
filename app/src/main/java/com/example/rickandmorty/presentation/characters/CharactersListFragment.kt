package com.example.rickandmorty.presentation.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rickandmorty.databinding.FragmentCharactersListBinding
import com.example.rickandmorty.presentation.characters.utils.Gender
import com.example.rickandmorty.presentation.characters.utils.Status
import com.example.rickandmorty.presentation.recycleviewList.GridItemDecorator
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
        observeCharacterList()
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

    private fun initClickListeners() {
        binding.btFilter.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }



        binding.btnApply.setOnClickListener {
            viewModel.updateCharactersListWithFilters(genderState, statusState)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            refreshCharacterList()
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




    private fun updateStatusState(selectedStatus: Status?) {
        statusState = selectedStatus ?: Status.NONE
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
}
