package com.zadanierekrutacyjne2

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room

import com.zadanierekrutacyjne2.settings.Api
import com.zadanierekrutacyjne2.settings.ICallApiErrorOnStart
import com.zadanierekrutacyjne2.settings.ICallApiOnStart
import com.zadanierekturacyjne2.model.ItemModel
import com.zadanierekturacyjne2.settings.AppDatabase



public class PreloadData : AppCompatActivity() {

    var itemModelList: MutableList<ItemModel>? = null
    var appDatabase: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (appDatabase == null) {
            //appDatabase = Room.databaseBuilder(applicationContext, AppDatabase::class.java)
            appDatabase = Room.databaseBuilder(applicationContext,
                    AppDatabase::class.java, "database-name").allowMainThreadQueries().build()
        }
        val itemModelDao = appDatabase!!.ItemModelDao()
        Api.callApiOnStartBit(this@PreloadData)
        Api.addItemListListenerOnStart(object : ICallApiOnStart {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @SuppressLint("SetTextI18n")
            override fun OnCallApiOnStart() {
                var errorString: String? = Api.getError()
                if (errorString?.contains("bit") == true)
                {
                    if (errorString?.contains("git") == true)
                    {
                        Start()
                    }
                }
                itemModelList = Api.getItemList() as MutableList<ItemModel>?
                appDatabase!!.clearAllTables()

                for (item in itemModelList!!)
                {
                    itemModelDao?.insert(item)
                }
                Start()
            }
        })
    }

    fun Start()
    {
        val si = Intent(applicationContext, MainActivity::class.java)
        si.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        applicationContext.startActivity(si)
    }
}