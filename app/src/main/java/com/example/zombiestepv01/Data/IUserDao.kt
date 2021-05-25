package com.example.zombiestepv01.Data

import com.example.zombiestepv01.Model.BEUser

interface IUserDao {

    fun login(email : String , password : String): BEUser

    fun checkIfUserExists(email : String) : Boolean

    fun createNewUser(user: BEUser)

    fun updateUser(user: BEUser)

    fun restartDb()
}