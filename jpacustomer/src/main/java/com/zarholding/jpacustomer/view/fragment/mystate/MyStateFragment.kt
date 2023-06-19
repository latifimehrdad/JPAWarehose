package com.zarholding.jpacustomer.view.fragment.mystate

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.applibrary.ext.config
import com.hoomanholding.applibrary.ext.downloadProfileImage
import com.hoomanholding.applibrary.ext.setAmount
import com.hoomanholding.applibrary.ext.setTitleAndValue
import com.hoomanholding.applibrary.ext.startLoading
import com.hoomanholding.applibrary.ext.stopLoading
import com.hoomanholding.applibrary.model.data.database.entity.UserInfoEntity
import com.hoomanholding.applibrary.model.data.enums.EnumSystemType
import com.hoomanholding.applibrary.model.data.response.customer.CustomerStateModel
import com.hoomanholding.applibrary.model.data.response.customer.NotDueCheckModel
import com.hoomanholding.applibrary.tools.getShimmerBuild
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.zar.core.enums.EnumApiError
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.FragmentMyStateBinding
import com.zarholding.jpacustomer.view.activity.MainActivity
import com.zarholding.jpacustomer.view.adapter.holder.RemindCheckHolder
import com.zarholding.jpacustomer.view.adapter.recycler.RemindCheckAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by m-latifi on 5/24/2023.
 */

@AndroidEntryPoint
class MyStateFragment(
    override var layout: Int = R.layout.fragment_my_state
) : JpaFragment<FragmentMyStateBinding>() {


    private val viewModel: MyStateViewModel by viewModels()
    var adapter: RemindCheckAdapter? = null


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
        viewModel.getUserInfo()
        getCustomerState()
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


        viewModel.customerStateModel.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            setCustomerStateToXml(it)
        }

        viewModel.userInfoLiveData.observe(viewLifecycleOwner) {
            setUserInfo(it)
        }

    }
    //---------------------------------------------------------------------------------------------- observeLiveDate


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {

    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- getCustomerState
    private fun getCustomerState() {
        binding.shimmerViewContainer.startLoading()
        viewModel.getCustomerState()
    }
    //---------------------------------------------------------------------------------------------- getCustomerState


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
        binding.imageViewProfile.downloadProfileImage(
            url = userInfo.profileImageName,
            systemType = EnumSystemType.Customers.name,
            token = viewModel.getBearerToken()
        )
    }
    //---------------------------------------------------------------------------------------------- setUserInfo


    //---------------------------------------------------------------------------------------------- setCustomerStateToXml
    private fun setCustomerStateToXml(customerStateModel: CustomerStateModel) {
        binding.textViewPurchaseAmount.setAmount(
            value = customerStateModel.billingAmount,
            getString(R.string.rial)
        )
        binding.textViewPurchaseAmountText.setTitleAndValue(
            title = "تعداد",
            value = customerStateModel.billingCount,
            splitter = getString(R.string.space)
        )
        binding.textViewDiscountAmount.setAmount(
            value = customerStateModel.returnAmount,
            getString(R.string.rial)
        )
        binding.textViewDiscountAmountText.setTitleAndValue(
            title = "تعداد",
            value = customerStateModel.returnCount,
            splitter = getString(R.string.space)
        )
        binding.textViewAmountCredit.setAmount(
            value = customerStateModel.dependentCheckAmount,
            getString(R.string.rial)
        )
        binding.textviewCreditAmountText.setTitleAndValue(
            title = "تعداد",
            value = customerStateModel.dependentCheckCount,
            splitter = getString(R.string.space)
        )
        binding.textViewAmountOwed.setAmount(
            value = customerStateModel.remain,
            getString(R.string.rial)
        )
        if (context != null) {
            if (customerStateModel.remain > 0) {
                binding.textViewAmountOwedTitle.text = getText(R.string.amountOwed)
                binding.textViewAmountOwedTitle.setTextColor(requireContext().getColor(R.color.red))
                binding.textViewAmountOwed.setTextColor(requireContext().getColor(R.color.red))
            } else {
                binding.textViewAmountOwedTitle.text = getText(R.string.creditAmount)
                binding.textViewAmountOwedTitle.setTextColor(requireContext().getColor(R.color.green))
                binding.textViewAmountOwed.setTextColor(requireContext().getColor(R.color.green))
            }

            if (customerStateModel.notDueCheckList.isNullOrEmpty()) {
                binding.constraintLayoutRemindCheck.visibility = View.GONE
                return
            }
            val click = object : RemindCheckHolder.Click {
                override fun click(position: Int, item: NotDueCheckModel) {
                    selectCheck(position, item)
                }
            }
            RemindCheckAdapter.selectedPosition = 0
            adapter = RemindCheckAdapter(customerStateModel.notDueCheckList!!, click)
            val manager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
            )
            manager.reverseLayout = true
            binding.recyclerViewRemind.adapter = adapter
            binding.recyclerViewRemind.layoutManager = manager
            selectCheck(0, customerStateModel.notDueCheckList!![0])
        }
    }
    //---------------------------------------------------------------------------------------------- setCustomerStateToXml


    //---------------------------------------------------------------------------------------------- selectCheck
    private fun selectCheck(position: Int, item: NotDueCheckModel) {
        RemindCheckAdapter.selectedPosition = position
        val title = "${item.diffday} روز مانده تا تاریخ چک بعدی "
        binding.textViewRemindCheck.text = title
        adapter?.notifyItemRangeChanged(0, adapter?.itemCount?:0)
    }
    //---------------------------------------------------------------------------------------------- selectCheck
}