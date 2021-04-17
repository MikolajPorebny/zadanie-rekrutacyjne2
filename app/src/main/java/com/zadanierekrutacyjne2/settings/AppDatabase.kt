package com.zadanierekturacyjne2.settings

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zadanierekturacyjne2.model.ItemModel
import com.zadanierekturacyjne2.model.ItemModelDao

@Database(entities = [ItemModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ItemModelDao(): ItemModelDao?
}