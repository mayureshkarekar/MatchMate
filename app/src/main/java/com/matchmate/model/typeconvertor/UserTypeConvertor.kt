package com.matchmate.model.typeconvertor

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.matchmate.model.Dob
import com.matchmate.model.Location
import com.matchmate.model.Name
import com.matchmate.model.Picture

class UserTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromName(name: Name): String {
        return gson.toJson(name)
    }

    @TypeConverter
    fun toName(value: String): Name {
        return gson.fromJson(value, Name::class.java)
    }

    @TypeConverter
    fun fromDob(dob: Dob): String {
        return gson.toJson(dob)
    }

    @TypeConverter
    fun toDob(value: String): Dob {
        return gson.fromJson(value, Dob::class.java)
    }

    @TypeConverter
    fun fromLocation(location: Location): String {
        return gson.toJson(location)
    }

    @TypeConverter
    fun toLocation(value: String): Location {
        return gson.fromJson(value, Location::class.java)
    }

    @TypeConverter
    fun fromPicture(picture: Picture): String {
        return gson.toJson(picture)
    }

    @TypeConverter
    fun toPicture(value: String): Picture {
        return gson.fromJson(value, Picture::class.java)
    }
}