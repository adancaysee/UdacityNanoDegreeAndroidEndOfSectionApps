package com.udacity.politicalpreparedness.domain.voterinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.politicalpreparedness.R
import com.udacity.politicalpreparedness.data.domain.Division
import com.udacity.politicalpreparedness.data.domain.VoterInfo
import com.udacity.politicalpreparedness.data.repository.ElectionRepository
import kotlinx.coroutines.launch
import com.udacity.politicalpreparedness.data.repository.Result
import com.udacity.politicalpreparedness.data.repository.VoterInfoRepository
import com.udacity.politicalpreparedness.util.SingleLiveEvent

class VoterInfoViewModel(
    private val electionRepository: ElectionRepository,
    private val voterInfoRepository: VoterInfoRepository
) : ViewModel() {

    private val _voterInfo = MutableLiveData<VoterInfo>()
    val voterInfo: LiveData<VoterInfo> = _voterInfo

    private val electionId = MutableLiveData<Int>()
    private var division: Division? = null

    private val _isSaved = MutableLiveData(false)
    val isSavedResourceInt: LiveData<Int> = Transformations.map(_isSaved) {
        if (it == true) {
            R.string.unfollow_election
        } else {
            R.string.follow_election
        }
    }

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    val isDataAvailable: LiveData<Boolean> = Transformations.map(_voterInfo) { it != null }

    val openUrlEvent = SingleLiveEvent<String?>()

    fun setParameters(electionId: Int, division: Division) {
        //for configuration changes
        if (this.electionId.value == electionId && _dataLoading.value == true) return
        this.electionId.value = electionId
        this.division = division

        fetchVoterInfo(electionId, division)

        viewModelScope.launch {
            val result = electionRepository.getSavedElection(electionId)
            _isSaved.value = result is Result.Success
        }
    }

    private fun fetchVoterInfo(electionId: Int, division: Division) {
        _dataLoading.value = true
        viewModelScope.launch {
            val address = "${division.state}, ${division.country}"
            val result = voterInfoRepository.fetchVoterInfo(electionId, address)
            if (result is Result.Success) {
                val response = result.data
                _voterInfo.value = response
            }
            _dataLoading.value = false
        }
    }

    fun changeFollowingVoterInfo() {
        viewModelScope.launch {
            voterInfo.value?.election?.let {
                if (_isSaved.value == true) {
                    electionRepository.deleteElection(it.id)
                    _isSaved.value = false
                } else {
                    electionRepository.saveElection(it)
                    _isSaved.value = true
                }
            }
        }
    }

    fun openBallotInfo() {
        openUrlEvent.value = voterInfo.value?.ballotInfoUrl
    }

    fun openVotingLocationFinder() {
        openUrlEvent.value = voterInfo.value?.votingLocationFinderUrl
    }

}