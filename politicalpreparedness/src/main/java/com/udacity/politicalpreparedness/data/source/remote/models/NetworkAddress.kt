package com.udacity.politicalpreparedness.data.source.remote.models

data class NetworkAddress(
    val line1: String,
    val line2: String? = null,
    val city: String,
    val state: String,
    val zip: String
)