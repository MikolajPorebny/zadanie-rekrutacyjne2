package com.zadanierekrutacyjne2.model

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.zadanierekrutacyjne2.R
import com.zadanierekturacyjne2.model.ItemModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


object ItemModelAdapter : BaseAdapter() {


    private var context: Context? = null
    private var itemModelList: List<ItemModel>? = null

    fun ItemModelAdapter(itemModelList: List<ItemModel>?, context: Context?) {
        this.itemModelList = itemModelList
        this.context = context
    }

    override fun getCount(): Int {
        return itemModelList!!.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView
        val itemModel: ItemModel
        itemModel = itemModelList!![position]
        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(context)
            convertView = layoutInflater.inflate(R.layout.item_model_adapter, null)
        }
        val textDate = convertView!!.findViewById<TextView>(R.id.textDate)
        val textTitle = convertView.findViewById<TextView>(R.id.textTitle)
        val textDescription = convertView.findViewById<TextView>(R.id.textDescription)
        val imageItem = convertView.findViewById<ImageView>(R.id.imageItem)
        val adapterLayout: ConstraintLayout = convertView.findViewById(R.id.adapterLayout)
        if (position % 2 == 0) {
            adapterLayout.setBackgroundColor(context!!.resources.getColor(R.color.even))
        } else {
            adapterLayout.setBackgroundColor(context!!.resources.getColor(R.color.white))
        }

/*
        //DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
        try {
            @SuppressLint("SimpleDateFormat")
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(itemModel.modificationDate);
            Calendar rightNow = Calendar.getInstance();
            textDate.setText(date.toLocaleString());
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        val calendar = Calendar.getInstance()
        @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat("yyyy-MM-dd")
        try {
            calendar.time = sdf.parse(itemModel.modificationDate)
            val finalDate = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) +
                    " " + calendar[Calendar.DAY_OF_MONTH] +
                    " " + calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) +
                    " " + calendar[Calendar.YEAR]
            textDate.text = finalDate
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        textTitle.setText(itemModel.title)
        val allMatches: MutableList<String?> = ArrayList()
        val regex = "^.*?(?=http)"
        val pattern = Pattern.compile(regex, Pattern.DOTALL)
        val matcher = pattern.matcher(itemModel.description)
        while (matcher.find()) {
            allMatches.add(matcher.group(0))
        }
        if (allMatches[0] != null) {
            textDescription.text = allMatches[0]
        } else {
            textDescription.setText(itemModel.description)
        }
        Glide.with(context!!)
                .load(itemModel.image_url)
                .timeout(1000)
                .placeholder(R.drawable.baseline_image_24)
                .error(R.drawable.baseline_running_with_errors_24)
                .into(imageItem)
        return convertView
    }

}