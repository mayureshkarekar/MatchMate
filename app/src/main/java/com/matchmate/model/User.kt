package com.matchmate.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = USER_TABLE_NAME)
data class User(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("userid")
    val id: Int,

    @SerializedName("name")
    val name: Name,

    @SerializedName("dob")
    val dob: Dob,

    @SerializedName("gender")
    val gender: String,

    @SerializedName("location")
    val location: Location,

    @SerializedName("picture")
    val picture: Picture
)

data class UserResponse(
    @SerializedName("results")
    val results: List<User>
)

// region constants
const val USER_TABLE_NAME = "user"
// endregion