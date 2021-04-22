package com.zadanierekturacyjne2.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ItemModelDao {
    @Query("SELECT * FROM itemModel")
    fun getAll(): List<ItemModel>?

    @Query("DELETE FROM itemModel WHERE repo = 'bit'")
    fun delBit()

    @Query("DELETE FROM itemModel WHERE repo = 'git'")
    fun delGit()

    @Query("SELECT * FROM itemModel WHERE orderId = :id")
    fun getItem(id: String?): ItemModel?

    @Insert
    fun insert(itemModel: ItemModel?)
}