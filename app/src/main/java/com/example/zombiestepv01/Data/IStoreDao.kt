package com.example.zombiestepv01.Data

import com.example.zombiestepv01.Model.BEItem

interface IStoreDao {

    fun initializeStore(userId: Int)

    fun getStore(userId: Int) : List<BEItem>

    fun restartDb()

    fun updateStore(item : BEItem)
}