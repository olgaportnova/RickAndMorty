package com.example.rickandmorty.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.rickandmorty.R

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

    protected fun initSpinner(spinner: Spinner, adapter: ArrayAdapter<*>?) {
        adapter?.setDropDownViewResource(R.layout.item_spinner_dropdown)
        spinner.adapter = adapter
    }

}
