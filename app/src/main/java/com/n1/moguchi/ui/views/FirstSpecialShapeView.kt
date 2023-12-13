package com.n1.moguchi.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.n1.moguchi.R

class FirstSpecialShapeView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val fillPaint = Paint().apply {
        color = context.getColor(R.color.blue)
        style = Paint.Style.FILL
        strokeWidth = 8f
    }
    private val path: Path = Path()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val top = paddingTop
        val bottom = height - paddingBottom
        val left = paddingLeft
        val right = width - paddingRight

        val centerHorizontal = width / 2f // total = 412
        val bottomVertical = height.toFloat() // total = 915
        val sides = 6
        val angle = 2.0 * Math.PI / 5
        path.moveTo(centerHorizontal, bottomVertical - 50)
        // /
        path.lineTo(
            centerHorizontal + (width / 2) - 50f,
            bottomVertical - 90f
        )
        // |
        path.lineTo(
            centerHorizontal + (width / 2) - 50f,
            90f
        )
        // \
        path.lineTo(
            centerHorizontal,
            45f
        )
        // /
        path.lineTo(
            centerHorizontal - (width / 2) + 50f,
            90f
        )
        // |
//        path.lineTo(
//            centerHorizontal - (width / 2) + 50f,
//            bottomVertical - 800
//        )
        // \
        path.lineTo(
            centerHorizontal - (width / 2) + 50f,
            bottomVertical - 90
        )
//        for (i in 1 until sides) {
//        }
        path.close()
        canvas.drawPath(path, fillPaint)
    }

    // region Extensions Utils
    private fun Float.dpToPx(): Float =
        this * resources.displayMetrics.density

    private fun Float.pxToDp(): Float =
        this / resources.displayMetrics.density
}