package com.zadanierekrutacyjne2

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.zadanierekrutacyjne2.model.ItemModelAdapter
import com.zadanierekrutacyjne2.settings.Api
import com.zadanierekrutacyjne2.settings.ICallApi
import com.zadanierekturacyjne2.model.ItemModel
import com.zadanierekturacyjne2.model.ItemModelDao
import com.zadanierekturacyjne2.settings.AppDatabase
import java.util.ArrayList


class MainActivity : AppCompatActivity() {
    var itemModelListMain: MutableList<ItemModel>? = null


    var buttonReload: TextView? = null
    var buttonSort: TextView? = null
    var appDatabase: AppDatabase? = null
    var itemModelDao: ItemModelDao? = null
    var gridItemList: GridView? = null
    var itemModelAdapter: ItemModelAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonReload = findViewById(R.id.buttonReload)
        buttonSort = findViewById(R.id.buttonSort)
        gridItemList = findViewById(R.id.gridItemList)

        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(applicationContext,
                    AppDatabase::class.java, "database-name").allowMainThreadQueries().build()
        }



        itemModelDao = appDatabase!!.ItemModelDao()
        itemModelListMain = itemModelDao?.getAll() as MutableList<ItemModel>
        itemModelAdapter = ItemModelAdapter(itemModelListMain!!, applicationContext)
        gridItemList!!.setAdapter(itemModelAdapter)
        Api.addItemListListener(object : ICallApi {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @SuppressLint("SetTextI18n")
            override fun OnCallApi() {
                itemModelListMain?.clear()
                itemModelListMain = Api.getItemList() as MutableList<ItemModel>
                var errorString: String? = Api.getError()
                if (errorString?.contains("bit") == true)
                {
                    if (errorString?.contains("git") == true)
                    {
                        itemModelListMain = itemModelDao?.getAll() as MutableList<ItemModel>
                        itemModelAdapter?.updateList(itemModelListMain!!)
                        return
                    }
                    else
                    {
                        itemModelDao?.delGit()
                    }
                }
                else
                {
                    if (errorString?.contains("git") == true)
                    {
                        itemModelDao?.delBit()
                    }
                    else
                    {
                        appDatabase!!.clearAllTables()
                    }
                }

                for (item in itemModelListMain!!)
                {
                    itemModelDao?.insert(item)
                }

                itemModelAdapter?.updateList(itemModelListMain!!)
            }
        })
        buttonReload?.setOnClickListener({
            Api.callApiBit(this@MainActivity)
        })
        buttonSort?.setOnClickListener({
            itemModelListMain?.sortWith(
                compareBy(String.CASE_INSENSITIVE_ORDER, { it.name.toString() })
            )
            itemModelAdapter?.updateList(itemModelListMain!!)

        })
        gridItemList?.setOnItemClickListener({ parent, view, position, _ ->
            val ctx1 = applicationContext
            Start(position, ctx1)
        })
    }


    private fun Start(position: Int, ctx1: Context) {

        val intent = Intent(ctx1, ItemActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ctx1.startActivity(intent)

    }
}