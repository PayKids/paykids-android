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
        viewModel.getUserInfo()
    }

    override fun initListener() {
        super.initListener()

        binding.ibBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.ibSend.setOnClickListener {
            sendMessage()
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

        viewModel.userInfoState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> {
                    showToast(it.message)
                }

                is UiState.Loading -> {}

                is UiState.Success -> {
                    viewModel.setUserNickname(it.data.nickname)
                }
            }
        }



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
                        isMine = false,
                        nickname = viewModel.userNickname.value.toString()
                    )

                    val updatedList = studyAdapter.currentList.toMutableList().apply {
                        add(gptResponse)
                    }

                    studyAdapter.submitList(updatedList)
                    binding.rvChat.smoothScrollToPosition(studyAdapter.itemCount - 1)
                }
            }
        }
    }

    private fun sendMessage() {
        val newMessage = ChatItem(
            chatId = studyAdapter.itemCount + 1,
            isMine = true,
            nickname = viewModel.userNickname.value.toString(),
            content = binding.etSendChat.text.toString()
        )

        val newList = studyAdapter.currentList.toMutableList()
        newList.add(newMessage)
        studyAdapter.submitList(newList) {
            binding.rvChat.scrollToPosition(studyAdapter.itemCount - 1)
        }

        viewModel.sendQuestion(binding.etSendChat.text.toString())
        binding.rvChat.smoothScrollToPosition(studyAdapter.itemCount - 1)
        binding.etSendChat.text.clear()
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