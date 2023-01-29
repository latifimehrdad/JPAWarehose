package com.hoomanholding.jpawarehose.view.adapter

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.jpawarehose.databinding.ItemSpinnerSupplierBinding
import com.hoomanholding.jpawarehose.model.database.entity.ReceiptEntity
import com.skydoves.powerspinner.*

/**
 * Create by Mehrdad on 1/26/2023
 */

/** definition of the non-value of Int type. */


class ReceiptSpinnerAdapter(
    powerSpinnerView: PowerSpinnerView
) : RecyclerView.Adapter<ReceiptSpinnerAdapter.ReceiptSpinnerViewHolder>(),
    PowerSpinnerInterface<ReceiptEntity> {

    override var index: Int = powerSpinnerView.selectedIndex
    override val spinnerView: PowerSpinnerView = powerSpinnerView
    override var onSpinnerItemSelectedListener: OnSpinnerItemSelectedListener<ReceiptEntity>? = null

    private val compoundPadding: Int = 12
    private val spinnerItems: MutableList<ReceiptEntity> = arrayListOf()

    init {
        this.spinnerView.compoundDrawablePadding = compoundPadding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiptSpinnerViewHolder {
        val binding =
            ItemSpinnerSupplierBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ReceiptSpinnerViewHolder(binding).apply {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition.takeIf { it != RecyclerView.NO_POSITION }
                    ?: return@setOnClickListener
                notifyItemSelected(position)
            }
        }
    }

    override fun onBindViewHolder(holder: ReceiptSpinnerViewHolder, position: Int) {
        holder.bind(spinnerItems[position], spinnerView)
    }

    override fun setItems(itemList: List<ReceiptEntity>) {
        this.spinnerItems.clear()
        this.spinnerItems.addAll(itemList)
        this.index = NO_SELECTED_INDEX
        notifyDataSetChanged()
    }

    override fun notifyItemSelected(index: Int) {
        if (index == NO_SELECTED_INDEX) return
        val item = spinnerItems[index]
        spinnerView.compoundDrawablePadding = spinnerView.compoundDrawablePadding
        val oldIndex = this.index
        this.index = index
        this.spinnerView.notifyItemSelected(index, item.nameTaminKonandeh ?: "")
        this.onSpinnerItemSelectedListener?.onItemSelected(
            oldIndex = oldIndex,
            oldItem = oldIndex.takeIf { it != NO_SELECTED_INDEX }?.let { spinnerItems[oldIndex] },
            newIndex = index,
            newItem = item
        )
    }

    override fun getItemCount(): Int = this.spinnerItems.size

    inner class ReceiptSpinnerViewHolder(private val binding: ItemSpinnerSupplierBinding) :
        RecyclerView.ViewHolder(binding.root) {

        internal fun bind(item: ReceiptEntity, spinnerView: PowerSpinnerView) {
            val title = "${item.nameTaminKonandeh} - ${item.shomarehForm}"
            binding.itemDefaultText.apply {
                text = title
                setTextSize(TypedValue.COMPLEX_UNIT_PX, spinnerView.textSize)
                setTextColor(spinnerView.currentTextColor)
                compoundDrawablePadding = spinnerView.compoundDrawablePadding
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
        }
    }

}
