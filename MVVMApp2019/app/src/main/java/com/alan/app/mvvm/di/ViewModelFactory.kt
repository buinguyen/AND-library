package com.alan.app.mvvm.di

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alan.app.mvvm.data.source.local.PreferenceHelper
import com.alan.app.mvvm.ui.first.FirstVM
import com.alan.app.mvvm.ui.second.SecondVM
import com.alan.app.mvvm.utils.SchedulerProvider
import javax.inject.Inject
import javax.inject.Singleton

/**
 * ViewModel factory class which keeps all the ViewModel instances
 */
@Singleton
class ViewModelFactory @Inject
constructor(private val application: Application,
            private val schedulerProvider: SchedulerProvider,
            private val preferenceHelper: PreferenceHelper) : ViewModelProvider.Factory {

    private val genVMMap: Map<Class<*>, (() -> Any)> = mapOf(
            FirstVM::class.java to { FirstVM(application, schedulerProvider, preferenceHelper) },
            SecondVM::class.java to { SecondVM(application, schedulerProvider, preferenceHelper) }
    )

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return genVMMap.getValue(modelClass).invoke() as T
    }
}
