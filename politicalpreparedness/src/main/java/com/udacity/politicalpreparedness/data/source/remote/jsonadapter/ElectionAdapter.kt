package com.udacity.politicalpreparedness.data.source.remote.jsonadapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import com.udacity.politicalpreparedness.data.source.remote.models.NetworkDivision

class ElectionAdapter {
    @FromJson
    fun divisionFromJson (ocdDivisionId: String): NetworkDivision {
        val countryDelimiter = "country:"
        val stateDelimiter = "state:"
        val country = ocdDivisionId.substringAfter(countryDelimiter,"")
                .substringBefore("/")
        val state = ocdDivisionId.substringAfter(stateDelimiter,"")
                .substringBefore("/")
        return NetworkDivision(ocdDivisionId, country, state)
    }

    @ToJson
    fun divisionToJson (division: NetworkDivision): String {
        return division.id
    }
}