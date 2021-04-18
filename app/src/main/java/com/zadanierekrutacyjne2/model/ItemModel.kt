package com.zadanierekturacyjne2.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "itemModel")
class ItemModel {
    @PrimaryKey
    var orderId = 0

    @ColumnInfo(name = "title")
    var title: String? = null

    @ColumnInfo(name = "modificationDate")
    var modificationDate: String? = null

    @ColumnInfo(name = "image_url")
    var image_url: String? = null

    @ColumnInfo(name = "description")
    var description: String? = null
    fun Sortowanie(itemModelList: List<ItemModel?>?)
    {
        Collections.sort(itemModelList, object : Comparator<ItemModel?>
        {
            override fun compare(o1: ItemModel?, o2: ItemModel?): Int
            {
                return if (o1!!.orderId < o2!!.orderId)
                {
                    1
                }
                else
                {
                    -1
                }
            }
        })
    }
}