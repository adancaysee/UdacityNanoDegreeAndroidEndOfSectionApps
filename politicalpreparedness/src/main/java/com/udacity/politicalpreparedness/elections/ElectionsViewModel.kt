package com.udacity.politicalpreparedness.elections

import androidx.lifecycle.*
import com.udacity.politicalpreparedness.data.domain.Election
import com.udacity.politicalpreparedness.data.source.CivicsRepository
import kotlinx.coroutines.launch
import com.udacity.politicalpreparedness.data.domain.Result
import com.udacity.politicalpreparedness.util.SingleLiveEvent

class ElectionsViewModel(private val repository: CivicsRepository) : ViewModel() {

    private val _upcomingElectionList = MutableLiveData<List<Election>?>()
    val upcomingElectionList: LiveData<List<Election>?> = _upcomingElectionList

    private val _dataLoading = MutableLiveData(false)
    val dataLoading: LiveData<Boolean> = _dataLoading

    val empty = Transformations.map(_upcomingElectionList) { it.isNullOrEmpty() }

    val savedElectionList = repository.observeSavedElections()

    init {
        refresh()
    }

    private fun refresh() {
        _dataLoading.value = true
        viewModelScope.launch {
            val result = repository.fetchUpcomingSelections()
            if (result is Result.Success) {
                _upcomingElectionList.value = result.data
            } else {
                _upcomingElectionList.value = null
            }
            _dataLoading.value = false
        }
    }


}