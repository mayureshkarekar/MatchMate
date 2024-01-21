package com.matchmate.model

import androidx.annotation.IntDef
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
    val picture: Picture,

    @SerializedName("status")
    @Status val status: Int = PENDING
) {
    companion object {
        const val PENDING = 0
        const val ACCEPTED = 1
        const val REJECTED = 2
        const val GENDER_MALE = "male"
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(PENDING, ACCEPTED, REJECTED)
    annotation class Status
}

data class UserResponse(
    @SerializedName("results")
    val results: List<User>
)

// region constants
const val USER_TABLE_NAME = "user"
// endregion