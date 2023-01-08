package com.udacity.shoestore.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.shoestore.model.Shoe
import com.udacity.shoestore.screens.util.getShoeImageList
import kotlin.random.Random

class MainActivityViewModel : ViewModel() {

    private val _shoeList = MutableLiveData<ArrayList<Shoe>>()
    val shoeList: LiveData<ArrayList<Shoe>>
        get() = _shoeList

    private val list: ArrayList<Shoe> = ArrayList()

    val shoeName = MutableLiveData("")
    val shoeSize = MutableLiveData("")
    val shoeDescription = MutableLiveData("")
    val shoeCompany = MutableLiveData("")

    private val _eventAddNewShoeFinish = MutableLiveData<UIState?>()
    val eventAddNewShoeFinish: LiveData<UIState?>
        get() = _eventAddNewShoeFinish

    data class UIState(
        var isSuccess: Boolean = false,
        var list: List<Shoe>? = null,
    )

    fun addNewShoe() {
        if (shoeName.value.isNullOrBlank() || shoeCompany.value.isNullOrBlank()
            || shoeSize.value.isNullOrBlank() || shoeDescription.value.isNullOrBlank()
        ) {
            _eventAddNewShoeFinish.postValue(UIState(false, null))
            return
        }
        list.add(
            Shoe(
                name = shoeName.value!!,
                size = shoeSize.value!!.toInt(),
                company = shoeCompany.value!!,
                description = shoeDescription.value!!,
                images = listOf(getShoeImageList()[Random.nextInt(1, 6) - 1])
            )
        )
        _shoeList.postValue(list)
        clearShoeData()
        _eventAddNewShoeFinish.postValue(UIState(isSuccess = true, list = list))
    }

    private fun clearShoeData() {
        shoeName.postValue("")
        shoeCompany.postValue("")
        shoeSize.postValue("")
        shoeDescription.postValue("")
    }

    fun onEventAddNewShoeFinishCompleted() {
        _eventAddNewShoeFinish.postValue(null)
    }



}