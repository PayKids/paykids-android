package com.paykids.presentation.view.quiz

import android.annotation.SuppressLint
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
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
    private lateinit var backPressedCallback: OnBackPressedCallback
    private val args: StudyFragmentArgs by navArgs()
    private val viewModel: StudyViewModel by viewModels()
    private lateinit var studyAdapter: StudyRvAdapter

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("SetTextI18n")
    override fun initView() {
        setStatusBarColorLight()
        hideStatusBar()

        val stageNumber = args.stageNumber
        binding.tvStage.text = "스테이지 $stageNumber"
        setRvAdapter()
        viewModel.getUserInfo()

        setupKeyboardVisibilityListener()
    }

    override fun initListener() {
        super.initListener()

        binding.etSendChat.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) showKeyboardAndFocus(v)
        }

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
                        nickname = "chatGPT"
                    )

                    val updatedList = studyAdapter.currentList.toMutableList().apply {
                        add(gptResponse)
                    }

                    studyAdapter.submitList(updatedList) {
                        binding.rvChat.post {
                            binding.rvChat.scrollToPosition(studyAdapter.itemCount - 1)
                        }
                    }
                }
            }
        }
    }

    private fun sendMessage() {
        val messageContent = binding.etSendChat.text.toString()
        if (messageContent.isBlank()) return
        val currentNickname = viewModel.userNickname.value ?: return

        val newMessage = ChatItem(
            chatId = studyAdapter.itemCount + 1,
            isMine = true,
            nickname = currentNickname,
            content = messageContent
        )

        val newList = studyAdapter.currentList.toMutableList()
        newList.add(newMessage)
        studyAdapter.submitList(newList) {
            binding.rvChat.scrollToPosition(studyAdapter.itemCount - 1)
        }

        viewModel.sendQuestion(messageContent)
        binding.etSendChat.text.clear()

        hideKeyboard()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )
    }

    private fun setupKeyboardVisibilityListener() {
        val rootView = requireView()
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = rootView.height
            val keyboardHeight = screenHeight - rect.bottom

            if (keyboardHeight > screenHeight * 0.15) {
                binding.flChatInput.translationY = -keyboardHeight.toFloat()
                binding.rvChat.post {
                    binding.rvChat.scrollToPosition(studyAdapter.itemCount - 1)
                }
            } else {
                binding.flChatInput.translationY = 0f
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