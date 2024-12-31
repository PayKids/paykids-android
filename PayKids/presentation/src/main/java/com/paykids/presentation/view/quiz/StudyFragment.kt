package com.paykids.presentation.view.quiz

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.paykids.domain.model.ChatItem
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentStudyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudyFragment : BaseFragment<FragmentStudyBinding>() {
    private val viewModel: StudyViewModel by viewModels()
    private lateinit var studyAdapter: StudyRvAdapter

    override fun initView() {
        setRvAdapter()
    }

    override fun initListener() {
        super.initListener()

        binding.ibBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.ibSend.setOnClickListener {
            if (binding.etSendChat.text.isNotEmpty()) {
                val processedItems = processChatItems(
                    listOf(
                        ChatItem(
                            chatId = studyAdapter.getLastChatId() + 1,
                            content = binding.etSendChat.text.toString(),
                            isMine = true
                        )
                    )
                )

//                submitCustom(processedItems)
//                viewModel.sendQuestion(binding.etSendChat.text.toString())
                binding.etSendChat.text.clear()
            } else {
                showToast("작성된 내용이 없어요")
            }
        }

    }

    private fun setRvAdapter() {
        studyAdapter = StudyRvAdapter()
        binding.rvChat.adapter = studyAdapter
        binding.rvChat.layoutManager = LinearLayoutManager(requireContext()).apply {
            stackFromEnd = true
        }
    }

    private fun processChatItems(dataList: List<ChatItem>): MutableList<ChatItem> {
        val processedList = mutableListOf<ChatItem>()
        var lastOtherMessageIndex = -1

        dataList.forEachIndexed { index, item ->
            processedList.add(ChatItem(chatId = item.chatId, content = "", isMine = false))

            if (!item.isMine) {
                lastOtherMessageIndex = index
            }

//            processedList.add(item.copy(showRefreshIcon = showRefreshIcon))
        }

        return processedList
    }
}