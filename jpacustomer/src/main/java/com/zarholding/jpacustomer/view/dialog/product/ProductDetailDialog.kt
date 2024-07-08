package com.zarholding.jpacustomer.view.dialog.product

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.hoomanholding.applibrary.ext.downloadProfileImage
import com.hoomanholding.applibrary.ext.setTitleAndValue
import com.hoomanholding.applibrary.model.data.enums.EnumEntityType
import com.hoomanholding.applibrary.model.data.enums.EnumSystemType
import com.hoomanholding.applibrary.model.data.request.AddToBasket
import com.hoomanholding.applibrary.model.data.response.product.ProductModel
import com.hoomanholding.applibrary.tools.RoleManager
import com.zar.core.enums.EnumApiError
import com.zar.core.tools.extensions.split
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.DialogProductDetailBinding
import com.zarholding.jpacustomer.model.EnumProductPageType
import com.zarholding.jpacustomer.view.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by m-latifi on 6/4/2023.
 */

@AndroidEntryPoint
class ProductDetailDialog(
    private val click: Click,
    private val product: ProductModel,
    private val type: EnumProductPageType
) : DialogFragment() {

    private val viewModel: ProductDetailViewModel by viewModels()
    private lateinit var binding: DialogProductDetailBinding

    @Inject
    lateinit var roleManager: RoleManager


    //---------------------------------------------------------------------------------------------- Click
    interface Click {
        fun select()
    }
    //---------------------------------------------------------------------------------------------- Click


    //---------------------------------------------------------------------------------------------- onCreateView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
    //---------------------------------------------------------------------------------------------- onCreateView


    //---------------------------------------------------------------------------------------------- onCreateView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lp = WindowManager.LayoutParams()
        val window = dialog?.window
        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 50)
        window?.setBackgroundDrawable(inset)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = lp
        (activity as MainActivity?)?.hideFragmentContainer()
        observeLiveData()
        getValueToXml()
        setListener()
    }
    //---------------------------------------------------------------------------------------------- onCreateView


    //---------------------------------------------------------------------------------------------- getValueToXml
    private fun getValueToXml() {
        binding.imageviewStatus.visibility = View.GONE
        binding.imageviewDiscount.visibility = View.GONE
        binding.editTextCount.setText("0")
        binding.textViewName.text = product.productName
        binding.touchImageView.downloadProfileImage(
            url = "${product.productCode}.PNG",
            systemType = EnumSystemType.Customers.name,
            entityType = EnumEntityType.ProductImage.name,
            token = viewModel.getBearerToken(),
            placeholder = R.drawable.a_ic_logo
        )
        binding.textViewPrice.setTitleAndValue(
            title = getString(R.string.price),
            splitter = getString(R.string.space),
            last = getString(R.string.rial),
            value = product.price
        )
        binding.textViewCode.setTitleAndValue(
            title = getString(R.string.productCode),
            splitter = getString(R.string.colon),
            value = product.productCode
        )

        when(type) {
            EnumProductPageType.Product ->
                binding.buttonYes.setText(R.string.addToBasket)
            EnumProductPageType.Return ->
                binding.buttonYes.setText(R.string.addToReturn)
        }

        binding.textViewCount.text = getString(
            R.string.countInBoxAndPackage,
            product.productCountinPackage.split(),
            product.productCountinBox.split()
        )

        if (product.isCash)
            binding.imageviewStatus.visibility = View.VISIBLE

        if (product.isSpecial)
            binding.imageviewDiscount.visibility = View.VISIBLE

        if (roleManager.isAccessToBasketMenu()) {
            binding.linearLayoutCount.visibility = View.VISIBLE
            binding.buttonYes.visibility = View.VISIBLE
        } else {
            binding.linearLayoutCount.visibility = View.GONE
            binding.buttonYes.visibility = View.GONE
        }

    }
    //---------------------------------------------------------------------------------------------- getValueToXml


    //---------------------------------------------------------------------------------------------- observeLiveData
    private fun observeLiveData() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            (activity as MainActivity?)?.showMessage(it.message)
            binding.buttonYes.stopLoading()
            when (it.type) {
                EnumApiError.UnAuthorization -> (activity as MainActivity?)?.gotoFirstFragment()
                else -> {}
            }
        }


        viewModel.addToBasketLiveData.observe(viewLifecycleOwner) {
            binding.buttonYes.stopLoading()
            click.select()
            dismiss()
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveData


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.imageViewMinus.setOnClickListener {
            val count = getEditTextCount() - 1
            if (count >= 0)
                binding.editTextCount.setText((count).toString())
        }

        binding.imageViewPlus.setOnClickListener {
            val count = getEditTextCount() + 1
            if (count <= 999)
                binding.editTextCount.setText((count).toString())
        }

        binding.buttonYes.setOnClickListener {
            val count = getEditTextCount()
            if (count == 0 || binding.buttonYes.isLoading)
                return@setOnClickListener
            val request = AddToBasket(
                ProductId = product.id,
                Price = product.price,
                Count = count
            )
            binding.buttonYes.startLoading(getString(R.string.bePatient))
            when(type) {
                EnumProductPageType.Product -> viewModel.requestAddToBasket(request)
                EnumProductPageType.Return -> viewModel.requestAddToReturn(request)
            }
        }

        binding.buttonNo.setOnClickListener { dismiss() }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- getEditTextCount
    private fun getEditTextCount(): Int {
        val text = binding.editTextCount.text.toString()
        return if (text.isEmpty())
            0
        else if (text.isDigitsOnly())
            text.toInt()
        else {
            binding.editTextCount.setText("0")
            0
        }
    }
    //---------------------------------------------------------------------------------------------- getEditTextCount


    //---------------------------------------------------------------------------------------------- onDismiss
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.let {
            (it as MainActivity).showFragmentContainer()
        }
    }
    //---------------------------------------------------------------------------------------------- onDismiss
}