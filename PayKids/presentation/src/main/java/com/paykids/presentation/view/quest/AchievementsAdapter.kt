package com.paykids.presentation.view.quest

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.paykids.domain.model.achievement.AchievementInfo
import com.paykids.presentation.databinding.ItemAchievementBinding

class AchievementsAdapter(
    private var achievements: List<AchievementInfo>
) : RecyclerView.Adapter<AchievementsAdapter.AchievementViewHolder>() {

    inner class AchievementViewHolder(private val binding: ItemAchievementBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AchievementInfo) {
            Glide.with(binding.root.context)
                .load(item.imageURL)
                .transform(CircleCrop())
                .into(binding.ivAchieveImage)

            binding.tvAchieveName.text = item.name
            binding.tvAchieveDesc.text = item.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
        val binding = ItemAchievementBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AchievementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
        holder.bind(achievements[position])
    }

    override fun getItemCount(): Int = achievements.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateAchievements(newAchievements: List<AchievementInfo>) {
        achievements = newAchievements
        notifyDataSetChanged()
    }
}