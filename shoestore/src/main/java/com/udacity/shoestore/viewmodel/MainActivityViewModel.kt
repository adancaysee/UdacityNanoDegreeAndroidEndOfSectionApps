package com.udacity.shoestore.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.shoestore.R
import com.udacity.shoestore.model.Shoe
import kotlin.random.Random

class MainActivityViewModel : ViewModel() {

    private val _shoeList = MutableLiveData<ArrayList<Shoe>>()
    val shoeList: LiveData<ArrayList<Shoe>>
        get() = _shoeList

    private val _eventAddNewShoeFinish = MutableLiveData<UIState?>()
    val eventAddNewShoeFinish: LiveData<UIState?>
        get() = _eventAddNewShoeFinish


    private val list: ArrayList<Shoe> = ArrayList()
    private val shoeImageList: List<Int> = listOf(
        R.drawable.images_1,
        R.drawable.images_2,
        R.drawable.images_3,
        R.drawable.images_4,
        R.drawable.images_5
    )

    data class UIState(
        var isSuccess: Boolean = false,
        var list: List<Shoe>? = null,
    )


    fun addNewShoe(name: String, size: String, company: String, description: String) {
        if (name.isBlank() || size.isBlank() || company.isBlank()) {
            _eventAddNewShoeFinish.postValue(UIState(isSuccess = false))
            return
        }
        list.add(
            Shoe(
                name = name,
                size = size.toInt(),
                company = company,
                description = description,
                images = listOf(shoeImageList[Random.nextInt(1,6) - 1])
            )
        )
        _shoeList.postValue(list)
        _eventAddNewShoeFinish.postValue(UIState(isSuccess = true, list = list))
    }

    fun onEventAddNewShoeFinishCompleted() {
        _eventAddNewShoeFinish.postValue(null)
    }

}