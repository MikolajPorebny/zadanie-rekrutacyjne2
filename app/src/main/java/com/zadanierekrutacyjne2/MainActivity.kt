package com.zadanierekrutacyjne2

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.zadanierekrutacyjne2.model.ItemModelAdapter
import com.zadanierekrutacyjne2.settings.Api
import com.zadanierekrutacyjne2.settings.ICallApi
import com.zadanierekrutacyjne2.settings.ICallApiError
import com.zadanierekturacyjne2.model.ItemModel
import com.zadanierekturacyjne2.model.ItemModelDao
import com.zadanierekturacyjne2.settings.AppDatabase

import java.util.*
import java.util.regex.Pattern


class MainActivity : AppCompatActivity() {
    var itemModelList: MutableList<ItemModel>? = null

    //boolean isTablet = false;
    var buttonReload: TextView? = null
    var appDatabase: AppDatabase? = null
    var itemModelDao: ItemModelDao? = null
    var gridItemList: GridView? = null
    var webDetail: WebView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonReload = findViewById(R.id.buttonReload)
        gridItemList = findViewById(R.id.gridItemList)

        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(applicationContext,
                    AppDatabase::class.java, "database-name").allowMainThreadQueries().build()
        }
        itemModelDao = appDatabase!!.ItemModelDao()
        itemModelList = itemModelDao?.getAll() as MutableList<ItemModel>
        val itemModelAdapter = ItemModelAdapter(itemModelList!!, applicationContext)
        gridItemList!!.setAdapter(itemModelAdapter)
        Api.addItemListListener(object : ICallApi {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @SuppressLint("SetTextI18n")
            override fun OnCallApi() {
                itemModelList = Api.getItemList() as MutableList<ItemModel>?
                appDatabase!!.clearAllTables()
                itemModelDao!!::insert?.let { itemModelList!!.forEach(it) }
                itemModelList?.clear()
                itemModelList = itemModelDao!!.getAll() as MutableList<ItemModel>?
            }
        })
        Api.addApiErrorListener(object : ICallApiError {
            override fun OnCallApiError() {
                val alertDialog = AlertDialog.Builder(this@MainActivity, R.style.AlertDialog).create()
                alertDialog.setMessage("Błąd pobierania danych!\nDane będą załadowane z pamięci podręcznej.")
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK") { dialog, which -> dialog.dismiss() }
                alertDialog.setOnShowListener { alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.black)) }
                alertDialog.show()
                itemModelList = itemModelDao?.getAll() as MutableList<ItemModel>?
            }
        })
        buttonReload?.setOnClickListener(View.OnClickListener { Api.callApi(this@MainActivity) })
        gridItemList?.setOnItemClickListener(OnItemClickListener { parent, view, position, _ ->
            val ctx1 = applicationContext
            Start(position, ctx1)
        })
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun Start(position: Int, ctx1: Context) {
        var http = ""
        val allMatches: MutableList<String?> = ArrayList()
        val regex = "http(?!.*http).*$"
        val pattern = Pattern.compile(regex, Pattern.DOTALL)
        val matcher = pattern.matcher(itemModelDao!!.getItem(position))
        while (matcher.find()) {
            allMatches.add(matcher.group(0))
        }
        if (allMatches[0] != null) {
            http = allMatches[0].toString()
        }
        /*if (isTablet) {



            webDetail.loadUrl(http);
            WebSettings webSettings = webDetail.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setSupportMultipleWindows(true);

            webDetail.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onReceivedTitle(WebView view, String title) {
                    getWindow().setTitle(title);
                }
            });

            webDetail.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return false;
                }
            });

        }
        else
        {*/
        val intent = Intent(ctx1, ItemActivity::class.java)
        intent.putExtra("http", http)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ctx1.startActivity(intent)
        //}
    }
}