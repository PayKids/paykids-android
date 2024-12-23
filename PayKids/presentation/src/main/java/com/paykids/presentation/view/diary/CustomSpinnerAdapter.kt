package com.paykids.presentation.view.diary

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.paykids.presentation.R

class CustomSpinnerAdapter(
    context: Context,
    private val items: Array<String>
) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: inflater.inflate(R.layout.item_spinner, parent, false)

        val itemTextView = view.findViewById<TextView>(R.id.tv_spinner_item)
        itemTextView.text = items[position]

        return view
    }
}
