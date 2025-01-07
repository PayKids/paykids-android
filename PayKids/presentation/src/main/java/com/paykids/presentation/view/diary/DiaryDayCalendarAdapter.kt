package com.paykids.presentation.view.diary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.paykids.presentation.R
import com.paykids.presentation.databinding.ItemDiaryDayBinding
import com.paykids.presentation.view.OnRvItemClickListener
import java.util.Calendar

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
            holder.bind(dateAndConsume, isToday(dateAndConsume.first))
        } else {
            holder.clear()
        }
    }

    inner class DateViewHolder(val binding: ItemDiaryDayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dateAndConsume: Pair<String, DayInfo?>, isToday: Boolean) {
            val date = dateAndConsume.first
            val detailConsume = dateAndConsume.second
            val day = date.split("-").lastOrNull() ?: ""

            binding.tvDay.text = day
            binding.tvIncome.text = when {
                detailConsume?.income == null || detailConsume.income == 0 -> ""
                else -> "+${detailConsume.income}"
            }
            binding.tvConsume.text = when {
                detailConsume?.consume == null || detailConsume.consume == 0 -> ""
                else -> "-${detailConsume.consume}"
            }
            binding.root.isClickable = true
            binding.root.visibility = View.VISIBLE

            binding.root.setBackgroundResource(0)
            binding.tvDay.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
            if (isToday) {
                binding.ivDiaryCheck.setBackgroundResource(R.drawable.shape_bg_day)
                binding.tvDay.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
            }

            itemView.setOnClickListener {
                rvItemClickListener.onClick(day.toInt())
            }
        }

        fun clear() {
            binding.tvDay.text = ""
            binding.tvIncome.text = ""
            binding.tvConsume.text = ""
            binding.root.isClickable = false
            binding.root.visibility = View.GONE
        }
    }

    private lateinit var rvItemClickListener: OnRvItemClickListener<Int>

    fun setRvItemClickListener(rvItemClickListener: OnRvItemClickListener<Int>) {
        this.rvItemClickListener = rvItemClickListener
    }

    private fun isToday(date: String): Boolean {
        // 특수 값('previous', 'next')은 오늘로 간주하지 않음
        if (date == "previous" || date == "next") return false
        if (!date.contains("-")) return false

        val dateParts = date.split("-")
        if (dateParts.size != 3) return false

        return try {
            val year = dateParts[0].toInt()
            val month = dateParts[1].toInt()
            val day = dateParts[2].toInt()

            val calendar = Calendar.getInstance()
            val todayYear = calendar.get(Calendar.YEAR)
            val todayMonth = calendar.get(Calendar.MONTH) + 1
            val todayDay = calendar.get(Calendar.DAY_OF_MONTH)

            todayYear == year && todayMonth == month && todayDay == day

        } catch (e: NumberFormatException) {
            false
        }
    }
}