package com.alan.app.mvvm.ui.first

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.alan.app.mvvm.base.BaseViewModel
import com.alan.app.mvvm.data.source.local.PreferenceHelper
import com.alan.app.mvvm.utils.SchedulerProvider

class FirstVM
constructor(application: Application, schedulerProvider: SchedulerProvider,
            private val prefHelper: PreferenceHelper) : BaseViewModel(application, schedulerProvider) {

    internal val resultLiveData = MutableLiveData<String>()

    fun setResult(result: String) {
        resultLiveData.postValue(result)
    }

}