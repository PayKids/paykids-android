package com.paykids.presentation.view.diary

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.paykids.domain.model.DetailConsume
import com.paykids.presentation.databinding.ItemDetailConsumptionBinding
import com.paykids.presentation.utils.Constants

class DetailConsumeAdapter : RecyclerView.Adapter<DetailConsumeAdapter.ViewHolder>() {

    private val items = mutableListOf<DetailConsume>()

    interface OnItemClickListener {
        fun onItemClick()
    }

    var itemClickListener: OnItemClickListener? = null

    inner class ViewHolder(val binding: ItemDetailConsumptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: DetailConsume) {
            val formattedAmount = Constants.formatAmount(item.amount)

            binding.tvComsumptionPlace.text = item.place
            binding.tvConsumeAmount.text = "-${formattedAmount}원"
            binding.tvMemo.text = item.memo
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDetailConsumptionBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<DetailConsume>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}