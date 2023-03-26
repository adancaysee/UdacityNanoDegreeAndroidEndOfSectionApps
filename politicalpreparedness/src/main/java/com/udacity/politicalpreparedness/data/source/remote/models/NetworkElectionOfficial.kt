package com.udacity.politicalpreparedness.data.source.remote.models

import com.squareup.moshi.Json

data class NetworkElectionOfficial(
    val name: String,
    val title: String,
    @Json(name = "officePhoneNumber") val phone: String,
    @Json(name = "faxNumber") val fax: String,
    val emailAddress: String
)