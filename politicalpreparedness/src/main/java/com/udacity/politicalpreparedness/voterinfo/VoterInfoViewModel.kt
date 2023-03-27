package com.udacity.politicalpreparedness.voterinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.politicalpreparedness.data.domain.Division
import com.udacity.politicalpreparedness.data.repository.ElectionRepository
import kotlinx.coroutines.launch
import com.udacity.politicalpreparedness.data.domain.Result
import com.udacity.politicalpreparedness.data.repository.VoterInfoRepository
import timber.log.Timber

class VoterInfoViewModel(
    private val electionRepository: ElectionRepository,
    private val voterInfoRepository: VoterInfoRepository
) : ViewModel() {

    private val electionId = MutableLiveData<Int>()
    private var division: Division? = null

    private val _isSaved = MutableLiveData<Boolean>()

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    fun setParameters(electionId: Int, division: Division) {
        //for configuration changes
        if (this.electionId.value == electionId && _dataLoading.value == true) return
        this.electionId.value = electionId
        this.division = division

        viewModelScope.launch {
            val result = electionRepository.getSavedElection(electionId)
            _isSaved.value = result is Result.Success
        }
        fetchVoterInfo(electionId,division)
    }

    private fun fetchVoterInfo(electionId: Int,division: Division) {
        _dataLoading.value = true
        viewModelScope.launch {
            val address = "${division.state}, ${division.country}"
            val result = voterInfoRepository.fetchVoterInfo(electionId,address)
            if (result is Result.Success) {

            }else {

            }

        }
    }

}