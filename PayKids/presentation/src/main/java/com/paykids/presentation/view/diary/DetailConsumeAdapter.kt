package com.paykids.presentation.view.diary

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.paykids.domain.model.DetailConsume
import com.paykids.presentation.databinding.ItemDetailConsumptionBinding

class DetailConsumeAdapter(private var items: List<DetailConsume>) :
    RecyclerView.Adapter<DetailConsumeAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick()
    }

    var itemClickListener: OnItemClickListener? = null

    inner class ViewHolder(val binding: ItemDetailConsumptionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                itemClickListener?.onItemClick()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemDetailConsumptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            tvComsumptionPlace.text = items[position].place
            tvConsumeAmount.text = items[position].amount.toString()
            tvMemo.text = items[position].memo
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<DetailConsume>) {
        items = newItems
        notifyDataSetChanged()
    }
}