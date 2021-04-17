package com.zadanierekturacyjne2.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ItemModelDao {
    @get:Query("SELECT * FROM itemModel")
    val all: List<ItemModel?>?

    @Query("SELECT description FROM itemModel WHERE orderId = :id")
    fun getItem(id: Int): String?

    @Insert
    fun insert(itemModel: ItemModel?)
}