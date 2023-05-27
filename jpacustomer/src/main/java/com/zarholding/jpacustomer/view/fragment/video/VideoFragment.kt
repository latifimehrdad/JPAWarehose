package com.zarholding.jpacustomer.view.fragment.video

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.applibrary.ext.config
import com.hoomanholding.applibrary.tools.getShimmerBuild
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.zar.core.enums.EnumApiError
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.FragmentVideoBinding
import com.zarholding.jpacustomer.view.activity.MainActivity
import com.zarholding.jpacustomer.view.adapter.holder.VideoCategoryHolder
import com.zarholding.jpacustomer.view.adapter.holder.VideoHolder
import com.zarholding.jpacustomer.view.adapter.recycler.VideoAdapter
import com.zarholding.jpacustomer.view.adapter.recycler.VideoCategoryAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by m-latifi on 5/24/2023.
 */

@AndroidEntryPoint
class VideoFragment(
    override var layout: Int = R.layout.fragment_video
): JpaFragment<FragmentVideoBinding>() {


    private val viewModel: VideoViewModel by viewModels()
    private var adapter: VideoCategoryAdapter? = null


    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated



    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
        activity?.let { (it as MainActivity).showMessage(message) }
    }
    //---------------------------------------------------------------------------------------------- showMessage



    //---------------------------------------------------------------------------------------------- initView
    private fun initView() {
        binding.shimmerViewContainer.config(getShimmerBuild())
        observeLiveDate()
        setListener()
        viewModel.getCategory()
    }
    //---------------------------------------------------------------------------------------------- initView


    //---------------------------------------------------------------------------------------------- observeLiveDate
    private fun observeLiveDate() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            showMessage(it.message)
            when (it.type) {
                EnumApiError.UnAuthorization -> (activity as MainActivity?)?.gotoFirstFragment()
                else -> {}
            }
        }

        viewModel.categoryLiveData.observe(viewLifecycleOwner) {
            setAdapter(it)
        }

    }
    //---------------------------------------------------------------------------------------------- observeLiveDate



    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {

    }
    //---------------------------------------------------------------------------------------------- setListener



    //---------------------------------------------------------------------------------------------- setAdapter
    private fun setAdapter(items: List<String>){
        val click = object: VideoCategoryHolder.Click{
            override fun click(position: Int) {
                VideoCategoryAdapter.selectedPosition = position
                adapter?.notifyItemRangeChanged(0, adapter?.itemCount?:0)
            }
        }
        adapter = VideoCategoryAdapter(items, click)
        val manager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.HORIZONTAL, true
        )
        VideoCategoryAdapter.selectedPosition = 0
        binding.recyclerViewCategory.adapter = adapter
        binding.recyclerViewCategory.layoutManager = manager

        val click2 = object : VideoHolder.Click{
            override fun click(position: Int) {
            }
        }
        val adapterVideo = VideoAdapter(items, click2)
        val manager2 = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerViewVideo.adapter = adapterVideo
        binding.recyclerViewVideo.layoutManager = manager2
    }
    //---------------------------------------------------------------------------------------------- setAdapter

}