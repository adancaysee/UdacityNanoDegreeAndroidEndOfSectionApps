package com.udacity.shoestore.tutorial

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import com.udacity.shoestore.BR


/**
 * Observable object
 */
class User : BaseObservable() {

    @get:Bindable
    var firstName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.firstName)
        }

    @get:Bindable
    var lastName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.lastName)
        }
}

/**
 * Observable field
 */
class Animal : BaseObservable() {

    val name = ObservableField<String>()
    val age = 0
}