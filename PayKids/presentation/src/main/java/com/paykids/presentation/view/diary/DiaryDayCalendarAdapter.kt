package com.paykids.presentation.view.diary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.paykids.domain.model.DayInfo
import com.paykids.presentation.R
import com.paykids.presentation.databinding.ItemDiaryDayBinding
import com.paykids.presentation.view.OnRvItemClickListener

class DiaryDayCalendarAdapter :
    ListAdapter<Pair<String, DayInfo?>, DiaryDayCalendarAdapter.DateViewHolder>(diaryDiffUtil) {

    companion object {
        private val diaryDiffUtil = object : DiffUtil.ItemCallback<Pair<String, DayInfo?>>() {
            override fun areItemsTheSame(
                oldItem: Pair<String, DayInfo?>,
                newItem: Pair<String, DayInfo?>
            ): Boolean {
                return oldItem.first == newItem.first
            }

            override fun areContentsTheSame(
                oldItem: Pair<String, DayInfo?>,
                newItem: Pair<String, DayInfo?>
            ): Boolean {
                // DayInfo를 비교하는 부분 (null 체크 포함)
                return oldItem.second == newItem.second
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val binding =
            ItemDiaryDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val dateAndConsume = getItem(position)
        if (dateAndConsume.first != "previous" && dateAndConsume.first != "next") {
            holder.bind(dateAndConsume)
        } else {
            holder.clear()
        }
    }

    inner class DateViewHolder(val binding: ItemDiaryDayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dateAndConsume: Pair<String, DayInfo?>) {
            val date = dateAndConsume.first
            val detailConsume = dateAndConsume.second

            binding.tvDay.text = date
            binding.tvIncome.text = detailConsume?.income.toString() ?: "0"
            binding.tvConsume.text = detailConsume?.consume.toString() ?: "0"

            binding.tvDay.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
            binding.root.isClickable = true
            binding.root.visibility = View.VISIBLE

            itemView.setOnClickListener {
                rvItemClickListener.onClick(date.toInt())
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