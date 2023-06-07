package com.zarholding.jpacustomer.view.fragment.video

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.applibrary.ext.config
import com.hoomanholding.applibrary.ext.startLoading
import com.hoomanholding.applibrary.ext.stopLoading
import com.hoomanholding.applibrary.model.data.response.video.VideoCategoryModel
import com.hoomanholding.applibrary.model.data.response.video.VideoModel
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
        hidePlayVideo()
        binding.shimmerViewContainer.config(getShimmerBuild())
        observeLiveDate()
        setListener()
        binding.shimmerViewContainer.startLoading()
        viewModel.getCategory()
    }
    //---------------------------------------------------------------------------------------------- initView


    //---------------------------------------------------------------------------------------------- observeLiveDate
    private fun observeLiveDate() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            showMessage(it.message)
            when (it.type) {
                EnumApiError.UnAuthorization -> (activity as MainActivity?)?.gotoFirstFragment()
                else -> {}
            }
        }

        viewModel.categoryLiveData.observe(viewLifecycleOwner) {
            setAdapterCategory(it)
        }

        viewModel.videoLiveData.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            setVideoAdapter(it)
        }

        viewModel.selectVideoLiveData.observe(viewLifecycleOwner) {
            selectVideo(it)
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveDate



    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.cardViewVideo.setOnClickListener {
//            "https://cdn14.git.ir/03/Udemy%20Android%20Jetpack%20Compose%20The%20Comprehensive%20Bootcamp%202022-git.ir/002-Course%20Learning%20Path%20Please%20Watch%20this-git.ir.mp4"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.parse(viewModel.getVideoLink()), "video/mp4")
            startActivity(intent)
        }
    }
    //---------------------------------------------------------------------------------------------- setListener



    //---------------------------------------------------------------------------------------------- setAdapterCategory
    private fun setAdapterCategory(items: List<VideoCategoryModel>){
        val click = object: VideoCategoryHolder.Click{
            override fun click(item: VideoCategoryModel, position: Int) {
                binding.shimmerViewContainer.startLoading()
                VideoCategoryAdapter.selectedPosition = position
                adapter?.notifyItemRangeChanged(0, adapter?.itemCount?:0)
                binding.recyclerViewVideo.adapter = null
                hidePlayVideo()
                viewModel.getVideo(item.id)
            }
        }
        adapter = VideoCategoryAdapter(items, click)
        val manager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.HORIZONTAL, true
        )
        VideoCategoryAdapter.selectedPosition = 0
        binding.recyclerViewCategory.adapter = adapter
        binding.recyclerViewCategory.layoutManager = manager
    }
    //---------------------------------------------------------------------------------------------- setAdapterCategory



    //---------------------------------------------------------------------------------------------- setVideoAdapter
    private fun setVideoAdapter(items: List<VideoModel>) {
        val click = object : VideoHolder.Click{
            override fun click(position: Int) {
                viewModel.selectVideo(position)
            }
        }
        val adapterVideo = VideoAdapter(items, click)
        val manager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerViewVideo.adapter = adapterVideo
        binding.recyclerViewVideo.layoutManager = manager
        viewModel.selectVideo(0)
    }
    //---------------------------------------------------------------------------------------------- setVideoAdapter


    //---------------------------------------------------------------------------------------------- selectVideo
    private fun selectVideo(item: VideoModel) {
        binding.cardViewVideo.visibility = View.VISIBLE
        binding.textViewTitleVideo.text = item.videoTitle
        binding.textViewInitDescription.text = item.videoDescription
    }
    //---------------------------------------------------------------------------------------------- selectVideo



    //---------------------------------------------------------------------------------------------- hidePlayVideo
    private fun hidePlayVideo() {
        binding.textViewTitleVideo.text = ""
        binding.textViewInitDescription.text = ""
        binding.cardViewVideo.visibility = View.INVISIBLE
    }
    //---------------------------------------------------------------------------------------------- hidePlayVideo

}