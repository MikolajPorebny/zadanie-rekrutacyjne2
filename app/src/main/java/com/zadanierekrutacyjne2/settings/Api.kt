package com.zadanierekrutacyjne2.settings

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.zadanierekrutacyjne2.R
import com.zadanierekrutacyjne2.model.loadmodel.ItemModelApiBit
import com.zadanierekrutacyjne2.model.loadmodel.ItemModelApiGit
import com.zadanierekturacyjne2.model.ItemModel
import java.util.*


object  Api {

    private val TAG = "Pobieranie danych..."
    var listItem = mutableListOf<ItemModel>()
    private val ICallApi: MutableList<ICallApi> = ArrayList()
    private val ICallApiOnStart: MutableList<ICallApiOnStart> = ArrayList()
    private var errorString: String = "error "



    var TIMEOUT_MS = 10000

    fun addItemListListener(l: ICallApi) {
        ICallApi.add(l)
    }

    fun addItemListListenerOnStart(l: ICallApiOnStart) {
        ICallApiOnStart.add(l)
    }

    fun getItemList(): List<ItemModel> {
        return listItem
    }

    fun getError(): String? {
        return errorString
    }

    fun CallListners() {

        for (l in ICallApi) {
            l.OnCallApi()
        }
    }

    fun CallListnersOnStart() {
        for (l in ICallApiOnStart) {
            l.OnCallApiOnStart()
        }
    }


    fun setItemListBitSync(value: List<ItemModelApiBit?>) {
        listItem.clear()
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

    fun callApiBit(ctx: Context?) {
        val dialog: ProgressDialog
        dialog = ProgressDialog(ctx)
        dialog.setMessage("Prosz?? czeka??...")
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
                    callApiGit(ctx)
                    dialog.dismiss()
                }) { error ->
            errorString += "bit "
            dialog.dismiss()
            val alertDialog = AlertDialog.Builder(ctx, R.style.AlertDialog).create()
            alertDialog.setMessage("B????d pobierania danych z Bitbucket!\nTe dane b??d?? za??adowane z pami??ci podr??cznej.")
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK") { dialog, which ->
                alertDialog.dismiss()
                callApiGit(ctx)
            }
            alertDialog.setOnShowListener { alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK) }
            alertDialog.show()
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(
                TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)
    }

    fun callApiGit(ctx: Context?)
    {
        val dialog: ProgressDialog
        dialog = ProgressDialog(ctx)
        dialog.setMessage("Prosz?? czeka??...")
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
                    CallListners()
                }) { error ->
            errorString += "git"
            dialog.dismiss()
            val alertDialog = AlertDialog.Builder(ctx, R.style.AlertDialog).create()
            alertDialog.setMessage("B????d pobierania danych z Gita!\nTe dane b??d?? za??adowane z pami??ci podr??cznej.")
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK") { dialog, which ->
                alertDialog.dismiss()
                CallListners()
            }
            alertDialog.setOnShowListener { alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK) }
            alertDialog.show()
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
        dialog.setMessage("Prosz?? czeka??...")
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
            errorString += "bit "
            dialog.dismiss()
            val alertDialog = AlertDialog.Builder(ctx, R.style.AlertDialog).create()
            alertDialog.setMessage("B????d pobierania danych z Bitbucket!\nTe dane b??d?? za??adowane z pami??ci podr??cznej.")
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK") { dialog, which ->
                alertDialog.dismiss()
                callApiOnStarGit(ctx)
            }
            alertDialog.setOnShowListener { alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK) }
            alertDialog.show()

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
        dialog.setMessage("Prosz?? czeka??...")
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
                    CallListnersOnStart()
                }) { error ->
            errorString += "git"
            dialog.dismiss()
            val alertDialog = AlertDialog.Builder(ctx, R.style.AlertDialog).create()
            alertDialog.setMessage("B????d pobierania danych z Gita!\nTe dane b??d?? za??adowane z pami??ci podr??cznej.")
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK") { dialog, which ->
                alertDialog.dismiss()
                CallListnersOnStart()
            }
            alertDialog.setOnShowListener { alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK) }
            alertDialog.show()
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(
                TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)
    }

}