package com.paykids.presentation.custom

import android.annotation.SuppressLint
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

    private val paint = Paint(ANTI_ALIAS_FLAG)
    private var sections: List<Float> = emptyList()
    private var colors: List<Int> = emptyList()
    private var categoryNames: List<String> = emptyList()

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val totalWidth = width.toFloat()
        var startX = 0f

        for (i in sections.indices) {
            val sectionWidth = totalWidth * sections[i]
            paint.color = colors.getOrElse(i) { Color.LTGRAY }
            canvas.drawRect(startX, 0f, startX + sectionWidth, height.toFloat(), paint)

            val sectionCenterX = startX + sectionWidth / 2
            val sectionCenterY = height / 2f
            paint.color = Color.WHITE
            paint.textAlign = Paint.Align.CENTER
            paint.textSize = 36f
            paint.typeface = ResourcesCompat.getFont(context, R.font.nanumsquare_bold)
            val textHeight = paint.fontMetrics.bottom - paint.fontMetrics.top
            val textBaseline = sectionCenterY + (textHeight / 2) - paint.fontMetrics.bottom

            categoryNames.getOrNull(i)?.let {
                canvas.drawText(it, sectionCenterX, textBaseline, paint)
            }
            startX += sectionWidth
        }
    }

    fun updateSections(
        newSections: List<Float>,
        newColors: List<Int>,
        newCategoryNames: List<String>
    ) {
        sections = newSections
        colors = newColors
        categoryNames = newCategoryNames
        invalidate()
    }
}
