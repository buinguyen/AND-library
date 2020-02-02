package com.alan.app.mvvm.di

import com.alan.app.mvvm.MVVMApp
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        FragmentModule::class
    ]
)
@Singleton
interface AppComponent : AndroidInjector<MVVMApp> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<MVVMApp>()

    override fun inject(app: MVVMApp)
}
