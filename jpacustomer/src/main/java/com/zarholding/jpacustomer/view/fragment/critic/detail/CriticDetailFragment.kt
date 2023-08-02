package com.zarholding.jpacustomer.view.fragment.critic.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.applibrary.ext.config
import com.hoomanholding.applibrary.ext.startLoading
import com.hoomanholding.applibrary.ext.stopLoading
import com.hoomanholding.applibrary.model.data.response.critic.CriticDetailModel
import com.hoomanholding.applibrary.tools.getShimmerBuild
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.zar.core.enums.EnumApiError
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.FragmentCriticDetailBinding
import com.zarholding.jpacustomer.view.activity.MainActivity
import com.zarholding.jpacustomer.view.adapter.recycler.CriticDetailAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by m-latifi on 8/2/2023.
 */

@AndroidEntryPoint
class CriticDetailFragment(
    override var layout: Int = R.layout.fragment_critic_detail
) : JpaFragment<FragmentCriticDetailBinding>() {

    private val viewModel: CriticDetailViewModel by viewModels()

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
        getCriticList()
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

        viewModel.criticTitleLiveData.observe(viewLifecycleOwner) {
            binding.textViewCriticTitle.text = it
        }

        viewModel.criticListLiveData.observe(viewLifecycleOwner) { items ->
            binding.shimmerViewContainer.stopLoading()
            items?.let { setAdapter(it) } ?: run { getCriticList() }
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveDate


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.imageViewSend.setOnClickListener { replyCritic() }
        binding.editTextMessage.setOnClickListener { scrollRecycler() }
        /*        binding.editTextMessage.addTextChangedListener{
                    if (it.isNullOrEmpty())
                        binding.imageViewSend.setImageResource(R.drawable.ic_microphone)
                    else
                        binding.imageViewSend.setImageResource(R.drawable.a_ic_step_sending)
                }*/
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- getCriticList
    private fun getCriticList() {
        binding.shimmerViewContainer.startLoading()
        viewModel.setCriticDetail(arguments)
    }
    //---------------------------------------------------------------------------------------------- getCriticList


    //---------------------------------------------------------------------------------------------- replyCritic
    private fun replyCritic() {
        val message = binding.editTextMessage.text.toString()
        if (message.isNotEmpty()) {
            viewModel.replyCritic(message)
            binding.editTextMessage.text.clear()
        }
    }
    //---------------------------------------------------------------------------------------------- replyCritic


    //---------------------------------------------------------------------------------------------- setAdapter
    private fun setAdapter(it: List<CriticDetailModel>?) {
        if (it.isNullOrEmpty() || context == null) {
            binding.recyclerViewMessage.adapter = null
            return
        }
        val adapter = CriticDetailAdapter(it)
        val manager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerViewMessage.adapter = adapter
        binding.recyclerViewMessage.layoutManager = manager
        scrollRecycler()
    }
    //---------------------------------------------------------------------------------------------- setAdapter


    //---------------------------------------------------------------------------------------------- scrollRecycler
    private fun scrollRecycler() {
        val adapter = binding.recyclerViewMessage.adapter
        adapter?.let {
            binding.recyclerViewMessage.scrollToPosition(it.itemCount - 1)
        }
    }
    //---------------------------------------------------------------------------------------------- scrollRecycler

}