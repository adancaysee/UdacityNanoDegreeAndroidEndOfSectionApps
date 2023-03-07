package com.udacity.loadapp.util

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.udacity.loadapp.ButtonState
import com.udacity.loadapp.R
import kotlin.math.abs
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var centerPointF: PointF = PointF()
    private val circleColor = ContextCompat.getColor(context, R.color.orange_500)

    private val loadingText = resources.getString(R.string.button_loading)

    private val paint = Paint().apply {
        color = Color.WHITE
        textSize = resources.getDimension(R.dimen.default_text)
        textAlign = Paint.Align.CENTER
        style = Paint.Style.FILL
    }

    val r = Rect()
    private var text: String = ""

    private var circleAnimator: ValueAnimator

    private var sweepAngle = 0f

    init {
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            text = getString(R.styleable.LoadingButton_text) ?: ""
        }
        paint.getTextBounds(text.uppercase(), 0, text.length, r)

        circleAnimator = ValueAnimator.ofFloat(0f,360f).apply {
            duration = 5000
            addUpdateListener {
                sweepAngle = it.animatedValue as Float
                invalidate()
            }
        }
        circleAnimator.start()

    }

    private var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { _, _, _ ->

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return

        drawLoading(canvas)

    }

    private fun drawDefault(canvas: Canvas) {
        canvas.drawText(
            text, centerPointF.x, centerPointF.y + abs(r.height() / 2), paint
        )
    }

    private fun drawLoading(canvas: Canvas) {
        text = loadingText
        paint.color = Color.WHITE
        canvas.drawText(
            text, centerPointF.x - r.height(), centerPointF.y + abs(r.height() / 2), paint
        )
        paint.color = circleColor
        paint.style = Paint.Style.FILL
        canvas.drawArc(
            centerPointF.x + r.width() / 2,
            centerPointF.y - r.height(),
            centerPointF.x + r.width() / 2 + 2 * r.height(),
            centerPointF.y + r.height(),
            0f,
            sweepAngle,
            true,
            paint
        )

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minW: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minW, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w), heightMeasureSpec, 0
        )
        centerPointF.x = (w / 2).toFloat()
        centerPointF.y = (h / 2).toFloat()
        setMeasuredDimension(w, h)
    }

    override fun performClick(): Boolean {
        Toast.makeText(context, "Perform click", Toast.LENGTH_LONG).show()
        return super.performClick()

    }
}






/*
paint.color = Color.RED
        canvas.drawLine(centerPointF.x, 0f, centerPointF.x, centerPointF.y * 2, paint)
        canvas.drawLine(0f, centerPointF.y, centerPointF.x * 2, centerPointF.y, paint)

 */