package com.example.rickandmorty.utils.event

open class Event<out T>(private val content: T) {

    private var hasBeenHandled = false
        private set
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
}

