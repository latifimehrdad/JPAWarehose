package com.zarholding.jpacustomer.view.fragment.profile

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.hoomanholding.applibrary.ext.downloadProfileImage
import com.hoomanholding.applibrary.model.data.enums.EnumEntityType
import com.hoomanholding.applibrary.tools.CompanionValues
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.zar.core.enums.EnumApiError
import com.zar.core.tools.manager.ThemeManager
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.FragmentProfileBinding
import com.zarholding.jpacustomer.view.activity.MainActivity
import com.zarholding.jpacustomer.view.dialog.ConfirmDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by zar on 5/23/2023.
 */

@AndroidEntryPoint
class ProfileFragment(
    override var layout: Int = R.layout.fragment_profile
) : JpaFragment<FragmentProfileBinding>() {

    private val viewModel: ProfileViewModel by viewModels()

    @Inject lateinit var themeManagers: ThemeManager

    @Inject lateinit var sharedPreferences: SharedPreferences

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
        binding.viewModel = viewModel
        CoroutineScope(Main).launch {
            delay(300)
            when (themeManagers.applicationTheme()) {
                Configuration.UI_MODE_NIGHT_YES -> binding.switchActive.isChecked =
                    true
                Configuration.UI_MODE_NIGHT_NO -> binding.switchActive.isChecked =
                    false
            }
        }
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

    }
    //---------------------------------------------------------------------------------------------- observeLiveDate


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.switchActive.setOnClickListener { changeAppTheme() }
        binding.cardViewSingOut.setOnClickListener { signOut() }
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
        if (binding.switchActive.isChecked)
            themeManagers.changeApplicationTheme(Configuration.UI_MODE_NIGHT_YES)
        else
            themeManagers.changeApplicationTheme(Configuration.UI_MODE_NIGHT_NO)
    }
    //---------------------------------------------------------------------------------------------- changeAppTheme


}