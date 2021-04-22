package com.zadanierekrutacyjne2

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.bumptech.glide.Glide
import com.zadanierekturacyjne2.model.ItemModel
import com.zadanierekturacyjne2.model.ItemModelDao
import com.zadanierekturacyjne2.settings.AppDatabase


class ItemActivity : AppCompatActivity() {

    var appDatabase: AppDatabase? = null
    var itemModelDao: ItemModelDao? = null

    var imgAvatar: ImageView? = null
    var textName: TextView? = null
    var textUser: TextView? = null
    var textDescription: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)
        val intent = intent
        val id = intent.getIntExtra("id", 0)

        imgAvatar = findViewById(R.id.imgAvatar)
        textName = findViewById(R.id.textName)
        textUser = findViewById(R.id.textUser)
        textDescription = findViewById(R.id.textDescription)

        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "database-name"
            ).allowMainThreadQueries().build()
        }

        itemModelDao = appDatabase!!.ItemModelDao()

        val item: ItemModel? = itemModelDao?.getItem(id.toString())



        textName?.setText(item?.name)
        textUser?.setText(item?.user)
        textDescription?.setText(item?.description)

        imgAvatar?.let {
            Glide.with(applicationContext)
                .load(item?.avatar)
                .timeout(1000)
                .placeholder(R.drawable.baseline_image_24)
                .error(R.drawable.baseline_running_with_errors_24)
                .into(it)
        }

    }
}