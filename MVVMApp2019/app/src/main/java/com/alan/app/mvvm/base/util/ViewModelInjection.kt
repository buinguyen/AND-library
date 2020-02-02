package com.alan.app.mvvm.base.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

fun <T : ViewModel> FragmentActivity.injectVM(
    factory: ViewModelProvider.Factory,
    clazz: Class<T>
): T {
    return ViewModelProviders.of(this, factory).get(clazz)
}

fun <T : ViewModel> Fragment.injectVM(factory: ViewModelProvider.Factory, clazz: Class<T>): T {
    return ViewModelProviders.of(this, factory).get(clazz)
}

fun <T : ViewModel> Fragment.injectVMWithActivityScope(
    factory: ViewModelProvider.Factory,
    clazz: Class<T>
): T {
    return ViewModelProviders.of(this.requireActivity(), factory).get(clazz)
}

inline fun <reified T : ViewModel> FragmentActivity.injectVM(factory: ViewModelProvider.Factory): T {
    return ViewModelProviders.of(this, factory).get(T::class.java)
}

inline fun <reified T : ViewModel> Fragment.injectVMWithActivityScope(factory: ViewModelProvider.Factory): T {
    return ViewModelProviders.of(this.requireActivity(), factory).get(T::class.java)
}