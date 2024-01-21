package com.matchmate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matchmate.model.User
import com.matchmate.model.User.Status
import com.matchmate.repository.UserRepository
import com.matchmate.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    val users: StateFlow<Resource<List<User>>>
        get() = userRepository.users

    init {
        getUsers()
    }

    private fun getUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getUsers()
        }
    }

    fun updateUserStatus(@Status status: Int, userId: Int) {
        viewModelScope.launch(Dispatchers.IO) { userRepository.updateUserStatus(status, userId) }
    }
}