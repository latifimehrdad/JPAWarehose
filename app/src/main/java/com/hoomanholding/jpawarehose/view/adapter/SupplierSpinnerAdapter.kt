package com.hoomanholding.jpawarehose.view.adapter

import android.graphics.Color
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.jpawarehose.databinding.ItemSpinnerSupplierBinding
import com.hoomanholding.jpawarehose.model.database.entity.BrandEntity
import com.skydoves.powerspinner.*
import com.skydoves.powerspinner.databinding.PowerspinnerItemDefaultPowerBinding

/**
 * Create by Mehrdad on 1/26/2023
 */

/** definition of the non-value of Int type. */
internal const val NO_INT_VALUE: Int = Int.MIN_VALUE

/** definition of the non-selected index value. */
internal const val NO_SELECTED_INDEX: Int = -1


class SupplierSpinnerAdapter(
    powerSpinnerView: PowerSpinnerView
) : RecyclerView.Adapter<SupplierSpinnerAdapter.SupplierSpinnerViewHolder>(),
    PowerSpinnerInterface<BrandEntity> {

    override var index: Int = powerSpinnerView.selectedIndex
    override val spinnerView: PowerSpinnerView = powerSpinnerView
    override var onSpinnerItemSelectedListener: OnSpinnerItemSelectedListener<BrandEntity>? = null

    private val compoundPadding: Int = 12
    private val spinnerItems: MutableList<BrandEntity> = arrayListOf()

    init {
        this.spinnerView.compoundDrawablePadding = compoundPadding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupplierSpinnerViewHolder {
        val binding =
            ItemSpinnerSupplierBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return SupplierSpinnerViewHolder(binding).apply {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition.takeIf { it != RecyclerView.NO_POSITION }
                    ?: return@setOnClickListener
                notifyItemSelected(position)
            }
        }
    }

    override fun onBindViewHolder(holder: SupplierSpinnerViewHolder, position: Int) {
        holder.bind(spinnerItems[position], spinnerView)
    }

    override fun setItems(itemList: List<BrandEntity>) {
        this.spinnerItems.clear()
        this.spinnerItems.addAll(itemList)
        this.index = NO_SELECTED_INDEX
        notifyDataSetChanged()
    }

    override fun notifyItemSelected(index: Int) {
        if (index == NO_SELECTED_INDEX) return
        val item = spinnerItems[index]
        spinnerView.compoundDrawablePadding = spinnerView.compoundDrawablePadding
        spinnerView.applyCompoundDrawables(spinnerView, item)
        val oldIndex = this.index
        this.index = index
        this.spinnerView.notifyItemSelected(index, item.brandName ?: "")
        this.onSpinnerItemSelectedListener?.onItemSelected(
            oldIndex = oldIndex,
            oldItem = oldIndex.takeIf { it != NO_SELECTED_INDEX }?.let { spinnerItems[oldIndex] },
            newIndex = index,
            newItem = item
        )
    }

    override fun getItemCount(): Int = this.spinnerItems.size

    inner class SupplierSpinnerViewHolder(private val binding: ItemSpinnerSupplierBinding) :
        RecyclerView.ViewHolder(binding.root) {

        internal fun bind(item: BrandEntity, spinnerView: PowerSpinnerView) {
            binding.itemDefaultText.apply {
                text = item.brandName
                setTextSize(TypedValue.COMPLEX_UNIT_PX, spinnerView.textSize)
                setTextColor(spinnerView.currentTextColor)
                compoundDrawablePadding = spinnerView.compoundDrawablePadding
                applyCompoundDrawables(spinnerView, item)
                binding.root.setPadding(
                    spinnerView.paddingLeft,
                    spinnerView.paddingTop,
                    spinnerView.paddingRight,
                    spinnerView.paddingBottom
                )
/*                if (spinnerView.spinnerItemHeight != NO_INT_VALUE) {
                    binding.root.height = spinnerView.spinnerItemHeight
                }*/
            }
            binding.cardViewColor.apply {
                setCardBackgroundColor(Color.argb(100, item.r,item.g,item.b))
            }
        }
    }

    @JvmSynthetic
    internal fun AppCompatTextView.applyCompoundDrawables(
        spinnerView: PowerSpinnerView,
        item: BrandEntity
    ) {
/*        val icon = item.iconRes?.let {
            ResourcesCompat.getDrawable(spinnerView.resources, it, null)
        } ?: item.icon
        val start = compoundDrawablesRelative[0]
        val top = compoundDrawablesRelative[1]
        val end = compoundDrawablesRelative[2]
        val bottom = compoundDrawablesRelative[3]
        when (item.iconGravity) {
            Gravity.START ->
                setCompoundDrawablesRelativeWithIntrinsicBounds(icon, top, end, bottom)
            Gravity.END ->
                setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, icon, bottom)
            Gravity.TOP ->
                setCompoundDrawablesRelativeWithIntrinsicBounds(start, icon, end, bottom)
            Gravity.BOTTOM ->
                setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, icon)
        }*/
    }
}
