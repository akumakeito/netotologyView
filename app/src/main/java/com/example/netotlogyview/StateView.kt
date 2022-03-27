package com.example.netotlogyview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.withStyledAttributes
import kotlin.math.min
import kotlin.random.Random

class StateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    var data: List<Float> = emptyList()
    set(value) {
        field = value
        update()
    }



    private var radius = 0F
    private var center = PointF(0F, 0F)
    private var oval = RectF(0F, 0F,0F, 0F)

    private var lineWidth = AndroidUtils.dp(context, 15F).toFloat()
    private var fontSize = AndroidUtils.dp(context, 40F).toFloat()
    private var colors = emptyList<Int>()

    private val partsCount = 4F
    private var sumOfElement = 0F
    private var hundredPercentSum = 0F

    private var progress = 0F
    private var valueAnimator : ValueAnimator? = null
    private var endValue = 1F



    init {
        context.withStyledAttributes(attrs, R.styleable.StateView) {
            lineWidth = getDimension(R.styleable.StateView_lineWidth, lineWidth)
            fontSize = getDimension(R.styleable.StateView_fontSize, fontSize)
            colors = listOf(
                getColor(R.styleable.StateView_color1, getRandomColor()),
                getColor(R.styleable.StateView_color2, getRandomColor()),
                getColor(R.styleable.StateView_color3, getRandomColor()),
                getColor(R.styleable.StateView_color4, getRandomColor()),
            )

        }
    }

    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = lineWidth

    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = fontSize
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = min(w,h) / 2F - lineWidth / 2
        center = PointF(w / 2F, h / 2F)
        oval = RectF(
            center.x - radius, center.y - radius,
            center.x + radius, center.y + radius
        )
    }

    override fun onDraw(canvas: Canvas) {
        if (data.isEmpty()) {
            return
        }

        canvas.drawText(
                "%.2f%%".format(getPercent(hundredPercentSum, sumOfElement)),
        center.x,
        center.y + textPaint.textSize / 4F,
        textPaint
        )

        hundredPercentSum = data[0] * partsCount
        sumOfElement = data.sum()

        var startAngle = -90F


        for ((index, datum) in data.withIndex()) {
            val angle = 360F * getPercent(hundredPercentSum, datum)/100
            strokePaint.color = colors.getOrNull(index) ?: getRandomColor()
            canvas.drawArc(oval, startAngle + (progress * 360), angle * progress, false, strokePaint)
            startAngle += angle
        }
        strokePaint.color = colors[0]

            canvas.drawArc(oval, -90F + (progress *360), 1F, false, strokePaint)
    }

    private fun getRandomColor() = Random.nextInt(0xFF000000.toInt(), 0xFFFFFFFF.toInt())

    private fun getPercent(sumOfElement : Float, element: Float): Float = element * 100 / sumOfElement

    private fun update() {
        valueAnimator?.let {
            it.removeAllListeners()
            it.cancel()
        }

        progress = 0F


        valueAnimator = ValueAnimator.ofFloat(progress, endValue).apply {
            addUpdateListener { anim ->
                progress = anim.animatedValue as Float
                invalidate()
            }
            duration = 3_000
            interpolator = LinearInterpolator()
        }.also {
            it.start()
        }
    }
}