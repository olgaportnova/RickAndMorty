package com.example.rickandmorty.presentation.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


abstract class BaseFragmentDetails<VB : ViewBinding, VM : ViewModel >(
    private val bindingInflater: (inflater: LayoutInflater) -> VB
) : Fragment() {

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

    protected fun setVisibility(vararg views: View) {
        views.forEach { view ->
            view.visibility = if ((view as? TextView)?.text.isNullOrEmpty()) View.GONE else View.VISIBLE
        }
    }

    protected fun setBackButtonClickListener(backButton: View) {
        backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
    companion object {
        const val ARG_LOCATION_ID = "locationId"
        const val ARG_EPISODE_ID = "episodeId"
        const val ARG_CHARACTER_ID = "characterId"
        const val LOCATION = 1
        const val ORIGIN = 2
    }
}