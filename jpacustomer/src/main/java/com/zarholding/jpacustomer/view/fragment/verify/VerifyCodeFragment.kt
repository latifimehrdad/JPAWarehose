package com.zarholding.jpacustomer.view.fragment.verify

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.text.isDigitsOnly
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.hoomanholding.applibrary.model.data.enums.EnumVerifyType
import com.hoomanholding.applibrary.tools.CompanionValues
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.zar.core.enums.EnumApiError
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.FragmentVerifyCodeBinding
import com.zarholding.jpacustomer.tools.smsretriever.MySMSBroadcastReceiver
import com.zarholding.jpacustomer.tools.smsretriever.SmsClient
import com.zarholding.jpacustomer.view.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main


/**
 * create by m-latifi on 5/3/2023
 */

@AndroidEntryPoint
class VerifyCodeFragment(
    override var layout: Int = R.layout.fragment_verify_code
) : JpaFragment<FragmentVerifyCodeBinding>() {

    private var job: Job? = null
    private val viewModel: VerifyCodeViewModel by viewModels()


    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        initView()
        setListener()
        receiveSms()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- initView
    private fun initView() {
        viewModel.setVerifyTypeFromBundle(arguments)
        viewModel.deleteToken()
        observeLiveDate()
        getTokenFromArgument()
    }
    //---------------------------------------------------------------------------------------------- initView


    //---------------------------------------------------------------------------------------------- observeLiveDate
    private fun observeLiveDate() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            showMessage(it.message)
            resetEditTextsVerifyCode()
            when (it.type) {
                EnumApiError.UnAuthorization -> (activity as MainActivity?)?.gotoFirstFragment()
                else -> {}
            }
        }


        viewModel.resendLiveData.observe(viewLifecycleOwner) {
            receiveSms()
            startTimer()
        }

        viewModel.forceChanePasswordLiveData.observe(viewLifecycleOwner) {
            if (it)
                gotoChangePassword()
            else
                (activity as MainActivity?)?.gotoFirstFragment(false)
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveDate


    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
        activity?.let {
            (it as MainActivity).showMessage(message)
        }
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- getTokenFromArgument
    private fun getTokenFromArgument() {
        arguments?.let { bundle ->
            val token = bundle.getString(CompanionValues.TOKEN, null)
            token?.let {
                viewModel.token = it
                startTimer()
            } ?: run {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }
    }
    //---------------------------------------------------------------------------------------------- getTokenFromArgument


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.buttonReceiveAgain.setOnClickListener {
            requestResendVerifyCode()
        }

        binding.editTextCode1.addTextChangedListener {
            val text = it.toString()
            if (text.length == 1)
                binding.editTextCode2.requestFocus()
        }


        binding.editTextCode2.addTextChangedListener {
            val text = it.toString()
            if (text.length == 1)
                binding.editTextCode3.requestFocus()
//            else
//                binding.editTextCode1.requestFocus()
        }

        binding.editTextCode3.addTextChangedListener {
            val text = it.toString()
            if (text.length == 1)
                binding.editTextCode4.requestFocus()
//            else
//                binding.editTextCode2.requestFocus()
        }

        binding.editTextCode4.addTextChangedListener {
            val text = it.toString()
            if (text.length == 1)
                binding.editTextCode5.requestFocus()
//            else
//                binding.editTextCode3.requestFocus()
        }

        binding.editTextCode5.addTextChangedListener {
            val text = it.toString()
            if (text.length == 1)
                requestVerifyCode()
//            else
//                binding.editTextCode4.requestFocus()
        }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- startTimer
    private fun startTimer() {
        binding.mlAnimationTimer.visibility = View.VISIBLE
        binding.textViewRemaining.visibility = View.VISIBLE
        binding.buttonReceiveAgain.visibility = View.GONE
        startCoroutineRepeat()
        resetEditTextsVerifyCode()
    }
    //---------------------------------------------------------------------------------------------- startTimer


    //---------------------------------------------------------------------------------------------- startCoroutineRepeat
    private fun startCoroutineRepeat() {
        job = CoroutineScope(IO).launch {
            repeat(60) {
                delay(1000)
                withContext(Main) { binding.mlAnimationTimer.changeTime(59 - it) }
            }
            withContext(Main) { resendRequestForSms() }
        }
    }
    //---------------------------------------------------------------------------------------------- startCoroutineRepeat


    //---------------------------------------------------------------------------------------------- resendRequestForSms
    private fun resendRequestForSms() {
        binding.mlAnimationTimer.visibility = View.GONE
        binding.textViewRemaining.visibility = View.GONE
        binding.buttonReceiveAgain.visibility = View.VISIBLE
        binding.buttonReceiveAgain.stopLoading()
        resetEditTextsVerifyCode()
    }
    //---------------------------------------------------------------------------------------------- resendRequestForSms


    //---------------------------------------------------------------------------------------------- requestResendVerifyCode
    private fun requestResendVerifyCode() {
        if (viewModel.verifyType == EnumVerifyType.Login) {
            binding.buttonReceiveAgain.startLoading(getString(R.string.bePatient))
            viewModel.requestResendVerifyCode()
        } else
            activity?.onBackPressedDispatcher?.onBackPressed()
    }
    //---------------------------------------------------------------------------------------------- requestResendVerifyCode


    //---------------------------------------------------------------------------------------------- requestVerifyCode
    private fun requestVerifyCode() {
        val verifyCode =
            binding.editTextCode1.text.toString() + binding.editTextCode2.text.toString() +
                    binding.editTextCode3.text.toString() + binding.editTextCode4.text.toString() +
                    binding.editTextCode5.text.toString()
        if (verifyCode.length != 5)
            resetEditTextsVerifyCode()
        else if (!verifyCode.isDigitsOnly())
            resetEditTextsVerifyCode()
        else {
            startAnimationEditText()
            viewModel.requestVerifyCode(verifyCode)
        }
    }
    //---------------------------------------------------------------------------------------------- requestVerifyCode


    //---------------------------------------------------------------------------------------------- resetEditTextsVerifyCode
    private fun resetEditTextsVerifyCode() {
        binding.editTextCode1.text.clear()
        binding.editTextCode2.text.clear()
        binding.editTextCode3.text.clear()
        binding.editTextCode4.text.clear()
        binding.editTextCode5.text.clear()
        binding.editTextCode1.requestFocus()
        binding.buttonReceiveAgain.stopLoading()
        stopAnimationEditText()
    }
    //---------------------------------------------------------------------------------------------- resetEditTextsVerifyCode


    //---------------------------------------------------------------------------------------------- startAnimationEditText
    private fun startAnimationEditText() {
        CoroutineScope(Main).launch {
            delay(150)
            if (context != null) {
                val alpha1 =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.zoom)
                binding.editTextCode1.startAnimation(alpha1)
                delay(150)
                val alpha2 =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.zoom)
                binding.editTextCode2.startAnimation(alpha2)
                delay(150)
                val alpha3 =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.zoom)
                binding.editTextCode3.startAnimation(alpha3)
                delay(150)
                val alpha4 =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.zoom)
                binding.editTextCode4.startAnimation(alpha4)
                delay(150)
                val alpha5 =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.zoom)
                binding.editTextCode5.startAnimation(alpha5)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- startAnimationEditText


    //---------------------------------------------------------------------------------------------- stopAnimationEditText
    private fun stopAnimationEditText() {
        binding.editTextCode1.animation = null
        binding.editTextCode2.animation = null
        binding.editTextCode3.animation = null
        binding.editTextCode4.animation = null
        binding.editTextCode5.animation = null
    }
    //---------------------------------------------------------------------------------------------- stopAnimationEditText


    //---------------------------------------------------------------------------------------------- gotoChangePassword
    private fun gotoChangePassword() {
        resetEditTextsVerifyCode()
        val bundle = Bundle()
        bundle.putString(CompanionValues.TOKEN, viewModel.token)
        bundle.putString(CompanionValues.VERIFY_TYPE, viewModel.verifyType.name)
        gotoFragment(R.id.action_verifyCodeFragment_to_changePasswordFragment, bundle)
    }
    //---------------------------------------------------------------------------------------------- gotoChangePassword


    //______________________________________________________________________________________________ splitVerifyCodeFromSms
    private fun splitVerifyCodeFromSms(sms: String?) {
        val numberOnly = sms?.replace("[^0-9]".toRegex(), "")
        numberOnly?.let {
            if (numberOnly.length < 5)
                return
            binding.editTextCode1.setText(numberOnly.substring(0))
            binding.editTextCode2.setText(numberOnly.substring(1))
            binding.editTextCode3.setText(numberOnly.substring(2))
            binding.editTextCode4.setText(numberOnly.substring(3))
            binding.editTextCode5.setText(numberOnly.substring(4))
        }
    }
    //______________________________________________________________________________________________ splitVerifyCodeFromSms


    //______________________________________________________________________________________________ receiveSms
    private fun receiveSms() {
        if (context == null)
            return
        MySMSBroadcastReceiver.getSms = MySMSBroadcastReceiver.GetSms {
            splitVerifyCodeFromSms(it)
        }
        SmsClient().start(requireContext())
    }
    //______________________________________________________________________________________________ receiveSms


    //---------------------------------------------------------------------------------------------- onDestroyView
    override fun onDestroyView() {
        super.onDestroyView()
        job?.cancel()
        binding.mlAnimationTimer.changeTime(0)
        resendRequestForSms()
    }
    //---------------------------------------------------------------------------------------------- onDestroyView
}