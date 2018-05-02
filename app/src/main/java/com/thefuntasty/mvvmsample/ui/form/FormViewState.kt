package com.thefuntasty.mvvmsample.ui.form

import com.thefuntasty.mvvm.ViewState
import com.thefuntasty.mvvm.livedata.DefaultValueLiveData
import com.thefuntasty.mvvm.livedata.liveDataCombineTwo

class FormViewState(login: String, password: String) : ViewState {

    val login = DefaultValueLiveData(login)
    val password = DefaultValueLiveData(password)
    val submitEnabled = liveDataCombineTwo(this.login, this.password) { login, password ->
        login.isNotEmpty() && password.isNotEmpty()
    }
}
