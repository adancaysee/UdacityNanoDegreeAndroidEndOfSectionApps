package com.udacity.shoestore.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.shoestore.model.Shoe

class MainActivityViewModel : ViewModel() {

    private val _shoeList = MutableLiveData<ArrayList<Shoe>>()
    val shoeList: LiveData<ArrayList<Shoe>>
        get() = _shoeList

    private val _eventAddNewShoeFinish = MutableLiveData<UIState?>()
    val eventAddNewShoeFinish: LiveData<UIState?>
        get() = _eventAddNewShoeFinish


    private val list: ArrayList<Shoe> = ArrayList()

    data class UIState(
        var isSuccess: Boolean = false,
        var list: List<Shoe>? = null,
    )


    fun addNewShoe(name: String, size: String, company: String, description: String) {
        if (name.isBlank() || size.isBlank() || company.isBlank()) {
            _eventAddNewShoeFinish.postValue(UIState(isSuccess = false))
            return
        }
        list.add(Shoe(name = name, size = size.toDouble(),company = company, description = description))
        _shoeList.postValue(list)
        _eventAddNewShoeFinish.postValue(UIState(isSuccess = true,list = list))
    }

    fun onEventAddNewShoeFinishCompleted() {
        _eventAddNewShoeFinish.postValue(null)
    }

}