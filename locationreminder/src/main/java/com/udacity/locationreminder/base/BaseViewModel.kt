package com.udacity.locationreminder.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.udacity.locationreminder.util.SingleLiveEvent

/*
TODO : showLoading,showNoData,showErrorMessage --> correct handle
 */

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    val navigationCommandEvent: SingleLiveEvent<NavigationCommand> = SingleLiveEvent()
    val showSnackBarEvent: SingleLiveEvent<String> = SingleLiveEvent()
    val showSnackBarIntEvent: SingleLiveEvent<Int> = SingleLiveEvent()
    val showToastEvent: SingleLiveEvent<String> = SingleLiveEvent()

    val showLoading: MutableLiveData<Boolean> = MutableLiveData()
    val showNoData: MutableLiveData<Boolean> = MutableLiveData()
    val showErrorMessageEvent: SingleLiveEvent<String> = SingleLiveEvent()

}