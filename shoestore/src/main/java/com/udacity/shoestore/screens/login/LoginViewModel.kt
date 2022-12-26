package com.udacity.shoestore.screens.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    private val _eventLoginOrRegisterFinish = MutableLiveData<Boolean>()
    val eventLoginOrRegisterFinished: LiveData<Boolean>
        get() = _eventLoginOrRegisterFinish

    fun onClickLoginOrRegister() {
        _eventLoginOrRegisterFinish.value = true
    }

    fun onLoginOrRegisterFinishComplete() {
        _eventLoginOrRegisterFinish.value = false
    }


}