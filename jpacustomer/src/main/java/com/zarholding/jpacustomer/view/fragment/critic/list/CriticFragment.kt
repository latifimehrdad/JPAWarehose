package com.zarholding.jpacustomer.view.fragment.critic.list

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.hoomanholding.applibrary.ext.config
import com.hoomanholding.applibrary.ext.startLoading
import com.hoomanholding.applibrary.ext.stopLoading
import com.hoomanholding.applibrary.model.data.response.critic.CriticModel
import com.hoomanholding.applibrary.tools.CompanionValues
import com.hoomanholding.applibrary.tools.getShimmerBuild
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.zar.core.enums.EnumApiError
import com.zar.core.tools.manager.DialogManager
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.FragmentCriticBinding
import com.zarholding.jpacustomer.view.activity.MainActivity
import com.zarholding.jpacustomer.view.adapter.recycler.CriticAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by m-latifi on 8/2/2023.
 */

@AndroidEntryPoint
class CriticFragment(
    override var layout: Int = R.layout.fragment_critic
) : JpaFragment<FragmentCriticBinding>() {

    private val viewModel: CriticViewModel by viewModels()

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

        viewModel.criticListLiveData.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            setAdapter(it)
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveDate


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.buttonNewMessage.setOnClickListener { showDialogNewMessage() }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- getCriticList
    private fun getCriticList() {
        binding.shimmerViewContainer.startLoading()
        viewModel.getCriticList()
    }
    //---------------------------------------------------------------------------------------------- getCriticList


    //---------------------------------------------------------------------------------------------- setAdapter
    private fun setAdapter(it: List<CriticModel>?) {
        if (it.isNullOrEmpty() || context == null) {
            binding.recyclerViewMessage.adapter = null
            return
        }
        val adapter = CriticAdapter(it) { id, title ->
            val bundle = Bundle()
            bundle.putLong(CompanionValues.ID, id)
            bundle.putString(CompanionValues.SUBJECT, title)
            gotoFragment(R.id.action_goto_criticDetailFragment, bundle)
        }
        val manager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerViewMessage.adapter = adapter
        binding.recyclerViewMessage.layoutManager = manager
    }
    //---------------------------------------------------------------------------------------------- setAdapter


    //---------------------------------------------------------------------------------------------- showDialogNewMessage
    private fun showDialogNewMessage() {
        if (context != null) {
            val dialog = DialogManager().createDialogHeightWrapContent(
                requireContext(),
                R.layout.dialog_new_message,
                Gravity.CENTER,
                0
            )

            val textInputEditTextSubject =
                dialog.findViewById<TextInputEditText>(R.id.textInputEditTextSubject)

            val textInputEditTextDescription =
                dialog.findViewById<TextInputEditText>(R.id.textInputEditTextDescription)

            val buttonNo = dialog.findViewById<MaterialButton>(R.id.buttonNo)
            buttonNo.setOnClickListener { dialog.dismiss() }

            val buttonYes = dialog.findViewById<MaterialButton>(R.id.buttonYes)
            buttonYes.setOnClickListener {
                if (textInputEditTextSubject.text.isNullOrEmpty()) {
                    textInputEditTextSubject.error = getString(R.string.subjectIsEmpty)
                    return@setOnClickListener
                }

                if (textInputEditTextDescription.text.isNullOrEmpty()) {
                    textInputEditTextDescription.error = getString(R.string.descriptionIsEmpty)
                    return@setOnClickListener
                }

                binding.recyclerViewMessage.adapter = null
                binding.shimmerViewContainer.startLoading()
                viewModel.addNewCritic(
                    textInputEditTextSubject.text.toString(),
                    textInputEditTextDescription.text.toString()
                )
                dialog.dismiss()
            }
            dialog.show()
        }
    }
    //---------------------------------------------------------------------------------------------- showDialogNewMessage

}