import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.paykids.presentation.R

class QuizMultipleChoiceRvAdapter(
    private val onAnswerClicked: (String) -> Unit
) : ListAdapter<Pair<String, String>, QuizMultipleChoiceRvAdapter.AnswerViewHolder>(DiffCallback) {

    private var selectedAnswerLetter: String? = null
    private var correctAnswerLetter: String? = null

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Pair<String, String>>() {
            override fun areItemsTheSame(
                oldItem: Pair<String, String>,
                newItem: Pair<String, String>
            ): Boolean {
                return oldItem.first == newItem.first
            }

            override fun areContentsTheSame(
                oldItem: Pair<String, String>,
                newItem: Pair<String, String>
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    // 정답 설정 메서드
    fun setCorrectAnswer(answerLetter: String) {
        correctAnswerLetter = answerLetter
    }

    // 선택된 답 갱신 메서드
    fun updateSelectedAnswer(answerLetter: String) {
        selectedAnswerLetter = answerLetter

        // 선택된 항목과 정답 항목만 갱신
        val selectedPosition = currentList.indexOfFirst { it.first == answerLetter }
        val correctPosition = currentList.indexOfFirst { it.first == correctAnswerLetter }

        notifyItemChanged(selectedPosition)
        if (correctPosition != -1) {
            notifyItemChanged(correctPosition)
        }
    }

    inner class AnswerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvAnswer: TextView = itemView.findViewById(R.id.tv_answer)

        fun bind(answer: Pair<String, String>) {
            tvAnswer.text = answer.second // 답의 실제 문자열 표시

            // 선택 상태와 정답 상태에 따른 UI 변경
            when {
                selectedAnswerLetter != correctAnswerLetter && selectedAnswerLetter == answer.first -> {
                    // 틀린 답일 경우 배경을 레드로 설정
                    val redBackground =
                        ContextCompat.getDrawable(itemView.context, R.drawable.shape_quiz_box_red)
                    applyBackgroundWithPadding(tvAnswer, redBackground)
//                    tvAnswer.setTextColor(ContextCompat.getColor(itemView.context, R.color.red))
                }

                correctAnswerLetter == answer.first && selectedAnswerLetter != null -> {
                    // 정답일 경우 배경을 블루로 설정
                    val blueBackground =
                        ContextCompat.getDrawable(itemView.context, R.drawable.shape_quiz_box_blue)
                    applyBackgroundWithPadding(tvAnswer, blueBackground)
//                    tvAnswer.setTextColor(ContextCompat.getColor(itemView.context, R.color.blue1))
                }
            }

            itemView.setOnClickListener { onAnswerClicked(answer.first) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_answer_box_text, parent, false)
        return AnswerViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // 배경 설정 함수 (기존 패딩을 유지)
    private fun applyBackgroundWithPadding(tvAnswer: TextView, backgroundDrawable: Drawable?) {
        // 기존 패딩을 가져오기
        val paddingLeft = tvAnswer.paddingLeft
        val paddingTop = tvAnswer.paddingTop
        val paddingRight = tvAnswer.paddingRight
        val paddingBottom = tvAnswer.paddingBottom

        // 배경을 설정
        tvAnswer.background = backgroundDrawable

        // 배경을 설정한 후 패딩을 다시 적용
        tvAnswer.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
    }

}
