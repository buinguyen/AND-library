package com.alan.app.mvvm.di

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alan.app.mvvm.base.util.SchedulerProvider
import javax.inject.Inject
import javax.inject.Singleton

/**
 * ViewModel factory class which keeps all the ViewModel instances
 */
@Singleton
class ViewModelFactory @Inject constructor(
    private val application: Application,
    private val schedulerProvider: SchedulerProvider
) : ViewModelProvider.Factory {

    private val genVMMap: Map<Class<*>, (() -> Any)> = mapOf(
        /* HomeVM::class.java to {HomeVM(application, schedulerProvider)} */
    )

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return genVMMap.getValue(modelClass).invoke() as T
    }
}
