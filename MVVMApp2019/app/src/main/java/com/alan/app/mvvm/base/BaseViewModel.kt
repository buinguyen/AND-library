package com.alan.app.mvvm.base

import android.app.Application
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.alan.app.mvvm.base.core.ViewModelConfiguration
import com.alan.app.mvvm.base.model.AlertData
import com.alan.app.mvvm.base.model.LoadingStatus
import com.alan.app.mvvm.base.util.RestApiUtil
import com.alan.app.mvvm.base.util.SchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

abstract class BaseViewModel(val applicaton: Application,
                             val schedulerProvider: SchedulerProvider) : AndroidViewModel(applicaton), ViewModelConfiguration {

    private val disposables = CompositeDisposable()

    private val viewModelJob = SupervisorJob()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

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

    protected fun launchDataLoad(work: ((coroutineScope: CoroutineScope) -> Unit)?) {
        uiScope.launch {
            withContext(Dispatchers.Default) {
                work?.invoke(this)
            }
        }
    }

    protected fun showError(title: String? = null, message: String? = null) {
        errorLV.value = (AlertData(title ?: "Data error!", message))
    }

    protected fun showError(throwable: Throwable) {
        val errorMsg = RestApiUtil.getError(throwable)
        showError(message = errorMsg)
    }

    protected fun createTextBody(text: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), text)
    }

    protected fun createFileBody(name: String, file: File): MultipartBody.Part {
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        return MultipartBody.Part.createFormData(name, file.name, requestFile)
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        disposables.clear()
        viewModelJob.cancel()
    }
}