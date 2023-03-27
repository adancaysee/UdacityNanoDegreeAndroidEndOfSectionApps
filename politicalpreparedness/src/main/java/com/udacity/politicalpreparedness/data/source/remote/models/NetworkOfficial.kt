package com.udacity.politicalpreparedness.data.source.remote.models

import com.udacity.politicalpreparedness.data.domain.Channel
import com.udacity.politicalpreparedness.data.domain.Official


data class NetworkOfficial(
    val name: String,
    val address: List<NetworkAddress>? = null,
    val party: String? = null,
    val phones: List<String>? = null,
    val urls: List<String>? = null,
    val photoUrl: String? = null,
    val channels: List<NetworkChannel>? = null
)

fun NetworkOfficial.asDomain() = Official(
    name, party, phones, urls, photoUrl, channels?.asDomain()
)


data class NetworkChannel(
    val type: String,
    val id: String
)

fun NetworkChannel.asDomain() = Channel(type, id)

fun List<NetworkChannel>.asDomain(): List<Channel> = map {
    Channel(it.type, it.id)
}