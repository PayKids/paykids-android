package com.paykids.presentation.view.quiz

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.paykids.domain.model.ChatItem
import com.paykids.presentation.databinding.ItemChatMyBinding
import com.paykids.presentation.databinding.ItemChatOtherBinding
import com.paykids.presentation.view.OnRvItemClickListener

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

        fun bind(item: ChatItem) {
            binding.messageText.text = item.content
            setMaxWidth(binding.messageText, itemView.context)
        }

        private fun setMaxWidth(textView: TextView, context: Context) {
            val displayMetrics = DisplayMetrics()
            val windowManager =
                context.getSystemService(Context.WINDOW_SERVICE) as android.view.WindowManager
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            val screenWidth = displayMetrics.widthPixels
            val maxWidth = (screenWidth * 0.5).toInt()
            textView.maxWidth = maxWidth
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