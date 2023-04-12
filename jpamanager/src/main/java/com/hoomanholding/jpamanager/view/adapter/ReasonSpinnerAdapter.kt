package com.hoomanholding.jpamanager.view.adapter

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.reason.DisApprovalReasonModel
import com.hoomanholding.jpamanager.databinding.ItemSpinnerReasonBinding
import com.skydoves.powerspinner.*

/**
 * Create by Mehrdad on 1/26/2023
 */

/** definition of the non-value of Int type. */
internal const val NO_INT_VALUE: Int = Int.MIN_VALUE

/** definition of the non-selected index value. */
internal const val NO_SELECTED_INDEX: Int = -1


class ReasonSpinnerAdapter(
    powerSpinnerView: PowerSpinnerView
) : RecyclerView.Adapter<ReasonSpinnerAdapter.ReasonSpinnerViewHolder>(),
    PowerSpinnerInterface<DisApprovalReasonModel> {

    override var index: Int = powerSpinnerView.selectedIndex
    override val spinnerView: PowerSpinnerView = powerSpinnerView
    override var onSpinnerItemSelectedListener: OnSpinnerItemSelectedListener<DisApprovalReasonModel>? = null

    private val compoundPadding: Int = 12
    private val spinnerItems: MutableList<DisApprovalReasonModel> = arrayListOf()

    init {
        this.spinnerView.compoundDrawablePadding = compoundPadding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReasonSpinnerViewHolder {
        val binding =
            ItemSpinnerReasonBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ReasonSpinnerViewHolder(binding).apply {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition.takeIf { it != RecyclerView.NO_POSITION }
                    ?: return@setOnClickListener
                notifyItemSelected(position)
            }
        }
    }

    override fun onBindViewHolder(holder: ReasonSpinnerViewHolder, position: Int) {
        holder.bind(spinnerItems[position], spinnerView)
    }

    override fun setItems(itemList: List<DisApprovalReasonModel>) {
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
        this.spinnerView.notifyItemSelected(index, item.value ?: "")
        this.onSpinnerItemSelectedListener?.onItemSelected(
            oldIndex = oldIndex,
            oldItem = oldIndex.takeIf { it != NO_SELECTED_INDEX }?.let { spinnerItems[oldIndex] },
            newIndex = index,
            newItem = item
        )
    }

    override fun getItemCount(): Int = this.spinnerItems.size

    inner class ReasonSpinnerViewHolder(private val binding: ItemSpinnerReasonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        internal fun bind(item: DisApprovalReasonModel, spinnerView: PowerSpinnerView) {
            binding.itemDefaultText.apply {
                text = item.value
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
