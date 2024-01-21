package com.matchmate.network

import com.matchmate.model.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApi {
    @GET(API)
    suspend fun getUsers(
        @Query(RESULTS) resultsLimit: Int = DEFAULT_RESULTS_LIMIT
    ): Response<UserResponse>
}

// region constants
const val BASE_URL = "https://randomuser.me/"
const val API = "api"
const val RESULTS = "results"
const val DEFAULT_RESULTS_LIMIT = 20
// endregion