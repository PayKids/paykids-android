package com.paykids.presentation.view.quiz

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.paykids.domain.model.ChatItem
import com.paykids.presentation.databinding.ItemChatMyBinding
import com.paykids.presentation.databinding.ItemChatOtherBinding

class StudyRvAdapter : ListAdapter<ChatItem, RecyclerView.ViewHolder>(chatDiffCallback) {

    companion object {
        private const val VIEW_TYPE_MY_MESSAGE = 1
        private const val VIEW_TYPE_OTHER_MESSAGE = 2

        private val chatDiffCallback = object : DiffUtil.ItemCallback<ChatItem>() {
            override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
                return oldItem.chatId == newItem.chatId
            }

            override fun areContentsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when {
            item.isMine -> VIEW_TYPE_MY_MESSAGE
            else -> VIEW_TYPE_OTHER_MESSAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_MY_MESSAGE -> MyChatViewHolder.from(parent)
            VIEW_TYPE_OTHER_MESSAGE -> OtherChatViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MyChatViewHolder -> holder.bind(getItem(position))
            is OtherChatViewHolder -> {
                holder.bind(getItem(position))
            }
        }
    }

    class MyChatViewHolder private constructor(private val binding: ItemChatMyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChatItem) {
            binding.tvNickname.text = item.nickname
            binding.messageText.text = item.content
            setMaxWidth(binding.messageText, itemView.context)
        }

        private fun setMaxWidth(textView: TextView, context: Context) {
            val displayMetrics = DisplayMetrics()
            val windowManager =
                context.getSystemService(Context.WINDOW_SERVICE) as android.view.WindowManager
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            val screenWidth = displayMetrics.widthPixels
            val maxWidth = (screenWidth * 0.7).toInt()
            textView.maxWidth = maxWidth
        }

        companion object {
            fun from(parent: ViewGroup): MyChatViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = ItemChatMyBinding.inflate(layoutInflater, parent, false)
                return MyChatViewHolder(view)
            }
        }
    }

    class OtherChatViewHolder private constructor(val binding: ItemChatOtherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.R)
        fun bind(item: ChatItem) {
            binding.tvChatOtherName.text = item.nickname
            binding.messageText.text = item.content
            setMaxWidth(binding.messageText, itemView.context)
        }

        @RequiresApi(Build.VERSION_CODES.R)
        private fun setMaxWidth(textView: TextView, context: Context) {
            val windowManager =
                context.getSystemService(Context.WINDOW_SERVICE) as? android.view.WindowManager
            windowManager?.let {
                val windowMetrics = it.currentWindowMetrics
                val bounds = windowMetrics.bounds
                val screenWidth = bounds.width()
                val maxWidth = (screenWidth * if (itemViewType == VIEW_TYPE_MY_MESSAGE) 0.7 else 0.5).toInt()
                textView.maxWidth = maxWidth
            }
        }

        companion object {
            fun from(parent: ViewGroup): OtherChatViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = ItemChatOtherBinding.inflate(layoutInflater, parent, false)
                return OtherChatViewHolder(view)
            }
        }
    }

    fun getLastChatId(): Int {
        return if (itemCount > 0) {
            getItem(itemCount - 1).chatId
        } else {
            0
        }
    }

}