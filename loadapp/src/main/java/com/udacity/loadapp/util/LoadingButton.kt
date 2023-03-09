package com.udacity.loadapp.util

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.databinding.BindingAdapter
import com.udacity.loadapp.ButtonState
import com.udacity.loadapp.R
import timber.log.Timber
import kotlin.math.abs
import kotlin.properties.Delegates


class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var centerPointF: PointF = PointF()

    private val paint = Paint().apply {
        color = Color.WHITE
        textSize = resources.getDimension(R.dimen.default_text)
        textAlign = Paint.Align.CENTER
        style = Paint.Style.FILL
    }

    private var textWidth = 0f
    private var textHeight = 0f
    private var defaultText: String = ""
    private var loadingText: String = ""

    private val loadingArcColor = ContextCompat.getColor(context, R.color.orange_500)
    private val loadingRectColor = ContextCompat.getColor(context, R.color.teal_900)
    private var loadingAnimator: ValueAnimator? = null

    private var arcOffset = resources.getDimensionPixelOffset(R.dimen.arch_offset)
    private var sweepAngle = 360F
    private var loadingWidth = 0F

    private var buttonState: ButtonState by Delegates.observable(ButtonState.None) { _, _, newValue ->
        when (newValue) {
            is ButtonState.None -> {
                Timber.d("Button is none")
            }
            is ButtonState.Clicked -> {
                Timber.d("Button is clicked")
            }
            is ButtonState.Loading -> {
                changeText(loadingText)
                startAnimation()
            }
            is ButtonState.Completed -> {
                changeText(defaultText)
                loadingWidth = 0F
                sweepAngle = 0F
                stopAnimation()
                buttonState = ButtonState.None
            }
        }
    }

    private fun changeText(text: String) {
        val rect = Rect()
        paint.getTextBounds(text.uppercase(), 0, text.length, rect)
        textHeight = rect.height().toFloat()
        textWidth = paint.measureText(text)
    }

    private fun startAnimation() {
        loadingAnimator?.start()
    }

    private fun stopAnimation() {
        loadingAnimator?.end()
    }

    init {
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            defaultText = getString(R.styleable.LoadingButton_defaultText) ?: ""
            loadingText = getString(R.styleable.LoadingButton_loadingText) ?: ""
        }
        changeText(defaultText)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return

        if (buttonState == ButtonState.Loading) {
            drawLoadingState(canvas)
        } else {
            drawDefaultState(canvas)
        }
    }

    private fun drawDefaultState(canvas: Canvas) {
        paint.color = Color.WHITE
        canvas.drawText(
            defaultText, centerPointF.x, centerPointF.y + abs(textHeight / 2), paint
        )
    }

    private fun drawLoadingState(canvas: Canvas) {
        paint.color = loadingRectColor
        paint.style = Paint.Style.FILL
        canvas.drawRect(0f, 0f, loadingWidth, centerPointF.y * 2, paint)

        paint.color = Color.WHITE
        canvas.drawText(
            loadingText,
            centerPointF.x - arcOffset / 2,
            centerPointF.y + textHeight / 2,
            paint
        )

        paint.color = loadingArcColor
        paint.style = Paint.Style.FILL
        canvas.drawArc(
            centerPointF.x + textWidth / 2 - arcOffset / 2,
            centerPointF.y - arcOffset / 2,
            centerPointF.x + textWidth / 2 + arcOffset / 2,
            centerPointF.y + arcOffset / 2,
            0f,
            sweepAngle,
            true,
            paint
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerPointF.x = (w / 2).toFloat()
        centerPointF.y = (h / 2).toFloat()
        createCircleAnimation(w)
    }

    private fun createCircleAnimation(width: Int) {
        loadingAnimator = ValueAnimator.ofFloat(0f, 360f).apply {
            duration = 3000
            addUpdateListener {
                val animatedValue = it.animatedValue as Float
                sweepAngle = animatedValue
                loadingWidth = (width * animatedValue / 360f)
                invalidate()
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.RESTART
            }
        }
    }

    fun setState(buttonState: ButtonState) {
        this.buttonState = buttonState
    }

    override fun performClick(): Boolean {
        if (buttonState == ButtonState.Loading) {
            return false
        }
        return super.performClick()
    }
}

@BindingAdapter("state")
fun LoadingButton.changeButtonState(state: ButtonState?) {
    state?.let {
        setState(state)
    }
}