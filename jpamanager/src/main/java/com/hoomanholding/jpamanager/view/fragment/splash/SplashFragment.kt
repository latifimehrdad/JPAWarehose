package com.hoomanholding.jpamanager.view.fragment.splash

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hoomanholding.jpamanager.JpaFragment
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.databinding.FragmentSplashBinding
import com.hoomanholding.jpamanager.view.activity.MainActivity
import com.zar.core.enums.EnumApiError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * Created by m-latifi on 11/8/2022.
 */


@AndroidEntryPoint
class SplashFragment(override var layout: Int = R.layout.fragment_splash) :
    JpaFragment<FragmentSplashBinding>() {

    private val splashViewModel: SplashViewModel by viewModels()

    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageViewManegerApp.visibility = View.INVISIBLE
        binding.materialButtonLogin.visibility = View.INVISIBLE
        setListener()
        startAnimation()
        checkUserIsLogged()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
        binding.materialButtonLogin.stopLoading()
        activity?.let {
            (it as MainActivity).showMessage(message)
        }
    }
    //---------------------------------------------------------------------------------------------- showMessage



    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.materialButtonLogin.setOnClickListener {
            checkUserIsLogged()
        }

        binding.imageViewManegerApp.setOnClickListener {
            findNavController().navigate(R.id.action_splashFragment_to_HomeFragment)
        }
    }
    //---------------------------------------------------------------------------------------------- setListener



    //---------------------------------------------------------------------------------------------- startAnimation
    private fun startAnimation() {
        CoroutineScope(Main).launch {
            delay(1000)
            if (context != null) {
                binding.imageViewManegerApp.animation = null
                binding.materialButtonLogin.animation = null
                val slideInLeft =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_left)
                binding.imageViewManegerApp.startAnimation(slideInLeft)
                val slideInBottom =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_bottom)
                binding.materialButtonLogin.startAnimation(slideInBottom)
                binding.imageViewManegerApp.visibility = View.VISIBLE
                binding.materialButtonLogin.visibility = View.VISIBLE
            }
        }

        CoroutineScope(Main).launch {
            delay(2000)
            if (context != null) {
                binding.imageViewManegerApp.animation = null
                binding.imageViewManegerApp.visibility = View.INVISIBLE
                val alpha =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.alpha)
                binding.imageViewManegerApp.startAnimation(alpha)
                binding.imageViewManegerApp.visibility = View.VISIBLE
            }
        }
    }
    //---------------------------------------------------------------------------------------------- startAnimation


    //---------------------------------------------------------------------------------------------- checkUserIsLogged
    private fun checkUserIsLogged() {
        if (splashViewModel.userIsEntered())
            gotoFragmentHome()
        else
            gotoFragmentLogin()
    }
    //---------------------------------------------------------------------------------------------- checkUserIsLogged


    //---------------------------------------------------------------------------------------------- gotoFragmentLogin
    private fun gotoFragmentLogin() {
        findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
    }
    //---------------------------------------------------------------------------------------------- gotoFragmentLogin


    //---------------------------------------------------------------------------------------------- gotoFragmentHome
    private fun gotoFragmentHome() {
        if (binding.materialButtonLogin.isLoading)
            return
        observeLiveDate()
        binding.materialButtonLogin.startLoading(getString(R.string.bePatient))
        splashViewModel.requestGetData()
    }
    //---------------------------------------------------------------------------------------------- gotoFragmentHome


    //---------------------------------------------------------------------------------------------- observeLiveDate
    private fun observeLiveDate() {
        splashViewModel.errorLiveDate.observe(viewLifecycleOwner) {
            showMessage(it.message)
            when (it.type) {
                EnumApiError.UnAuthorization -> (activity as MainActivity?)?.gotoFirstFragment()
                else -> {}
            }
        }

        splashViewModel.successLiveData.observe(viewLifecycleOwner) {
            binding.materialButtonLogin.stopLoading()
            if (it)
                findNavController().navigate(R.id.action_splashFragment_to_HomeFragment)
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveDate


}