package com.n1.moguchi.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.CornerPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class FirstSpecialShapeView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val fillPaint = Paint().apply {
        color = context.getColor(com.n1.moguchi.R.color.blue)
        style = Paint.Style.FILL
        strokeWidth = 8f
        pathEffect = CornerPathEffect(72f)
    }
    private val path: Path = Path()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val top = paddingTop
        val bottom = height - paddingBottom
        val left = paddingLeft
        val right = width - paddingRight

        val centerHorizontal = width / 2f
        val bottomVertical = height.toFloat()
        path.apply {
            val margin = 50f
            reset()
            moveTo(centerHorizontal, bottomVertical - margin)
            lineTo(
                centerHorizontal + (width / 2) - margin,
                bottomVertical - 90
            )
            lineTo(
                centerHorizontal + (width / 2) - margin,
                90f
            )
            lineTo(
                centerHorizontal,
                15f
            )
            lineTo(
                centerHorizontal - (width / 2) + margin,
                90f
            )
            lineTo(
                centerHorizontal - (width / 2) + margin,
                bottomVertical - 90
            )
            close()
        }
        canvas.drawPath(path, fillPaint)
    }
}