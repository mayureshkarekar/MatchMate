package com.matchmate.model

import com.google.gson.annotations.SerializedName

data class Picture(
    @SerializedName("large")
    val large: String,

    @SerializedName("thumbnail")
    val thumbnail: String
)