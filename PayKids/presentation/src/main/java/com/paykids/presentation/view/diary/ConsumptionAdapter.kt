package com.paykids.presentation.view.diary

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.paykids.presentation.R
import com.paykids.presentation.databinding.ItemAnalysisConsumptionBinding
import com.paykids.presentation.databinding.ItemEtcCategoryBinding

class ConsumptionAdapter(private val items: List<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val itemList = items.toMutableList()

    companion object {
        private const val TYPE_NORMAL = 0
        private const val TYPE_ETC = 1
    }

    override fun getItemCount(): Int {
        // 항상 기존 아이템 수 + 1 ("기타"를 위해)
        return items.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        // 마지막 항목일 경우 TYPE_ETC
        return if (position == items.size) TYPE_ETC else TYPE_NORMAL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_ETC) {
            EtcViewHolder(ItemEtcCategoryBinding.inflate(inflater, parent, false))
        } else {
            NormalViewHolder(ItemAnalysisConsumptionBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NormalViewHolder) {
            // 일반 항목 데이터 바인딩
            holder.bind(items[position])
        } else if (holder is EtcViewHolder) {
            // "기타" 항목 데이터 바인딩 (필요 시 작업)
        }
    }

    class NormalViewHolder(private val binding: ItemAnalysisConsumptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: String) {
            // 넘겨받은 카테고리 이름 데이터를 TextView에 설정
            binding.tvComsumptionPlace.text = data
        }
    }

    class EtcViewHolder(binding: ItemEtcCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // "기타" 항목 ViewHolder
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<String>) {
        itemList.clear()
        itemList.addAll(newItems)
        notifyDataSetChanged()
    }

}
