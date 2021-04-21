package com.zadanierekrutacyjne2.settings

import android.app.ProgressDialog
import android.content.Context
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.zadanierekrutacyjne2.model.loadmodel.ItemModelApiBit
import com.zadanierekrutacyjne2.model.loadmodel.ItemModelApiGit
import com.zadanierekturacyjne2.model.ItemModel
import java.util.*


object  Api {

    private val TAG = "Pobieranie danych..."
    private var listItem = mutableListOf<ItemModel>()
    private val ICallApi: MutableList<ICallApi> = ArrayList()
    private val ICallApiError: MutableList<ICallApiError> = ArrayList()
    private val ICallApiOnStart: MutableList<ICallApiOnStart> = ArrayList()
    private val ICallApiErrorOnStart: MutableList<ICallApiErrorOnStart> = ArrayList()
    private var error: String? = null



    var TIMEOUT_MS = 10000


    fun getItemList(): List<ItemModel?>? {
        return listItem
    }
    //public static String getError() {return error;}

    //public static String getError() {return error;}
    fun setItemList(value: List<ItemModel>?) {
        listItem = value as MutableList<ItemModel>
        for (l in ICallApi) {
            l.OnCallApi()
        }
    }

    fun setError(value: String?) {
        error = value
        for (l in ICallApiError) {
            l.OnCallApiError()
        }
    }

    fun setItemListBitSync(value: List<ItemModelApiBit?>) {
        for (item in value)
        {
            val itemModel = ItemModel()
            itemModel?.description = item?.description
            itemModel?.name = item?.name
            itemModel?.user = item?.owner?.nickname
            itemModel?.avatar = item?.owner?.links?.avatar?.href
            itemModel?.repo = "bit"
            listItem.add(itemModel)
        }

    }

    fun setItemListGitSync(value: List<ItemModelApiGit?>) {
        for (item in value)
        {
            val itemModel = ItemModel()
            itemModel?.description = item?.description
            itemModel?.name = item?.full_name
            itemModel?.user = item?.owner?.login
            itemModel?.avatar = item?.owner?.avatar_url
            itemModel?.repo = "git"
            listItem.add(itemModel)
        }
    }

    fun setItemListOnStart(value: List<ItemModel>?) {
        listItem = value as MutableList<ItemModel>
        for (l in ICallApiOnStart) {
            l.OnCallApiOnStart()
        }
    }

    fun setErrorOnStart(value: String?) {
        error = value
        for (l in ICallApiErrorOnStart) {
            l.OnCallApiErrorOnStart()
        }
    }


    fun addItemListListener(l: ICallApi) {
        ICallApi.add(l)
    }

    fun addApiErrorListener(l: ICallApiError) {
        ICallApiError.add(l)
    }

    fun addItemListListenerOnStart(l: ICallApiOnStart) {
        ICallApiOnStart.add(l)
    }

    fun addApiErrorListenerOnStart(l: ICallApiErrorOnStart) {
        ICallApiErrorOnStart.add(l)
    }


    fun callApi(ctx: Context?) {
        val dialog: ProgressDialog
        dialog = ProgressDialog(ctx)
        dialog.setMessage("Proszę czekać...")
        dialog.setCancelable(false)
        dialog.setInverseBackgroundForced(false)
        dialog.setCancelable(true)
        dialog.show()
        val queue = Volley.newRequestQueue(ctx)
        val url = "https://api.github.com/repositories"
        val stringRequest = StringRequest(Request.Method.GET, url,
                { response ->
                    //listItem?.toMutableList()?.clear()



                    val listType = object : TypeToken<List<ItemModel?>?>() {}.type
                    val gSon = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
                    val lokalStan = gSon.fromJson<List<ItemModel?>>(response, listType)


                    dialog.dismiss()
                }) { error ->
            dialog.dismiss()
            setError(error.message)
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(
                TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)
    }


    fun callApiOnStartBit(ctx: Context?) {

        val dialog: ProgressDialog
        dialog = ProgressDialog(ctx)
        dialog.setMessage("Proszę czekać...")
        dialog.setCancelable(false)
        dialog.setInverseBackgroundForced(false)
        dialog.setCancelable(true)
        dialog.show()
        val queue = Volley.newRequestQueue(ctx)
        val url = "https://api.bitbucket.org/2.0/repositories?fields=values.name,values.owner,values.description"
        val stringRequest = StringRequest(Request.Method.GET, url,
                { response ->
                    val listType = object : TypeToken<List<ItemModelApiBit?>?>() {}.type
                    val gSon = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()

                    val index = response.indexOf(':')
                    val substringedResponse1 = response.substring(index+1)
                    val substringedResponse2 = substringedResponse1.substringBeforeLast('}')

                    val lokalStan = gSon.fromJson<List<ItemModelApiBit?>>(substringedResponse2, listType)

                    setItemListBitSync(lokalStan)
                    callApiOnStarGit(ctx)
                    dialog.dismiss()
                }) { error ->
            callApiOnStarGit(ctx)
            setErrorOnStart(error.message)
            dialog.dismiss()
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(
                TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)
    }

    fun callApiOnStarGit(ctx: Context?) {

        val dialog: ProgressDialog
        dialog = ProgressDialog(ctx)
        dialog.setMessage("Proszę czekać...")
        dialog.setCancelable(false)
        dialog.setInverseBackgroundForced(false)
        dialog.setCancelable(true)
        dialog.show()

        val queue = Volley.newRequestQueue(ctx)
        val url = "https://api.github.com/repositories"
        val stringRequest = StringRequest(Request.Method.GET, url,
                { response ->
                    val listType = object : TypeToken<List<ItemModelApiGit>?>() {}.type
                    val gSon = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
                    val lokalStan = gSon.fromJson<List<ItemModelApiGit?>>(response, listType)

                    setItemListGitSync(lokalStan)
                    dialog.dismiss()
                }) { error ->
            setErrorOnStart(error.message)
            dialog.dismiss()
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(
                TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)
    }

}