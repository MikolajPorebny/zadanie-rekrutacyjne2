package com.zadanierekturacyjne2.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ItemModelDao {
    @Query("SELECT * FROM itemModel")
    fun getAll(): List<ItemModel?>?

    @Query("SELECT description FROM itemModel WHERE orderId = :id")
    fun getItem(id: Int): String?

    @Insert
    fun insert(itemModel: ItemModel?)
}