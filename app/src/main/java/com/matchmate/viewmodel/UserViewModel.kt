package com.matchmate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matchmate.model.User
import com.matchmate.repository.UserRepository
import com.matchmate.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

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
}