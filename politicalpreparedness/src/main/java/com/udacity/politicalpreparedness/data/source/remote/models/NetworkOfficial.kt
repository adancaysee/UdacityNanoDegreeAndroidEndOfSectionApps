package com.udacity.politicalpreparedness.data.source.remote.models


data class NetworkOfficial(
    val name: String,
    val address: List<NetworkAddress>? = null,
    val party: String? = null,
    val phones: List<String>? = null,
    val urls: List<String>? = null,
    val photoUrl: String? = null,
    val channels: List<NetworkChannel>? = null
)