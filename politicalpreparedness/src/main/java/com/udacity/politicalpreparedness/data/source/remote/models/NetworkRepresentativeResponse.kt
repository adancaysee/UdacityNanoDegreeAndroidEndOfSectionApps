package com.udacity.politicalpreparedness.data.source.remote.models


data class NetworkRepresentativeResponse(
    val offices: List<NetworkOffice>,
    val officials: List<NetworkOfficial>
)