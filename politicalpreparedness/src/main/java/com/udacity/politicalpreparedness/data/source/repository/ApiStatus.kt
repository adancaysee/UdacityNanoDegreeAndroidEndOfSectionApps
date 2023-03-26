package com.udacity.politicalpreparedness.data.source.repository

sealed class ApiStatus {
    object Loading : ApiStatus()
    object Success : ApiStatus()
    data class Failure(val errorMsg: String) : ApiStatus()
}