package com.paykids.presentation.view.quiz

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.paykids.domain.model.ChatItem
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentStudyBinding
import com.paykids.presentation.utils.UiState
import com.paykids.presentation.view.home.HomeActivity
import com.paykids.util.LoggerUtils
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
            val chatContent = binding.etSendChat.text.toString()
            if (chatContent.isNotEmpty()) {
                val userChat = ChatItem(
                    chatId = studyAdapter.getLastChatId() + 1,
                    content = chatContent,
                    isMine = true
                )

                val updatedList = studyAdapter.currentList.toMutableList().apply {
                    add(userChat)
                }
                studyAdapter.submitList(updatedList)
                binding.rvChat.scrollToPosition(updatedList.size - 1)

                viewModel.sendQuestion(chatContent)

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
            processedList.add(item)

            if (!item.isMine) {
                lastOtherMessageIndex = index
            }

//            processedList.add(item.copy(showRefreshIcon = showRefreshIcon))
        }

        return processedList
    }

    override fun setObserver() {
        super.setObserver()

        viewModel.resState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    showToast("앗, 답변을 준비하는데 실패했어요.\n다시 한번 시도해 볼까요?")
                    LoggerUtils.e(it.message)
                }

                is UiState.Success -> {
                    val gptResponse = ChatItem(
                        chatId = studyAdapter.getLastChatId() + 1,
                        content = it.data,
                        isMine = false
                    )

                    val updatedList = studyAdapter.currentList.toMutableList().apply {
                        add(gptResponse)
                    }
                    studyAdapter.submitList(updatedList)
                    binding.rvChat.scrollToPosition(updatedList.size - 1)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as? HomeActivity)?.setBottomNavigationVisibility(false)
    }

    override fun onPause() {
        super.onPause()
        (requireActivity() as? HomeActivity)?.setBottomNavigationVisibility(true)
    }
}