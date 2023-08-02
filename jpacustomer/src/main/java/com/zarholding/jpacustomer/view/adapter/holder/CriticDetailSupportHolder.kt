package com.zarholding.jpacustomer.view.adapter.holder

import com.hoomanholding.applibrary.model.data.response.critic.CriticDetailModel
import com.zarholding.jpacustomer.databinding.ItemCriticDetailBinding

/**
 * Created by m-latifi on 8/2/2023.
 */

class CriticDetailSupportHolder(
    private val binding: ItemCriticDetailBinding
): CriticDetailHolder(binding.root) {

    //---------------------------------------------------------------------------------------------- bind
    override fun bind(item: CriticDetailModel) {
        val title = "${item.date} - ${item.time}"
        binding.textViewTime.text = title
        binding.textViewContent.text = item.message
    }
    //---------------------------------------------------------------------------------------------- bind

}