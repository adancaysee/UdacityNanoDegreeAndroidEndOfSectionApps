package com.udacity.politicalpreparedness.data.domain

data class Representative(
    val official: Official,
    val office: Office,

)

data class Official(
    val name: String,
    val party: String? = null,
    val phones: List<String>? = null,
    val urls: List<String>? = null,
    val photoUrl: String? = null,
    val channels: List<Channel>? = null
)

data class Channel(
    val type: String,
    val id: String
)

data class Office(
    val name: String,
    val division: Division,
    val officials: List<Int>
)