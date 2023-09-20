package com.example.rickandmorty.presentation.recycleviewList

import androidx.viewbinding.ViewBinding


interface ItemBinder<T, VB : ViewBinding> {
    fun bind(item: T, binding: VB)
}
