package com.paykids.presentation.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.paykids.presentation.R

class CategoryProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val sectionPaint = Paint(ANTI_ALIAS_FLAG)
    private val textPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        typeface = ResourcesCompat.getFont(context, R.font.nanumsquare_bold)
    }

    private var sections: List<Float> = emptyList()
    private var colors: List<Int> = emptyList()
    private var categoryNames: List<String> = emptyList()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val totalWidth = width.toFloat()
        var startX = 0f

        for (i in sections.indices) {
            val sectionWidth = totalWidth * sections[i]
            sectionPaint.color = colors[i]

            // progress bar그리기
            canvas.drawRect(startX, 0f, startX + sectionWidth, height.toFloat(), sectionPaint)

            // 카테고리명 표시
            val sectionCenterX = startX + sectionWidth / 2
            val sectionCenterY = height / 2f
            textPaint.textSize = 32f

            val textBaseline = sectionCenterY - (textPaint.descent() + textPaint.ascent()) / 2
            categoryNames.getOrNull(i)?.let {
                canvas.drawText(it, sectionCenterX, textBaseline, textPaint)
            }

            startX += sectionWidth
        }
    }

    fun updateSections(
        newSections: List<Float>,
        newColors: List<Int>,
        newCategoryNames: List<String>
    ) {
        require(newSections.sum() <= 100f) { "퍼센트의 총합은 100.00이하여야 합니다." }

        sections = newSections
        colors = newColors.take(newSections.size).toMutableList().apply {
            while (size < newSections.size) {
                add(Color.LTGRAY)
            }
        }
        categoryNames = newCategoryNames
        invalidate()
    }
}
