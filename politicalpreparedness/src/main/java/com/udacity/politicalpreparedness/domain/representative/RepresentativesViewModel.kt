package com.udacity.politicalpreparedness.domain.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.politicalpreparedness.data.domain.Representative
import com.udacity.politicalpreparedness.data.repository.Result
import com.udacity.politicalpreparedness.data.repository.RepresentativeRepository
import kotlinx.coroutines.launch

class RepresentativesViewModel(
    private val representativeRepository: RepresentativeRepository
) : ViewModel() {

    private val _representativeList = MutableLiveData<List<Representative>>()

    private val _dataLoading = MutableLiveData(false)
    val dataLoading: LiveData<Boolean> = _dataLoading

    init {
        _dataLoading.value = true
        viewModelScope.launch {
            val result = representativeRepository.fetchRepresentatives()
            if (result is Result.Success) {
                val response = result.data
                _representativeList.value = response
            }
        }
        _dataLoading.value = false
    }
}