package com.alan.app.mvvm.base.core

import io.reactivex.Single
import io.reactivex.disposables.Disposable

interface ViewModelConfiguration {

    fun <T> configThread(single: Single<T>): Single<T>

    fun <T> configComputingThread(single: Single<T>): Single<T>

    fun <T> configThreadAndLoading(single: Single<T>): Single<T>

    fun <T> configComputingThreadAndLoading(single: Single<T>): Single<T>

    fun addDisposable(disposable: Disposable)
}