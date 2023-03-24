package com.udacity.locationreminder.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.udacity.locationreminder.util.SingleLiveEvent

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    val navigationCommand: SingleLiveEvent<NavigationCommand> = SingleLiveEvent()
    val showSnackBar: SingleLiveEvent<String> = SingleLiveEvent()
    val showSnackBarInt: SingleLiveEvent<Int> = SingleLiveEvent()
    val showToast: SingleLiveEvent<String> = SingleLiveEvent()

}