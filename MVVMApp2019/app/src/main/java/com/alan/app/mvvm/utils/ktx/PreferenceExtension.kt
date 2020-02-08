package com.alan.app.mvvm.utils.ktx

import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
inline fun <reified T> SharedPreferences.observeKey(key: String, default: T,
                                                    dispatcher: CoroutineContext = Dispatchers.Default): Flow<T> {
    val flow: Flow<T> = channelFlow {
        offer(getByKey(key, default))

        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, k ->
            if (key == k) {
                offer(getByKey(key, default)!!)
            }
        }

        registerOnSharedPreferenceChangeListener(listener)
        awaitClose { unregisterOnSharedPreferenceChangeListener(listener) }
    }
    return flow.flowOn(dispatcher)
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T> SharedPreferences.getByKey(key: String, default: T): T {
    return when (default) {
        is String -> getString(key, default) as T
        is Int -> getInt(key, default) as T
        is Long -> getLong(key, default) as T
        is Boolean -> getBoolean(key, default) as T
        is Float -> getFloat(key, default) as T
        is Set<*> -> getStringSet(key, default as Set<String>) as T
        is MutableSet<*> -> getStringSet(key, default as MutableSet<String>) as T
        else -> throw IllegalArgumentException("generic type not handle ${T::class.java.name}")
    }
}