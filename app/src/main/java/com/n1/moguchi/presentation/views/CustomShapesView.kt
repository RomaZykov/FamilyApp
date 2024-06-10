package com.n1.moguchi.presentation.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.CornerPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable

class CustomShapesView(context: Context) : Drawable() {

    private val fillPaint = Paint().apply {
        color = context.getColor(com.n1.moguchi.R.color.blue)
        style = Paint.Style.FILL
        strokeWidth = 8f
        pathEffect = CornerPathEffect(72f)
    }
    private val path: Path = Path()

    override fun draw(canvas: Canvas) {
        val centerHorizontal = bounds.width() / 2f
        val bottomVertical = bounds.height().toFloat()
        path.apply {
            reset()
            moveTo(centerHorizontal, bottomVertical)
            // /
            lineTo(
                centerHorizontal + (bounds.width() / 2),
                bottomVertical - 24
            )
            // |
            lineTo(
                centerHorizontal + (bounds.width() / 2),
                90f
            )
            // \
            lineTo(
                centerHorizontal,
                15f
            )
            // /
            lineTo(
                centerHorizontal - (bounds.width() / 2),
                90f
            )
            // |
            lineTo(
                centerHorizontal - (bounds.width() / 2),
                bottomVertical - 24
            )
            close()
        }
        canvas.drawPath(path, fillPaint)
    }

    override fun setAlpha(alpha: Int) {
        TODO("Not yet implemented")
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        TODO("Not yet implemented")
    }

    override fun getOpacity(): Int =
        // Must be PixelFormat.UNKNOWN, TRANSLUCENT, TRANSPARENT, or OPAQUE
        PixelFormat.OPAQUE
}