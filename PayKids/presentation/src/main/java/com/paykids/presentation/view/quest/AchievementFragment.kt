package com.paykids.presentation.view.quest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentAchievementBinding
import com.paykids.util.LoggerUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AchievementFragment : BaseFragment<FragmentAchievementBinding>() {
    override fun initView() {
        LoggerUtils.d("Achieve")
    }

}
