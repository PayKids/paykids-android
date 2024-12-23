package com.paykids.presentation.view.diary

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paykids.domain.model.DetailConsume
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.custom.ConfirmDialogInterface
import com.paykids.presentation.databinding.FragmentDiaryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiaryFragment : BaseFragment<FragmentDiaryBinding>(), ConfirmDialogInterface {

    private val items = arrayListOf(
        DetailConsume("편의점", -1800, "메모1"),
        DetailConsume("편의점", -1800, "메모2"),
        DetailConsume("편의점", -1800, "메모3"),
        DetailConsume("편의점", -1800, "메모4")
    )

    override fun initView() {
    }

    override fun initListener() {
        super.initListener()

        binding.ibAddPocketMoney.setOnClickListener {
            val dialog = DiaryDialog(this)
            dialog.isCancelable = true
            dialog.show(parentFragmentManager, "AddPocketMoneyDialog")
        }

        val adapter = DetailConsumeAdapter(items)
        binding.rvDetailConsume.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            this.adapter = adapter
        }
    }

    override fun onYesButtonClick() {

    }

}