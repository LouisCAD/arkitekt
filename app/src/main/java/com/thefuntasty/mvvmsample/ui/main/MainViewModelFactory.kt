package com.thefuntasty.mvvmsample.ui.main

import com.thefuntasty.mvvm.factory.BaseViewModelFactory
import com.thefuntasty.mvvmsample.ui.main.MainViewModel
import javax.inject.Inject
import javax.inject.Provider

class MainViewModelFactory @Inject constructor(override val viewModelProvider: Provider<MainViewModel>) :
        BaseViewModelFactory<MainViewModel>()