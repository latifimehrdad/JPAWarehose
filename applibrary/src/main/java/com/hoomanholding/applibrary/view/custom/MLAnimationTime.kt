package com.hoomanholding.applibrary.view.custom

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.hoomanholding.applibrary.R
import kotlin.math.roundToInt


/**
 * create by m-latifi on 5/3/2023
 */

class MLAnimationTime : LinearLayout {
    private val context: Context
    private var ta: TypedArray? = null
    private var textViewHourCurrent: TextView? = null
    private var textViewHourNext: TextView? = null
    private var textViewMinuteCurrent: TextView? = null
    private var textViewMinuteNext: TextView? = null
    private var textViewSecondCurrent: TextView? = null
    private var textViewSecondNext: TextView? = null
    private var animationExit: Animation? = null
    private var animationEnter: Animation? = null
    private var textSize = 0
    private var hour = 0
    private var minute = 0
    private var second = 0

    //______________________________________________________________________________________________ ML_AnimationTime
    constructor(context: Context) : super(context) {
        this.context = context
    }

    //______________________________________________________________________________________________ ML_AnimationTime


    //______________________________________________________________________________________________ ML_AnimationTime
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.context = context
        init(attrs)
    }

    //______________________________________________________________________________________________ ML_AnimationTime


    //______________________________________________________________________________________________ init
    private fun init(attrs: AttributeSet?) {
        ta = getContext().obtainStyledAttributes(attrs, R.styleable.MLAnimationTime)
        configLayout()
        when (ta!!.getInt(R.styleable.MLAnimationTime_at_showing, 0)) {
            0 -> configSecond()
            1 -> {
                configMinute()
                addSeparator()
                configSecond()
            }
            2 -> {
                configHours()
                addSeparator()
                configMinute()
                addSeparator()
                configSecond()
            }
        }
    }

    //______________________________________________________________________________________________ init


    //______________________________________________________________________________________________ configLayout
    private fun configLayout() {
        setBackgroundColor(Color.TRANSPARENT)
        gravity = Gravity.CENTER
        orientation = HORIZONTAL
        textSize = (ta!!.getDimensionPixelSize(
            R.styleable.MLAnimationTime_at_textSize,
            0
        ) / resources.displayMetrics.density).toInt()
        second = ta!!.getInt(R.styleable.MLAnimationTime_at_second, 0)
        hour = second / 3600
        minute = second % 3600 / 60
        second %= 60
    }
    //______________________________________________________________________________________________ configLayout


    //______________________________________________________________________________________________ addSeparator
    private fun addSeparator() {
        var separatorColor = ta!!.getColor(R.styleable.MLAnimationTime_at_separatorTimeColor, 0)
        if (separatorColor == 0) separatorColor =
            ta!!.getColor(R.styleable.MLAnimationTime_android_textColor, 0)
        val textView = TextView(context)
        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        textView.setPadding(10, 10, 10, 10)
        textView.setTextColor(separatorColor)
        textView.textSize = (textSize * 1.5).roundToInt().toFloat()
        textView.typeface = Typeface.DEFAULT_BOLD
        textView.text = ":"
        textView.maxLines = 1
        val fontFamilyId = ta!!.getResourceId(R.styleable.MLAnimationTime_fontFamily, 0)
        if (fontFamilyId > 0) textView.typeface = ResourcesCompat.getFont(
            getContext(),
            fontFamilyId
        )
        val gravity = ta!!.getInt(R.styleable.MLAnimationTime_at_gravity, 0x11)
        textView.gravity = gravity
        params.setMargins(5, 0, 5, 0)
        textView.layoutParams = params
        addView(textView, params)
    }

    //______________________________________________________________________________________________ addSeparator


    //______________________________________________________________________________________________ configTextView
    private fun configTextView(): TextView {
        val textView = TextView(context)
        textView.background = ta!!.getDrawable(R.styleable.MLAnimationTime_at_back)
        val params = LayoutParams(0, LayoutParams.MATCH_PARENT, 1f)
        textView.setPadding(25, 0, 25, 0)
        textView.setTextColor(ta!!.getColor(R.styleable.MLAnimationTime_android_textColor, 0))
        textView.textSize = textSize.toFloat()
        textView.maxLines = 1
        val fontFamilyId = ta!!.getResourceId(R.styleable.MLAnimationTime_fontFamily, 0)
        if (fontFamilyId > 0) textView.typeface = ResourcesCompat.getFont(
            getContext(),
            fontFamilyId
        )
        val gravity = ta!!.getInt(R.styleable.MLAnimationTime_at_gravity, 0x11)
        textView.gravity = gravity
        textView.layoutParams = params
        return textView
    }

    //______________________________________________________________________________________________ configTextView


    //______________________________________________________________________________________________ configHours
    private fun configHours() {
        textViewHourCurrent = configTextView()
        textViewHourNext = configTextView()
        textViewHourCurrent!!.visibility = VISIBLE
        textViewHourNext!!.visibility = GONE
        textViewHourCurrent!!.text = String.format("%02d", hour)
        val linearLayout = LinearLayout(context)
        linearLayout.gravity = Gravity.CENTER
        val params = LayoutParams(0, LayoutParams.MATCH_PARENT, 1f)
        linearLayout.layoutParams = params
        linearLayout.addView(textViewHourCurrent, textViewHourCurrent!!.layoutParams)
        linearLayout.addView(textViewHourNext, textViewHourNext!!.layoutParams)
        addView(linearLayout, linearLayout.layoutParams)
    }

    //______________________________________________________________________________________________ configHours


    //______________________________________________________________________________________________ configMinute
    private fun configMinute() {
        textViewMinuteCurrent = configTextView()
        textViewMinuteNext = configTextView()
        textViewMinuteCurrent!!.visibility = VISIBLE
        textViewMinuteNext!!.visibility = GONE
        textViewMinuteCurrent!!.text = String.format("%02d", minute)
        val linearLayout = LinearLayout(context)
        linearLayout.gravity = Gravity.CENTER
        val params = LayoutParams(0, LayoutParams.MATCH_PARENT, 1f)
        linearLayout.layoutParams = params
        linearLayout.addView(textViewMinuteCurrent, textViewMinuteCurrent!!.layoutParams)
        linearLayout.addView(textViewMinuteNext, textViewMinuteNext!!.layoutParams)
        addView(linearLayout, linearLayout.layoutParams)
    }

    //______________________________________________________________________________________________ configMinute


    //______________________________________________________________________________________________ configMinute
    private fun configSecond() {
        textViewSecondCurrent = configTextView()
        textViewSecondNext = configTextView()
        textViewSecondCurrent!!.visibility = VISIBLE
        textViewSecondNext!!.visibility = GONE
        textViewSecondCurrent!!.text = String.format("%02d", second)
        val linearLayout = LinearLayout(context)
        linearLayout.gravity = Gravity.CENTER
        val params = LayoutParams(0, LayoutParams.MATCH_PARENT, 1f)
        linearLayout.layoutParams = params
        linearLayout.addView(textViewSecondCurrent, textViewSecondCurrent!!.layoutParams)
        linearLayout.addView(textViewSecondNext, textViewSecondNext!!.layoutParams)
        addView(linearLayout, linearLayout.layoutParams)
    }

    //______________________________________________________________________________________________ configMinute


    //______________________________________________________________________________________________ changeTime
    fun changeTime(totalSecond: Int) {
        val nHour = totalSecond / 3600
        val nMinute = totalSecond % 3600 / 60
        val nSecond = totalSecond % 60
        if (ta!!.getInt(R.styleable.MLAnimationTime_at_showing, 0) > 1) if (nHour != hour) {
            hour = nHour
            changeHour()
        }
        if (ta!!.getInt(R.styleable.MLAnimationTime_at_showing, 0) > 0) if (nMinute != minute) {
            minute = nMinute
            changeMinute()
        }
        if (nSecond != second) {
            second = nSecond
            changeSecond()
        }
    }

    //______________________________________________________________________________________________ changeTime


    //______________________________________________________________________________________________ changeHour
    private fun changeHour() {
        configAnimation()
        val time = String.format("%02d", hour)
        if (textViewHourCurrent!!.visibility == View.VISIBLE) {
            textViewHourNext!!.text = time
            textViewHourCurrent!!.startAnimation(animationExit)
            textViewHourNext!!.startAnimation(animationEnter)
            textViewHourCurrent!!.visibility = GONE
            textViewHourNext!!.visibility = VISIBLE
        } else {
            textViewHourCurrent!!.text = time
            textViewHourCurrent!!.startAnimation(animationEnter)
            textViewHourNext!!.startAnimation(animationExit)
            textViewHourNext!!.visibility = GONE
            textViewHourCurrent!!.visibility = VISIBLE
        }
    }

    //______________________________________________________________________________________________ changeHour


    //______________________________________________________________________________________________ changeMinute
    private fun changeMinute() {
        configAnimation()
        val time = String.format("%02d", minute)
        if (textViewMinuteCurrent!!.visibility == View.VISIBLE) {
            textViewMinuteNext!!.text = time
            textViewMinuteCurrent!!.startAnimation(animationExit)
            textViewMinuteNext!!.startAnimation(animationEnter)
            textViewMinuteCurrent!!.visibility = GONE
            textViewMinuteNext!!.visibility = VISIBLE
        } else {
            textViewMinuteCurrent!!.text = time
            textViewMinuteCurrent!!.startAnimation(animationEnter)
            textViewMinuteNext!!.startAnimation(animationExit)
            textViewMinuteNext!!.visibility = GONE
            textViewMinuteCurrent!!.visibility = VISIBLE
        }
    }

    //______________________________________________________________________________________________ changeMinute


    //______________________________________________________________________________________________ changeSecond
    private fun changeSecond() {
        configAnimation()
        val time = String.format("%02d", second)
        if (textViewSecondCurrent!!.visibility == View.VISIBLE) {
            textViewSecondNext!!.text = time
            textViewSecondCurrent!!.startAnimation(animationExit)
            textViewSecondNext!!.startAnimation(animationEnter)
            textViewSecondCurrent!!.visibility = GONE
            textViewSecondNext!!.visibility = VISIBLE
        } else {
            textViewSecondCurrent!!.text = time
            textViewSecondCurrent!!.startAnimation(animationEnter)
            textViewSecondNext!!.startAnimation(animationExit)
            textViewSecondNext!!.visibility = GONE
            textViewSecondCurrent!!.visibility = VISIBLE
        }
    }

    //______________________________________________________________________________________________ changeSecond


    //______________________________________________________________________________________________ configAnimation
    private fun configAnimation() {
        if (ta!!.getBoolean(R.styleable.MLAnimationTime_at_elapseTime, false)) {
            animationExit = AnimationUtils.loadAnimation(context, R.anim.slide_out_top)
            animationEnter = AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom)
        } else {
            animationExit = AnimationUtils.loadAnimation(context, R.anim.slide_out_bottom)
            animationEnter = AnimationUtils.loadAnimation(context, R.anim.slide_in_top)
        }
    } //______________________________________________________________________________________________ configAnimation
}