package com.zarholding.jpacustomer.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.zarholding.jpacustomer.R

/**
 * create by m-latifi on 3/6/2023
 */

class CustomStep @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var viewLineLeft: View
    private lateinit var viewLineRight: View
    private lateinit var iconImage: ImageView
    private lateinit var cardView: CardView
    private lateinit var titleView: TextView
    private val defaultColor = R.color.cardViewColor
    private val selectedColor = R.color.stepSelect

    //---------------------------------------------------------------------------------------------- init
    init {
        init(attrs)
    }
    //---------------------------------------------------------------------------------------------- init


    //---------------------------------------------------------------------------------------------- init
    private fun init(attrs: AttributeSet?) {
        View.inflate(context, R.layout.layout_step, this)

        viewLineLeft = findViewById(R.id.viewLineLeft)
        viewLineRight = findViewById(R.id.viewLineRight)
        cardView = findViewById(R.id.cardView)
        iconImage = findViewById(R.id.imageViewIcon)
        titleView = findViewById(R.id.textViewTitle)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.CustomStep)
        try {
            val drawableId = ta.getResourceId(R.styleable.CustomStep_step_icon, 0)
            if (drawableId != 0) {
                val drawable = AppCompatResources.getDrawable(context, drawableId)
                iconImage.setImageDrawable(drawable)
            }
            cardView.setCardBackgroundColor(context.getColor(selectedColor))
            val title = ta.getString(R.styleable.CustomStep_step_title)
            titleView.text = title
        } finally {
            ta.recycle()
        }
    }
    //---------------------------------------------------------------------------------------------- init


    //---------------------------------------------------------------------------------------------- selected
    fun selected() {
        viewLineLeft.setBackgroundColor(context.getColor(selectedColor))
        viewLineRight.setBackgroundColor(context.getColor(selectedColor))
        cardView.setCardBackgroundColor(context.getColor(selectedColor))
    }
    //---------------------------------------------------------------------------------------------- selected


    //---------------------------------------------------------------------------------------------- clearSelected
    fun clearSelected() {
        viewLineLeft.setBackgroundColor(context.getColor(defaultColor))
        viewLineRight.setBackgroundColor(context.getColor(defaultColor))
        cardView.setCardBackgroundColor(context.getColor(defaultColor))
    }
    //---------------------------------------------------------------------------------------------- clearSelected

}