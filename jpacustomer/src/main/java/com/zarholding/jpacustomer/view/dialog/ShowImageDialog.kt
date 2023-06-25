package com.zarholding.jpacustomer.view.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import com.hoomanholding.applibrary.ext.downloadProfileImage
import com.hoomanholding.applibrary.model.data.enums.EnumSystemType
import com.hoomanholding.applibrary.view.custom.TouchImageView
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.model.ShowImageModel

/**
 * Created by m-latifi on 11/26/2022.
 */

class ShowImageDialog(
    context: Context,
    private val item : ShowImageModel
) : Dialog(context) {


    //---------------------------------------------------------------------------------------------- onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_show_image)
        val lp = WindowManager.LayoutParams()
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        this.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        this.window?.setGravity(Gravity.CENTER)
        lp.copyFrom(this.window?.attributes)
        lp.horizontalMargin = 50f
        this.window?.attributes = lp
    }
    //---------------------------------------------------------------------------------------------- onCreate



    //---------------------------------------------------------------------------------------------- onStart
    override fun onStart() {
        initDialog()
        super.onStart()
    }
    //---------------------------------------------------------------------------------------------- onStart



    //---------------------------------------------------------------------------------------------- initDialog
    private fun initDialog() {
        val imageViewClose = this.findViewById<ImageView>(R.id.imageViewClose)
        val touchImageView = this.findViewById<TouchImageView>(R.id.touchImageView)
        touchImageView.downloadProfileImage(
            url = item.imageName,
            systemType = EnumSystemType.Customers.name,
            entityType = item.entityType,
            token = item.token,
            placeholder = R.drawable.a_ic_logo)
        imageViewClose.setOnClickListener {
            dismiss()
        }
    }
    //---------------------------------------------------------------------------------------------- initDialog

}