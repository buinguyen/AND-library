package com.alan.app.mvvm.ui.second

import android.app.Application
import com.alan.app.mvvm.base.BaseViewModel
import com.alan.app.mvvm.data.source.local.PreferenceHelper
import com.alan.app.mvvm.utils.SchedulerProvider

class SecondVM
constructor(application: Application, schedulerProvider: SchedulerProvider,
            private val prefHelper: PreferenceHelper) : BaseViewModel(application, schedulerProvider) {

}