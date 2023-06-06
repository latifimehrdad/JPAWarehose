package com.zarholding.jpacustomer.view.fragment.profile

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.hoomanholding.applibrary.ext.downloadProfileImage
import com.hoomanholding.applibrary.ext.setTitleAndValue
import com.hoomanholding.applibrary.model.data.database.entity.UserInfoEntity
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.zar.core.enums.EnumApiError
import com.zar.core.tools.manager.ThemeManager
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.FragmentProfileBinding
import com.zarholding.jpacustomer.view.activity.MainActivity
import com.zarholding.jpacustomer.view.dialog.ConfirmDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by m-latifi on 5/23/2023.
 */

@AndroidEntryPoint
class ProfileFragment(
    override var layout: Int = R.layout.fragment_profile
) : JpaFragment<FragmentProfileBinding>() {

    private val viewModel: ProfileViewModel by viewModels()

    @Inject
    lateinit var themeManagers: ThemeManager

    @Inject
    lateinit var sharedPreferences: SharedPreferences

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
        checkTheme()
        observeLiveDate()
        setListener()
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

    }
    //---------------------------------------------------------------------------------------------- observeLiveDate


    //---------------------------------------------------------------------------------------------- checkTheme
    private fun checkTheme() {
        CoroutineScope(Main).launch {
            delay(300)
            when (themeManagers.applicationTheme()) {
                Configuration.UI_MODE_NIGHT_YES ->
                    binding.dayNightSwitch.setDayChecked(false,animated = true)

                Configuration.UI_MODE_NIGHT_NO ->
                    binding.dayNightSwitch.setDayChecked(true,animated = true)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- checkTheme


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.dayNightSwitch.setOnSwitchListener { dayNightSwitch, b ->
            changeAppTheme()
        }
        binding.switchActive.setOnClickListener { changeAppTheme() }
        binding.cardViewSingOut.setOnClickListener { signOut() }
        binding.textViewMyState.setOnClickListener {
            gotoFragment(R.id.action_profileFragment_to_myStateFragment)
        }
        binding.textViewVideo.setOnClickListener {
            gotoFragment(R.id.action_profileFragment_to_videoFragment)
        }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- signOut
    private fun signOut() {
        if (context == null)
            return
        val click = object : ConfirmDialog.Click {
            override fun clickYes() {
                (activity as MainActivity?)?.gotoFirstFragment()
            }
        }
        ConfirmDialog(
            requireContext(),
            getString(R.string.doYouWantToExitAccount),
            click
        ).show()
    }
    //---------------------------------------------------------------------------------------------- signOut


    //---------------------------------------------------------------------------------------------- changeAppTheme
    private fun changeAppTheme() {
        CoroutineScope(IO).launch{
            delay(300)
            withContext(Main) {
                if (!binding.dayNightSwitch.isDayChecked())
                    themeManagers.changeApplicationTheme(Configuration.UI_MODE_NIGHT_YES)
                else
                    themeManagers.changeApplicationTheme(Configuration.UI_MODE_NIGHT_NO)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- changeAppTheme


    //---------------------------------------------------------------------------------------------- setUserInfo
    private fun setUserInfo(userInfo: UserInfoEntity) {
        binding.textViewName.text = userInfo.fullName
        binding.textViewVisitorName.text = userInfo.visitorName
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
        binding.textViewVisitorMobile.setTitleAndValue(
            title = getString(R.string.mobileNumber),
            splitter = getString(R.string.colon),
            value = userInfo.visitorMobile
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
        binding.imageViewVisitorImage.downloadProfileImage(
            url = userInfo.visitorImageName,
            systemType = userInfo.systemType,
            token = viewModel.getBearerToken()
        )
    }
    //---------------------------------------------------------------------------------------------- setUserInfo


}