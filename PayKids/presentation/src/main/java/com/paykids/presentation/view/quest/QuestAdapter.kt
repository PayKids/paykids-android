package com.paykids.presentation.view.quest

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.paykids.domain.model.quest.QuestInfo
import com.paykids.presentation.databinding.ItemQuestBinding

class QuestAdapter : RecyclerView.Adapter<QuestAdapter.QuestViewHolder>() {
    private var questItems = mutableListOf<QuestInfo>()

    inner class QuestViewHolder(private val binding: ItemQuestBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: QuestInfo) {
            binding.tvQuestName.text = item.name
            binding.tvQuestProgress.text = "${item.count} / ${item.maxCount}"
            setProgressBarStatus(item.count.toFloat() / item.maxCount)
        }

        private fun setProgressBarStatus(progress: Float) {
            binding.progressBarQuest.progress = (progress * 100).toInt()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestViewHolder {
        val binding = ItemQuestBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestViewHolder, position: Int) {
        holder.bind(questItems[position])
    }

    override fun getItemCount(): Int = questItems.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateQuests(newQuests: List<QuestInfo>) {
        questItems.clear()
        questItems.addAll(newQuests)
        notifyDataSetChanged()
    }
}