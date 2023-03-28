package com.hoomanholding.jpamanager.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.hoomanholding.jpamanager.R
import net.cachapa.expandablelayout.ExpandableLayout


/**
 * create by m-latifi on 3/6/2023
 */
class CustomMenu @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var iconImage: ImageView
    private lateinit var titleText: TextView
    private lateinit var expandable: ExpandableLayout
    private lateinit var constraintParent: ConstraintLayout
    private val defaultColor = R.color.white
    private val selectedColor = R.color.black

    //---------------------------------------------------------------------------------------------- init
    init {
        init(attrs)
    }
    //---------------------------------------------------------------------------------------------- init


    //---------------------------------------------------------------------------------------------- init
    private fun init(attrs: AttributeSet?) {
        View.inflate(context, R.layout.layout_main_footer, this)

        iconImage = findViewById(R.id.image_thumb)
        titleText = findViewById(R.id.text_title)
        expandable = findViewById(R.id.expandable_title)
        constraintParent = findViewById(R.id.constraintParent)


        val ta = context.obtainStyledAttributes(attrs, R.styleable.CustomMenu)
        try {
            val text = ta.getString(R.styleable.CustomMenu_menu_title)
            val drawableId = ta.getResourceId(R.styleable.CustomMenu_menu_icon, 0)
            if (drawableId != 0) {
                val drawable = AppCompatResources.getDrawable(context, drawableId)
                iconImage.setImageDrawable(drawable)
            }
            titleText.text = text
        } finally {
            ta.recycle()
        }

        setOnClickListener {
            selected()
        }
    }
    //---------------------------------------------------------------------------------------------- init


    //---------------------------------------------------------------------------------------------- isSelectedMenu
    fun isSelectedMenu() = expandable.isExpanded
    //---------------------------------------------------------------------------------------------- isSelectedMenu


    //---------------------------------------------------------------------------------------------- selected
    fun selected() {
        if (!expandable.isExpanded) {
            constraintParent.background =
                ContextCompat.getDrawable(context, R.drawable.drawable_button_menu_selected)
            expandable.expand()
            iconImage.setColorFilter(
                ContextCompat.getColor(context, selectedColor),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
    }
    //---------------------------------------------------------------------------------------------- selected


    //---------------------------------------------------------------------------------------------- clearSelected
    fun clearSelected() {
        if (expandable.isExpanded) {
            constraintParent.background =
                ContextCompat.getDrawable(context, R.drawable.drawable_button_menu_default)
            expandable.collapse()
            iconImage.setColorFilter(
                ContextCompat.getColor(context, defaultColor),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
    }
    //---------------------------------------------------------------------------------------------- clearSelected

}