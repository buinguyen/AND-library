package com.alan.app.mvvm.di

import android.app.Application
import android.content.Context
import com.alan.app.mvvm.MVVMApp
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun provideMVVMApp(application: MVVMApp): Application {
        return application
    }

    @Provides
    fun provideContext(application: MVVMApp): Context {
        return application.applicationContext
    }

}
