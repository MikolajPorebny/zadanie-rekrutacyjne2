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


@SuppressLint("StaticFieldLeak")
class ItemModelAdapter(var itemModelListAdapter: MutableList<ItemModel>, val context: Context?) : BaseAdapter() {


    /*
    fun ItemModelAdapter(itemModelList: List<ItemModel>, context: Context?) {
        this.itemModelList = itemModelList
        this.context = context
    }*/

    override fun getCount(): Int {
        return itemModelListAdapter.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun updateList(newlist: List<ItemModel>) {
        //itemModelListAdapter.clear()
        //itemModelListAdapter.addAll(newlist)
        itemModelListAdapter = newlist as MutableList<ItemModel>
        notifyDataSetChanged()
    }



    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        val itemModel: ItemModel
        itemModel = itemModelListAdapter[position]
        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(context)
            convertView = layoutInflater.inflate(R.layout.item_model_adapter, null)
        }
        val textName = convertView?.findViewById<TextView>(R.id.textName)
        val imgAvatar = convertView?.findViewById<ImageView>(R.id.imgAvatar)
        val textUser = convertView?.findViewById<TextView>(R.id.textUser)

        val adapterLayout: ConstraintLayout = convertView!!.findViewById(R.id.adapterLayout)
        if (position % 2 == 0) {
            if (itemModel.repo=="bit")
            {
                adapterLayout.setBackgroundColor(context!!.resources.getColor(R.color.blue_even))
            }
            else
            {
                adapterLayout.setBackgroundColor(context!!.resources.getColor(R.color.even))
            }
        }
        else
        {
            if (itemModel.repo=="bit")
            {
                adapterLayout.setBackgroundColor(context!!.resources.getColor(R.color.blue))
            }
            else
            {
                adapterLayout.setBackgroundColor(context!!.resources.getColor(R.color.white))
            }
        }


        textName?.setText(itemModel.name)
        textUser?.setText(itemModel.user)
        imgAvatar?.let {
            Glide.with(context)
                    .load(itemModel.avatar)
                    .timeout(1000)
                    .placeholder(R.drawable.baseline_image_24)
                    .error(R.drawable.baseline_running_with_errors_24)
                    .into(it)
        }
        return convertView
    }

}