package com.hoomanholding.jpawarehose.view.fragment.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.hoomanholding.jpawarehose.R
import com.hoomanholding.jpawarehose.databinding.FragmentSplashBinding
import com.hoomanholding.jpawarehose.view.activity.MainActivity
import com.zar.core.enums.EnumApiError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

/**
 * Created by m-latifi on 11/8/2022.
 */


@AndroidEntryPoint
class SplashFragment(override var layout: Int = R.layout.fragment_splash) :
    JpaFragment<FragmentSplashBinding>() {

    private val splashViewModel: SplashViewModel by viewModels()

    private var job: Job? = null

    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonReTry.setOnClickListener { checkUserIsLogged() }
        checkUserIsLogged()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
        activity?.let {
            (it as MainActivity).showMessage(message)
        }
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- checkUserIsLogged
    private fun checkUserIsLogged() {
        if (binding.buttonReTry.isLoading)
            return
        if (splashViewModel.userIsEntered())
            gotoFragmentHome()
        else
            gotoFragmentLogin()
    }
    //---------------------------------------------------------------------------------------------- checkUserIsLogged


    //---------------------------------------------------------------------------------------------- gotoFragmentLogin
    private fun gotoFragmentLogin() {
        job = CoroutineScope(IO).launch {
            delay(3000)
            withContext(Main) {
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- gotoFragmentLogin


    //---------------------------------------------------------------------------------------------- gotoFragmentHome
    private fun gotoFragmentHome() {
        observeErrorLiveDate()
        observeSuccessLiveDataLiveData()
        startLoading()
        splashViewModel.requestGetData()
    }
    //---------------------------------------------------------------------------------------------- gotoFragmentHome


    //---------------------------------------------------------------------------------------------- observeLoginLiveDate
    private fun observeErrorLiveDate() {
        splashViewModel.errorLiveDate.observe(viewLifecycleOwner) {
            stopLoading()
            showMessage(it.message)
            when (it.type) {
                EnumApiError.UnAuthorization -> (activity as MainActivity?)?.gotoFirstFragment()
                else -> {}
            }
        }
    }
    //---------------------------------------------------------------------------------------------- observeLoginLiveDate


    //---------------------------------------------------------------------------------------------- observeSuccessLiveDataLiveData
    private fun observeSuccessLiveDataLiveData() {
        splashViewModel.successLiveData.observe(viewLifecycleOwner) {
            activity?.let {main->
                (main as MainActivity).showImageViewShelf()
            }
            if (it)
                findNavController().navigate(R.id.action_splashFragment_to_HomeFragment)
        }
    }
    //---------------------------------------------------------------------------------------------- observeSuccessLiveDataLiveData



    //---------------------------------------------------------------------------------------------- startLoading
    private fun startLoading() {
        binding.buttonReTry.visibility = View.GONE
        binding.buttonReTry.startLoading("")
    }
    //---------------------------------------------------------------------------------------------- startLoading


    //---------------------------------------------------------------------------------------------- stopLoading
    private fun stopLoading() {
        binding.buttonReTry.stopLoading()
        binding.buttonReTry.visibility = View.VISIBLE
    }
    //---------------------------------------------------------------------------------------------- stopLoading

}