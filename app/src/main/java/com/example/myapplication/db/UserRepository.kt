package com.example.myapplication.db

import androidx.lifecycle.LiveData

class UserRepository(private val userDao: UserDao) {

    val allUsers : LiveData<List<User>> = userDao.getAllUsers()

    suspend fun insert(user: User){
        userDao.insertUser(user)
    }
}