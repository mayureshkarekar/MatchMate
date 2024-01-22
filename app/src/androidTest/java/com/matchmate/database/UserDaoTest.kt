package com.matchmate.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.matchmate.model.Dob
import com.matchmate.model.Location
import com.matchmate.model.Name
import com.matchmate.model.Picture
import com.matchmate.model.User
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class UserDaoTest {
    private lateinit var userDatabase: UserDatabase
    private lateinit var userDao: UserDao

    @Before()
    fun setUp() {
        userDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            UserDatabase::class.java
        ).allowMainThreadQueries().build()
        userDao = userDatabase.getUserDao()
    }

    // region insert test
    /**
     * Test case for inserting single user item.
     **/
    @Test
    fun insertUser_expectedSingleUser() = runBlocking {
        // Adding user to the database.
        val user = User(
            id = 1,
            name = Name("Tony", "Stark"),
            dob = Dob(50, "21/01/1960"),
            gender = "male",
            location = Location("Thane", "Maharashtra", "India"),
            picture = Picture(
                "https://picsum.photos/300/150.jpg",
                "https://picsum.photos/200/100.jpg"
            )
        )
        userDao.addUsers(listOf(user))

        // Verifying the result.
        val savedUser = userDao.getUsers()
        assertEquals(1, savedUser.size)
    }

    /**
     * Test case for inserting multiple user item.
     **/
    @Test
    fun insertUser_expectedMultipleUser() = runBlocking {
        // Adding user to the database.
        val user = User(
            id = 1,
            name = Name("Tony", "Stark"),
            dob = Dob(50, "21/01/1960"),
            gender = "male",
            location = Location("Thane", "Maharashtra", "India"),
            picture = Picture(
                "https://picsum.photos/300/150.jpg",
                "https://picsum.photos/200/100.jpg"
            )
        )

        val user2 = User(
            id = 2,
            name = Name("Steve", "Rogers"),
            dob = Dob(70, "21/01/1960"),
            gender = "male",
            location = Location("Mumbai", "Maharashtra", "India"),
            picture = Picture(
                "https://picsum.photos/300/150.jpg",
                "https://picsum.photos/200/100.jpg"
            )
        )
        userDao.addUsers(listOf(user, user2))

        // Verifying the result.
        val savedUser = userDao.getUsers()
        assertEquals(2, savedUser.size)
    }
    // endregion

    @After
    fun tearDown() {
        userDatabase.close()
    }
}