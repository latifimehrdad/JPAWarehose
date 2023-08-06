package com.zarholding.jpacustomer.view.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.enums.EnumCriticStatus
import com.hoomanholding.applibrary.model.data.response.critic.CriticModel
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.ItemCriticBinding

/**
 * Created by m-latifi on 8/2/2023.
 */

class CriticHolder(
    private val binding: ItemCriticBinding,
    private val onClick: (Long, String?) -> Unit
): RecyclerView.ViewHolder(binding.root) {


    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: CriticModel) {
        binding.textViewTime.text = item.createdate
        binding.textViewSubject.text = item.tittle
        binding.root.setOnClickListener { onClick(item.id, item.tittle) }
        val context = binding.textViewStatus.context
        binding.textViewStatus.text = item.txtStatus
        when(item.status){
            EnumCriticStatus.finished -> {
                binding.textViewStatus.setTextColor(context.getColor(R.color.a_textHint))
            }
            EnumCriticStatus.replybyadmin -> {
                binding.textViewStatus.setTextColor(context.getColor(R.color.green))

            }
            EnumCriticStatus.replybyuser -> {
                binding.textViewStatus.setTextColor(context.getColor(R.color.red))
            }
            EnumCriticStatus.savebyuser -> {
                binding.textViewStatus.setTextColor(context.getColor(R.color.yellow))
            }
        }
    }
    //---------------------------------------------------------------------------------------------- bind

}