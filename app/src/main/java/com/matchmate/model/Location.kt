package com.matchmate.model

import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("city")
    val city: String,

    @SerializedName("state")
    val state: String,

    @SerializedName("country")
    val country: String
)