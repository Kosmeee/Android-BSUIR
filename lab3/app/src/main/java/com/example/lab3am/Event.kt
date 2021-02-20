package com.example.lab3am

open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set
    fun getContentIfNotHandledOrReturnNull(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): T = content
}