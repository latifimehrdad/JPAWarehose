package com.hoomanholding.jpamanager.view.fragment.cardboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.hoomanholding.jpamanager.JpaFragment
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.databinding.FragmentCardboardBinding
import com.hoomanholding.jpamanager.model.data.other.CardBoardItemModel
import com.hoomanholding.jpamanager.view.activity.MainActivity
import com.hoomanholding.jpamanager.view.adapter.holder.CardBoardItemHolder
import com.hoomanholding.jpamanager.view.adapter.recycler.CardBoardItemAdapter
import dagger.hilt.android.AndroidEntryPoint


/**
 * create by m-latifi on 3/6/2023
 */

@AndroidEntryPoint
class CardBoardFragment(override var layout: Int = R.layout.fragment_cardboard) :
    JpaFragment<FragmentCardboardBinding>() {

    private val viewModel: CardboardViewModel by viewModels()

    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        viewModel.setItems()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
        activity?.let {
            (it as MainActivity).showMessage(message)
        }
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- observeLiveData
    private fun observeLiveData() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            showMessage(it.message)
        }

        viewModel.itemLiveData.observe(viewLifecycleOwner) {
            setItemAdapter(it)
        }

    }
    //---------------------------------------------------------------------------------------------- observeLiveData


    //---------------------------------------------------------------------------------------------- setItemAdapter
    private fun setItemAdapter(items: List<CardBoardItemModel>) {
        val click = object : CardBoardItemHolder.Click {
            override fun itemClick(action: Int) {
                if (action != 0)
                    findNavController().navigate(action)
            }
        }
        val adapter = CardBoardItemAdapter(items, click)
        val gridLayoutManager = GridLayoutManager(
            requireContext(),
            3,
            GridLayoutManager.VERTICAL,
            false
        )
        binding.recyclerItem.layoutManager = gridLayoutManager
        binding.recyclerItem.layoutDirection = View.LAYOUT_DIRECTION_RTL
        binding.recyclerItem.adapter = adapter
    }
    //---------------------------------------------------------------------------------------------- setItemAdapter

}