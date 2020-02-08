package com.alan.app.mvvm.utils

import java.util.concurrent.atomic.AtomicBoolean

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 */
open class SingleEventWrapper<out T>(private val data: T) {

    private val hasBeenHandled = AtomicBoolean(false)

    /**
     * Returns the data and prevents its use again.
     */
    fun getDataIfNotHandled(): T? {
        if (hasBeenHandled.get()) {
            return null
        }
        hasBeenHandled.set(true)
        return data
    }

    /**
     * Returns the data, even if it's already been handled.
     */
    fun getData(): T = data
}