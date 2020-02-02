package com.alan.app.mvvm.di.module

import android.app.Application
import android.content.Context
import com.alan.app.mvvm.MvvmApp
import com.alan.app.mvvm.data.source.local.PreferenceHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    fun provideMvvmApp(application: MvvmApp): Application {
        return application
    }

    @Provides
    fun provideContext(application: MvvmApp): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun providePreferenceHelper(context: Context): PreferenceHelper {
        return PreferenceHelper(context)
    }
}
