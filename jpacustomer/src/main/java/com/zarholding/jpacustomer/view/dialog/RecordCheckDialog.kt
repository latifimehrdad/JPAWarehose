package com.zarholding.jpacustomer.view.dialog

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.applibrary.ext.downloadProfileImage
import com.hoomanholding.applibrary.ext.setTitleAndValue
import com.hoomanholding.applibrary.model.data.enums.EnumEntityType
import com.hoomanholding.applibrary.model.data.enums.EnumSystemType
import com.hoomanholding.applibrary.model.data.request.AddToBasket
import com.hoomanholding.applibrary.model.data.response.check.RecordCheckNotifyModel
import com.hoomanholding.applibrary.model.data.response.product.ProductModel
import com.hoomanholding.applibrary.tools.RoleManager
import com.zar.core.enums.EnumApiError
import com.zar.core.tools.extensions.split
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.DialogProductDetailBinding
import com.zarholding.jpacustomer.databinding.DialogRecordCheckBinding
import com.zarholding.jpacustomer.view.activity.MainActivity
import com.zarholding.jpacustomer.view.adapter.recycler.RecordCheckAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by m-latifi on 6/4/2023.
 */

@AndroidEntryPoint
class RecordCheckDialog(
    private val item: RecordCheckNotifyModel
) : DialogFragment() {

    private lateinit var binding: DialogRecordCheckBinding

    @Inject
    lateinit var roleManager: RoleManager


    //---------------------------------------------------------------------------------------------- onCreateView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogRecordCheckBinding.inflate(inflater, container, false)
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
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        window?.attributes = lp
        (activity as MainActivity?)?.hideFragmentContainer()
        getValueToXml()
        setListener()
    }
    //---------------------------------------------------------------------------------------------- onCreateView


    //---------------------------------------------------------------------------------------------- getValueToXml
    private fun getValueToXml() {
        binding.textViewTitle.text = item.message
        val adapter = RecordCheckAdapter(items = item.checkList)
        val manager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.recyclerViewList.adapter = adapter
        binding.recyclerViewList.layoutManager = manager
    }
    //---------------------------------------------------------------------------------------------- getValueToXml


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.buttonYes.setOnClickListener { dismiss() }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- onDismiss
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (item.isForceExit) {
            (activity as MainActivity?)?.showFragmentContainer()
            (activity as MainActivity?)?.gotoFirstFragment()
//            activity?.finish()
        } else
            activity?.let {
                (it as MainActivity).showFragmentContainer()
            }
    }
    //---------------------------------------------------------------------------------------------- onDismiss
}