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
class CustomMenu @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var iconImage: ImageView
    private lateinit var cardView: CardView
    private lateinit var textViewBadge: TextView
    private val defaultColor = R.color.a_menuIconUnselect
    private val selectedColor = R.color.a_menuIconSelect
    private var selectedMenu = false

    //---------------------------------------------------------------------------------------------- init
    init {
        init(attrs)
    }
    //---------------------------------------------------------------------------------------------- init


    //---------------------------------------------------------------------------------------------- init
    private fun init(attrs: AttributeSet?) {
        View.inflate(context, R.layout.layout_main_menu, this)
        iconImage = findViewById(R.id.imageViewIcon)
        cardView = findViewById(R.id.cardView)
        textViewBadge = findViewById(R.id.textViewBadge)

        textViewBadge.visibility = View.GONE
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CustomMenu)
        try {
            val drawableId = ta.getResourceId(R.styleable.CustomMenu_menu_icon, 0)
            if (drawableId != 0) {
                val drawable = AppCompatResources.getDrawable(context, drawableId)
                iconImage.setImageDrawable(drawable)
            }
            cardView.setCardBackgroundColor(context.getColor(selectedColor))
        } finally {
            ta.recycle()
        }

        setOnClickListener {
            selected()
        }
    }
    //---------------------------------------------------------------------------------------------- init


    //---------------------------------------------------------------------------------------------- isSelectedMenu
    fun isSelectedMenu() = selectedMenu
    //---------------------------------------------------------------------------------------------- isSelectedMenu


    //---------------------------------------------------------------------------------------------- selected
    fun selected() {
        if (!selectedMenu) {
            cardView.visibility = View.VISIBLE
            iconImage.setColorFilter(
                ContextCompat.getColor(context, selectedColor),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
    }
    //---------------------------------------------------------------------------------------------- selected


    //---------------------------------------------------------------------------------------------- clearSelected
    fun clearSelected() {
        cardView.visibility = View.GONE
        iconImage.setColorFilter(
            ContextCompat.getColor(context, defaultColor),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
    }
    //---------------------------------------------------------------------------------------------- clearSelected


    //---------------------------------------------------------------------------------------------- setBadgeCount
    fun setBadgeCount(count: Int) {
        if (count == 0)
            textViewBadge.visibility = View.GONE
        else {
            textViewBadge.text = count.toString()
            textViewBadge.visibility = View.VISIBLE
        }
    }
    //---------------------------------------------------------------------------------------------- setBadgeCount

}