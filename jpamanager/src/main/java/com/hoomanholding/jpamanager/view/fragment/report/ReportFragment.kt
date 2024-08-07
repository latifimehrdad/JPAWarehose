package com.hoomanholding.jpamanager.view.fragment.report

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.databinding.FragmentReportBinding
import com.hoomanholding.jpamanager.model.data.other.CardBoardItemModel
import com.hoomanholding.jpamanager.view.activity.MainActivity
import com.hoomanholding.jpamanager.view.adapter.holder.CardBoardItemHolder
import com.hoomanholding.jpamanager.view.adapter.recycler.CardBoardItemAdapter
import dagger.hilt.android.AndroidEntryPoint


/**
 * create by m-latifi on 3/6/2023
 */

@AndroidEntryPoint
class ReportFragment(override var layout: Int = R.layout.fragment_report) :
    JpaFragment<FragmentReportBinding>() {

    private val viewModel: ReportViewModel by viewModels()

    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        setListener()
        viewModel.setItems(0)
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

        viewModel.itemPositionLiveData.observe(viewLifecycleOwner) {
            selectTab(it)
        }

    }
    //---------------------------------------------------------------------------------------------- observeLiveData


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab == null)
                    return
                viewModel.setItems(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }
    //---------------------------------------------------------------------------------------------- setListener



    private fun selectTab(position: Int) {
        binding.recyclerItem.adapter = null
        val tab = binding.tabLayout.getTabAt(position)
        tab?.let {
            val width = tab.view.width
            if (width == 0)
                return
            val percent = width * 25 / 100
            val arr = IntArray(2)
            tab.view.getLocationOnScreen(arr)
            binding.viewTab.x = (arr[0] + width - percent).toFloat()
            tab.select()
        }
    }


    //---------------------------------------------------------------------------------------------- setItemAdapter
    private fun setItemAdapter(items: List<CardBoardItemModel>?) {
        if (items.isNullOrEmpty()) {
            binding.recyclerItem.adapter = null
            return
        }
        val click = object : CardBoardItemHolder.Click {
            override fun itemClick(action: Int) {
                if (action != 0)
                    gotoFragment(action)
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