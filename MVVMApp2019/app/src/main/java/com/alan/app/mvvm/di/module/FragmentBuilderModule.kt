package com.alan.app.mvvm.di.module

import com.alan.app.mvvm.ui.first.FirstFragment
import com.alan.app.mvvm.ui.second.SecondFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeFirstFragment(): FirstFragment

    @ContributesAndroidInjector
    abstract fun contributeSecondFragment(): SecondFragment

}