package com.zarholding.jpacustomer.view.adapter.holder

import android.view.View
import com.hoomanholding.applibrary.model.data.response.critic.CriticDetailModel
import com.zarholding.jpacustomer.databinding.ItemCriticDetailMeBinding

/**
 * Created by m-latifi on 8/2/2023.
 */

class CriticDetailMeHolder(
    private val binding: ItemCriticDetailMeBinding
): CriticDetailHolder(binding.root)  {

    //---------------------------------------------------------------------------------------------- bind
    override fun bind(item: CriticDetailModel) {
        val title = "${item.date} - ${item.time}"
        binding.textViewTime.text = title
        binding.textViewContent.text = item.message
        if (item.sending)
            binding.textViewSending.visibility = View.VISIBLE
        else
            binding.textViewSending.visibility = View.GONE
    }
    //---------------------------------------------------------------------------------------------- bind

}