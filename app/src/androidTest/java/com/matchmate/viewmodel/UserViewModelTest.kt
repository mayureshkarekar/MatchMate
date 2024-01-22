package com.matchmate.viewmodel

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.matchmate.database.UserDatabase
import com.matchmate.network.UserApi
import com.matchmate.repository.UserRepository
import com.matchmate.utils.Resource
import com.matchmate.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@ExperimentalCoroutinesApi
class UserViewModelTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var userDatabase: UserDatabase
    private lateinit var userRepository: UserRepository
    private lateinit var userViewModel: UserViewModel
    private val testDispatcher = StandardTestDispatcher()

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
            ApplicationProvider.getApplicationContext(), userAPI, userDatabase.getUserDao()
        )
        Dispatchers.setMain(testDispatcher)
    }

    /**
     * Test case for non empty user list response in viewmodel.
     **/
    @Test
    fun testGetUser_ExpectedNonEmptyResponse() = runBlocking {
        val mockResponse = MockResponse()
        val content = Utils.readResourceFile("/mock_users_success.json")

        // Setting the mock response.
        mockResponse.apply {
            setResponseCode(200)
            setBody(content)
        }
        mockWebServer.enqueue(mockResponse)

        // Making API call.
        userViewModel = UserViewModel(userRepository)
        mockWebServer.takeRequest()

        // Scheduling the Coroutines
        testDispatcher.scheduler.advanceUntilIdle()

        userRepository.users.test {
            val userResource = awaitItem()
            Assert.assertEquals(10, userResource.data!!.size)
            cancel()
        }
    }

    /**
     * Test case for error response in viewmodel.
     **/
    @Test
    fun testGetUser_ExpectedErrorResponse() = runBlocking {
        val mockResponse = MockResponse()
        val content = Utils.readResourceFile("/mock_users_not_found.json")

        // Setting the mock response.
        mockResponse.apply {
            setResponseCode(400)
            setBody(content)
        }
        mockWebServer.enqueue(mockResponse)

        // Making API call.
        userViewModel = UserViewModel(userRepository)
        mockWebServer.takeRequest()

        // Scheduling the Coroutines
        testDispatcher.scheduler.advanceUntilIdle()

        userRepository.users.test {
            val userResource = awaitItem()
            assert(userResource is Resource.Error)
            cancel()
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mockWebServer.shutdown()
        userDatabase.close()
    }
}