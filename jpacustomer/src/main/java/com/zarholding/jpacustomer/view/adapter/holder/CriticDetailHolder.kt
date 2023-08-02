package com.zarholding.jpacustomer.view.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.critic.CriticDetailModel

/**
 * Created by zar on 2/15/2023.
 */

abstract class CriticDetailHolder(val view: View) :
    RecyclerView.ViewHolder(view) {

    abstract fun bind(item: CriticDetailModel)

}