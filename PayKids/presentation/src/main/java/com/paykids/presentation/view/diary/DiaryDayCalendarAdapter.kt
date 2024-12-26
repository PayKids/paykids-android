package com.paykids.presentation.view.diary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.paykids.domain.model.DiaryInfo
import com.paykids.presentation.R
import com.paykids.presentation.databinding.ItemDiaryDayBinding
import com.paykids.presentation.view.OnRvItemClickListener

class DiaryDayCalendarAdapter() :
    ListAdapter<Pair<String, DiaryInfo?>, DiaryDayCalendarAdapter.DateViewHolder>(diaryDiffUtil) {

    companion object {
        private val diaryDiffUtil = object : DiffUtil.ItemCallback<Pair<String, DiaryInfo?>>() {
            override fun areItemsTheSame(
                oldItem: Pair<String, DiaryInfo?>,
                newItem: Pair<String, DiaryInfo?>
            ): Boolean =
                oldItem.second?.diaryId == newItem.second?.diaryId

            override fun areContentsTheSame(
                oldItem: Pair<String, DiaryInfo?>,
                newItem: Pair<String, DiaryInfo?>
            ): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val binding =
            ItemDiaryDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        when (getItem(position).first) {
            "previous" -> holder.clear()
            "next" -> {}
            else -> holder.bind(getItem(position))
        }
    }

    inner class DateViewHolder(val binding: ItemDiaryDayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(diaryInfo: Pair<String, DiaryInfo?>) {
            binding.tvDay.text = diaryInfo.first
            binding.tvDay.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
            binding.root.isClickable = true
            binding.root.visibility = View.VISIBLE


            itemView.setOnClickListener {
                rvItemClickListener.onClick(diaryInfo.second!!.diaryId)
            }
        }

        fun clear() {
            binding.tvDay.text = ""
            binding.root.isClickable = false
            binding.root.visibility = View.GONE
        }
    }

    private lateinit var rvItemClickListener: OnRvItemClickListener<Int>

    fun setRvItemClickListener(rvItemClickListener: OnRvItemClickListener<Int>) {
        this.rvItemClickListener = rvItemClickListener
    }
}
