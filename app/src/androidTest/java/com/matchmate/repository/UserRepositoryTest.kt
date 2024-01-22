package com.matchmate.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.matchmate.database.UserDatabase
import com.matchmate.network.UserApi
import com.matchmate.utils.Resource
import com.matchmate.utils.Utils
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserRepositoryTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var userDatabase: UserDatabase
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        val userAPI = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApi::class.java)
        userDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            UserDatabase::class.java
        ).allowMainThreadQueries().build()
        userRepository = UserRepository(
            ApplicationProvider.getApplicationContext(),
            userAPI,
            userDatabase.getUserDao()
        )
    }

    /**
     * Test case for non empty user list response in repository.
     **/
    @Test
    fun testGetUser_ExpectedNonEmptyResponse() = runTest {
        val mockResponse = MockResponse()
        val content = Utils.readResourceFile("/mock_users_success.json")

        // Setting the mock response.
        mockResponse.apply {
            setResponseCode(200)
            setBody(content)
        }
        mockWebServer.enqueue(mockResponse)

        // Making API call.
        userRepository.getUsers()
        mockWebServer.takeRequest()

        // Validating the result.
        assertEquals(10, userRepository.users.value.data!!.size)
    }

    /**
     * Test case for error response in repository.
     **/
    @Test
    fun testGetUser_ExpectedErrorResponse() = runTest {
        val mockResponse = MockResponse()
        val content = Utils.readResourceFile("/mock_users_not_found.json")

        // Setting the mock response.
        mockResponse.apply {
            setResponseCode(404)
            setBody(content)
        }
        mockWebServer.enqueue(mockResponse)

        // Making API call.
        userRepository.getUsers()
        mockWebServer.takeRequest()

        // Validating the result.
        assert(userRepository.users.value is Resource.Error)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        userDatabase.close()
    }
}