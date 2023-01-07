package com.udacity.shoestore.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Shoe(var name: String, var size: Int, var company: String, var description: String,
                val images: List<Int> = mutableListOf()) : Parcelable