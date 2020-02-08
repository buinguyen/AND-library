package com.alan.app.mvvm.utils.ktx

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.alan.app.mvvm.base.BaseViewModel

fun <T : BaseViewModel> FragmentActivity.injectVM(factory: ViewModelProvider.Factory, clazz: Class<T>): T {
    return ViewModelProviders.of(this, factory).get(clazz)
}

fun <T : BaseViewModel> Fragment.injectVM(factory: ViewModelProvider.Factory, clazz: Class<T>): T {
    return ViewModelProviders.of(this, factory).get(clazz)
}

fun <T : BaseViewModel> Fragment.injectVMWithActivityScope(factory: ViewModelProvider.Factory, clazz: Class<T>): T {
    return ViewModelProviders.of(this.requireActivity(), factory).get(clazz)
}

inline fun <reified T : BaseViewModel> FragmentActivity.injectVM(factory: ViewModelProvider.Factory): T {
    return ViewModelProviders.of(this, factory).get(T::class.java)
}

inline fun <reified T : BaseViewModel> Fragment.injectVMWithActivityScope(factory: ViewModelProvider.Factory): T {
    return ViewModelProviders.of(this.requireActivity(), factory).get(T::class.java)
}