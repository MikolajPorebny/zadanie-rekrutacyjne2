package com.zadanierekturacyjne2.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "itemModel")
class ItemModel {
    @PrimaryKey(autoGenerate = true)
    var orderId = 0

    @ColumnInfo(name = "name")
    var name: String? = null

    @ColumnInfo(name = "user")
    var user: String? = null

    @ColumnInfo(name = "avatar")
    var avatar: String? = null

    @ColumnInfo(name = "description")
    var description: String? = null

    @ColumnInfo(name = "repo")
    var repo: String? = null



}