package com.zarholding.jpacustomer.view.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.basket.DetailBasketModel

/**
 * Created by m-latifi on 6/13/2023.
 */

abstract class BasketHolder(val view: View): RecyclerView.ViewHolder(view) {

    abstract fun bind(item: DetailBasketModel, position: Int)
}