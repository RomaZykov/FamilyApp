package com.n1.moguchi.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.n1.moguchi.R
import kotlin.math.max

class CurveProgressBarView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val arcSpace = RectF()
    private var backgroundPaint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = Color.GRAY
    }
    private var progressPaint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
    }
    private var backgroundProgressBarWidth: Float =
        resources.getDimension(R.dimen.default_background_stroke_width)
        set(value) {
            field = value.dpToPx()
            backgroundPaint.strokeWidth = field
            requestLayout()
            invalidate()
        }
    private var roundBorder = true
        set(value) {
            field = value
            backgroundPaint.strokeCap = if (field) Paint.Cap.ROUND else Paint.Cap.BUTT
            invalidate()
        }

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val attributes =
            context.theme.obtainStyledAttributes(attrs, R.styleable.CurveProgressBar, 0, 0)

        backgroundProgressBarWidth = attributes.getDimension(
            R.styleable.CurveProgressBar_background_progressbar_width,
            backgroundProgressBarWidth
        ).pxToDp()
        roundBorder = attributes.getBoolean(R.styleable.CurveProgressBar_round_border, roundBorder)

        attributes.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minWidth = getDefaultSize(300, widthMeasureSpec)
        val minHeight = suggestedMinimumHeight + paddingTop + paddingBottom
        val desiredHeight = max(minHeight, 100)
        setMeasuredDimension(
            resolveSize(minWidth, widthMeasureSpec),
            resolveSize(desiredHeight, heightMeasureSpec)
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        setSpace()
        canvas.apply {
            drawArc(this)
        }
    }

    private fun setSpace() {
        val horizontalCenter = (width.div(2)).toFloat()
        val verticalCenter = (height.div(2)).toFloat()
        val xLeft = arcSpace.left
        val yTop = arcSpace.top
        val xEnd = arcSpace.right
        val yBottom = arcSpace.bottom
        arcSpace.set(
            horizontalCenter - xLeft / 2,
            verticalCenter,
            horizontalCenter + xEnd / 2,
            verticalCenter + yBottom + yTop
        )
    }

    private fun drawArc(canvas: Canvas) {
        canvas.drawArc(arcSpace, 180f, 180f, false, backgroundPaint)
    }

    // region Extensions Utils
    private fun Float.dpToPx(): Float =
        this * resources.displayMetrics.density

    private fun Float.pxToDp(): Float =
        this / resources.displayMetrics.density
}