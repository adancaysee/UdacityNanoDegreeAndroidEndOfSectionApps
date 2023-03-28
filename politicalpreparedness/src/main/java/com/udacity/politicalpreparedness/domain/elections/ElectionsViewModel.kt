package com.udacity.politicalpreparedness.domain.elections

import android.app.Application
import androidx.lifecycle.*
import com.udacity.politicalpreparedness.data.domain.Election
import com.udacity.politicalpreparedness.data.repository.ElectionRepository
import kotlinx.coroutines.launch
import com.udacity.politicalpreparedness.data.repository.Result
import com.udacity.politicalpreparedness.util.SingleLiveEvent
import com.udacity.politicalpreparedness.util.isInternetAvailable

class ElectionsViewModel(
    application: Application,
    private val repository: ElectionRepository
) : ViewModel() {

    private val _upcomingElectionList = MutableLiveData<List<Election>?>()
    val upcomingElectionList: LiveData<List<Election>?> = _upcomingElectionList

    val empty = Transformations.map(_upcomingElectionList) { it.isNullOrEmpty() }

    val savedElectionList = repository.observeSavedElections()

    private val _dataLoading = MutableLiveData(false)
    val dataLoading: LiveData<Boolean> = _dataLoading

    val isInternetAvailable = MutableLiveData(application.isInternetAvailable())

    private val _navigateToVoterInfoEvent = SingleLiveEvent<Election>()
    val navigateToVoterInfoEvent = _navigateToVoterInfoEvent


    init {
        refresh()
    }

    private fun refresh() {
        if (isInternetAvailable.value == false) return
        _dataLoading.value = true
        viewModelScope.launch {
            val result = repository.fetchUpcomingElections()
            if (result is Result.Success) {
                _upcomingElectionList.value = result.data
            } else {
                _upcomingElectionList.value = null
            }
            _dataLoading.value = false
        }
    }

    fun navigateToVoterInfo(election: Election) {
        _navigateToVoterInfoEvent.value = election
    }

}