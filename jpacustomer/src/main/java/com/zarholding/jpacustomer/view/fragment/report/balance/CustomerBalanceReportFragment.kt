package com.zarholding.jpacustomer.view.fragment.report.balance

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.applibrary.ext.config
import com.hoomanholding.applibrary.ext.downloadProfileImage
import com.hoomanholding.applibrary.ext.setTitleAndValue
import com.hoomanholding.applibrary.ext.startLoading
import com.hoomanholding.applibrary.ext.stopLoading
import com.hoomanholding.applibrary.model.data.database.entity.UserInfoEntity
import com.hoomanholding.applibrary.model.data.response.report.CustomerBalanceReportDetailModel
import com.hoomanholding.applibrary.tools.getShimmerBuild
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.zar.core.enums.EnumApiError
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.FragmentReportBalanceBinding
import com.zarholding.jpacustomer.view.activity.MainActivity
import com.zarholding.jpacustomer.view.adapter.recycler.CustomerBalanceAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

/**
 * Created by m-latifi on 6/7/2023.
 */

@AndroidEntryPoint
class CustomerBalanceReportFragment(
    override var layout: Int = R.layout.fragment_report_balance
): JpaFragment<FragmentReportBalanceBinding>() {


    private val viewModel: CustomerBalanceReportViewModel by viewModels()


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
        binding.shimmerViewContainer.startLoading()
        viewModel.getUserInfo()
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

        viewModel.userInfoLiveData.observe(viewLifecycleOwner) {
            setUserInfo(it)
        }

        viewModel.reportDetailLiveData.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            setAdapter(it)
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveDate



    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {

    }
    //---------------------------------------------------------------------------------------------- setListener



    //---------------------------------------------------------------------------------------------- setUserInfo
    private fun setUserInfo(userInfo: UserInfoEntity) {
        binding.textViewName.text = userInfo.fullName
        binding.textViewUserCode.setTitleAndValue(
            title = getString(R.string.customerCode),
            splitter = getString(R.string.colon),
            value = userInfo.personnelNumber
        )
        binding.textViewUserMobile.setTitleAndValue(
            title = getString(R.string.mobileNumber),
            splitter = getString(R.string.colon),
            value = userInfo.mobileNumber
        )
        binding.textViewShopName.setTitleAndValue(
            title = getString(R.string.shopName),
            splitter = getString(R.string.colon),
            value = userInfo.storeName
        )
        binding.textViewShopAddress.setTitleAndValue(
            title = getString(R.string.shopAddress),
            splitter = getString(R.string.colon),
            value = userInfo.address
        )
        binding.imageViewProfile.downloadProfileImage(
            url = userInfo.profileImageName,
            systemType = userInfo.systemType,
            token = viewModel.getBearerToken()
        )
    }
    //---------------------------------------------------------------------------------------------- setUserInfo


    //---------------------------------------------------------------------------------------------- setAdapter
    private fun setAdapter(items: List<CustomerBalanceReportDetailModel>) {
        if (context == null)
            return
        val adapter = CustomerBalanceAdapter(items)
        val manager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.recyclerViewReport.layoutManager = manager
        binding.recyclerViewReport.adapter = adapter
        if (items.isNotEmpty()) {
            val item  = items.last()
            if (item.balance > 0) {
                binding.textViewTotal.setTextColor(requireContext().getColor(R.color.a_debit))
                binding.textViewTotal.setTitleAndValue(
                    title = getString(R.string.totalDebit),
                    splitter = getString(R.string.colon),
                    last = getString(R.string.rial),
                    value = item.balance
                )
            } else {
                binding.textViewTotal.setTextColor(requireContext().getColor(R.color.a_debit))
                binding.textViewTotal.setTitleAndValue(
                    title = getString(R.string.totalCredit),
                    splitter = getString(R.string.colon),
                    last = getString(R.string.rial),
                    value = abs(item.balance)
                )
            }
        }
    }
    //---------------------------------------------------------------------------------------------- setAdapter
}