import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.paykids.presentation.R

class QuizMultipleChoiceRvAdapter(
    private val onAnswerClicked: (String) -> Unit
) : ListAdapter<String, QuizMultipleChoiceRvAdapter.AnswerViewHolder>(DiffCallback) {

    inner class AnswerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvAnswer: TextView = itemView.findViewById(R.id.tv_answer)

        fun bind(answer: String) {
            tvAnswer.text = answer
            itemView.setOnClickListener { onAnswerClicked(answer) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_answer_box_text, parent, false)
        return AnswerViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                // 두 항목이 동일한지 비교 (예: 고유 ID로 비교 가능)
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                // 항목의 내용이 동일한지 비교
                return oldItem == newItem
            }
        }
    }
}
