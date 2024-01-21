package com.matchmate.repository

import android.content.Context
import com.matchmate.R
import com.matchmate.database.UserDao
import com.matchmate.model.User
import com.matchmate.network.NetworkUtils
import com.matchmate.network.UserApi
import com.matchmate.utils.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class UserRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userApi: UserApi,
    private val userDao: UserDao
) {
    // region user list
    private val _users = MutableStateFlow<Resource<List<User>>>(Resource.Loading())
    val users: StateFlow<Resource<List<User>>>
        get() = _users

    /**
     * This method is used to fetch the list of users from server. If the internet is not available
     * it fetches the locally stored data.
     **/
    suspend fun getUsers() {
        try {
            if (NetworkUtils.isInternetAvailable(context)) {
                val response = userApi.getUsers()
                if (response.isSuccessful) {
                    Timber.d("Users fetched successfully from server.")
                    _users.emit(Resource.Success(response.body()?.results.orEmpty()))
                } else {
                    Timber.d("Failed to fetch users from server. Cause: ${response.errorBody()}")
                    _users.emit(Resource.Error(context.getString(R.string.something_went_wrong)))
                }
            } else {
                Timber.d("Users fetched successfully from database.")
                _users.emit(Resource.Success(userDao.getUsers()))
            }
        } catch (e: IOException) {
            Timber.e("Failed to fetch users. Cause: ${e.message}")
            _users.emit(Resource.Error(context.getString(R.string.internet_not_available)))
        } catch (e: HttpException) {
            Timber.e("Failed to fetch users. Cause: ${e.message}")
            _users.emit(Resource.Error(context.getString(R.string.something_went_wrong)))
        } catch (e: Exception) {
            Timber.e("Failed to fetch users. Cause: ${e.message}")
            _users.emit(Resource.Error(context.getString(R.string.something_went_wrong)))
        }
    }
    // endregion
}