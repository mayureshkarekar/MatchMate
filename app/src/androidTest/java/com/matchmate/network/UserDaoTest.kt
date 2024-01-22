package com.matchmate.network

import com.google.gson.JsonSyntaxException
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

class UserApiTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var userAPI: UserApi

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        userAPI = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApi::class.java)
    }

    /**
     * Test case for non empty user list response.
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
        val response = userAPI.getUsers()
        mockWebServer.takeRequest()

        // Validating the result.
        assertEquals(10, response.body()!!.results.size)
    }

    /**
     * Test case for not found error code.
     **/
    @Test
    fun testGetUser_ExpectedNotFoundResponse() = runTest {
        val mockResponse = MockResponse()
        val content = Utils.readResourceFile("/mock_users_not_found.json")

        // Setting the mock response.
        mockResponse.apply {
            setResponseCode(404)
            setBody(content)
        }
        mockWebServer.enqueue(mockResponse)

        // Making API call.
        val response = userAPI.getUsers()
        mockWebServer.takeRequest()

        // Validating the result.
        assertEquals(404, response.code())
    }

    /**
     * Test case for malformed JSON/invalid response.
     **/
    @Test(expected = JsonSyntaxException::class)
    fun testGetUser_ExpectedMalformedException() = runTest {
        val mockResponse = MockResponse()
        val content = Utils.readResourceFile("/mock_users_malformed.json")

        // Setting the mock response.
        mockResponse.apply {
            setResponseCode(200)
            setBody(content)
        }
        mockWebServer.enqueue(mockResponse)

        // Making API call.
        val response = userAPI.getUsers()
        mockWebServer.takeRequest()

        // Validating the result.
        assertEquals(10, response.body()!!.results.size)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}