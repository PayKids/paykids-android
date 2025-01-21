package com.paykids.presentation.view.quest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.paykids.domain.model.quest.QuestItem
import com.paykids.presentation.R
import com.paykids.presentation.databinding.ItemQuestBinding

class QuestAdapter(
    private val questItems: List<QuestItem>
) : RecyclerView.Adapter<QuestAdapter.QuestViewHolder>() {

    inner class QuestViewHolder(private val binding: ItemQuestBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: QuestItem) {
            binding.tvQuestName.text = item.name
            binding.tvQuestProgress.text = "1/2"
            setProgressBarStatus(item.progress)
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
}
