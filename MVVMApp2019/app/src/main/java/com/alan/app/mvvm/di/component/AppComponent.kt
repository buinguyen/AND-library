package com.alan.app.mvvm.di.component

import com.alan.app.mvvm.MvvmApp
import com.alan.app.mvvm.di.module.AppModule
import com.alan.app.mvvm.di.module.FragmentBuilderModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        FragmentBuilderModule::class
    ]
)
@Singleton
interface AppComponent : AndroidInjector<MvvmApp> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<MvvmApp>()

    override fun inject(app: MvvmApp)
}
