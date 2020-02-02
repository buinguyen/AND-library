package com.alan.app.mvvm.base

import android.app.Application
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.alan.app.mvvm.base.core.ViewModelConfiguration
import com.alan.app.mvvm.data.RestApiUtil
import com.alan.app.mvvm.data.model.AlertData
import com.alan.app.mvvm.data.model.LoadingStatus
import com.alan.app.mvvm.utils.SchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel(private val applicaton: Application,
                             private val schedulerProvider: SchedulerProvider
) : AndroidViewModel(applicaton), ViewModelConfiguration {

    private val disposables = CompositeDisposable()

    @JvmField
    val loadingLV = MutableLiveData<LoadingStatus>()

    @JvmField
    val errorLV = MutableLiveData<AlertData>()

    protected fun getContext() = getApplication<Application>()

    override fun <T> configThread(single: Single<T>): Single<T> {
        return single.subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
    }

    override fun <T> configComputingThread(single: Single<T>): Single<T> {
        return single.subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
    }

    override fun <T> configThreadAndLoading(single: Single<T>): Single<T> {
        return configThread(single)
                .doOnSubscribe { loadingLV.value = LoadingStatus.SHOW_LOADING }
                .doAfterTerminate { loadingLV.value = LoadingStatus.HIDE_LOADING }
    }

    override fun <T> configComputingThreadAndLoading(single: Single<T>): Single<T> {
        return configComputingThread(single)
                .doOnSubscribe { loadingLV.value = LoadingStatus.SHOW_LOADING }
                .doAfterTerminate { loadingLV.value = LoadingStatus.HIDE_LOADING }
    }

    override fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    protected fun showError(title: String? = null, message: String? = null) {
        errorLV.value = (AlertData(title ?: "Data error!", message))
    }

    protected fun showError(throwable: Throwable) {
        val errorMsg = RestApiUtil.getError(throwable)
        showError(message = errorMsg)
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}