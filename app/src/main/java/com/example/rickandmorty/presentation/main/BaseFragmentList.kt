package com.example.rickandmorty.presentation.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.paging.CombinedLoadStates
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.rickandmorty.R
import com.example.rickandmorty.utils.SearchCategories
import androidx.paging.LoadState


abstract class BaseFragmentList<VB: ViewBinding, VM: ViewModel>(
    private val bindingInflater: (inflater: LayoutInflater) -> VB
): Fragment() {

    lateinit var binding: VB


    protected abstract val viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = bindingInflater.invoke(inflater)
        return binding.root
    }

    protected fun initAdapter(
        recyclerView: RecyclerView,
        adapter: RecyclerView.Adapter<*>,
        spanCount: Int,
        itemDecorator: RecyclerView.ItemDecoration
    ) {
        recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), spanCount)
            addItemDecoration(itemDecorator)
            this.adapter = adapter
        }


    }

    protected fun <T> initSpinnerItemSelectedListener(
        spinner: Spinner,
        adapter: ArrayAdapter<T>,
        onItemSelected: (position: Int) -> Unit
    ) {
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                onItemSelected(position)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    protected fun initSearchButton(btSearch: ImageButton, searchCategories: SearchCategories, inputText: EditText) {
        btSearch.setOnClickListener {
            val searchText = inputText.text.toString().toLowerCase()
            if (searchText.isEmpty()) {
                Toast.makeText(context, R.string.type_search_request, Toast.LENGTH_SHORT).show()
            } else {
                updateListWithSearch(searchText, searchCategories)
            }
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    protected fun initClearButton(inputText: EditText) {
        inputText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = inputText.compoundDrawablesRelative[2]
                if (drawableEnd != null && event.rawX >= inputText.right - drawableEnd.bounds.width()) {
                    inputText.text?.clear()
                    clearTextSearchField()
                }
            }
            false
        }
    }

    protected fun handleLoadState(
        loadState: CombinedLoadStates,
        recyclerView: RecyclerView,
        placeholderView: View,
        progressBar: View
    ) {
        if (loadState.refresh is LoadState.Loading) {
            placeholderView.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE

            if (recyclerView.adapter?.itemCount == 0 && loadState.refresh is LoadState.NotLoading) {
                recyclerView.visibility = View.GONE
                placeholderView.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
                placeholderView.visibility = View.GONE
            }

            // Обработка ошибок загрузки
            val errorState = loadState.refresh as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                showToast("Wooops ${it.error}")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    protected abstract fun updateListWithSearch(searchText: String, searchCategories: SearchCategories)
    protected abstract fun clearTextSearchField()
}
