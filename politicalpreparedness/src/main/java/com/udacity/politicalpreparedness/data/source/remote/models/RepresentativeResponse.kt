package com.udacity.politicalpreparedness.data.source.remote.models


data class RepresentativeResponse(
    val offices: List<Office>,
    val officials: List<Official>
)