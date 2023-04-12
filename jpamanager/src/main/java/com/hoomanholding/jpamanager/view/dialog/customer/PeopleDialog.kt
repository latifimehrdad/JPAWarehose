package com.hoomanholding.jpamanager.view.dialog.customer

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.applibrary.model.data.enums.EnumPeopleType
import com.hoomanholding.applibrary.model.data.response.customer.CustomerModel
import com.hoomanholding.applibrary.model.data.response.visitor.VisitorModel
import com.hoomanholding.jpamanager.databinding.DialogPeopleBinding
import com.hoomanholding.jpamanager.view.adapter.holder.CustomerSelectHolder
import com.hoomanholding.jpamanager.view.adapter.holder.VisitorSelectHolder
import com.hoomanholding.jpamanager.view.adapter.recycler.CustomerSelectAdapter
import com.hoomanholding.jpamanager.view.adapter.recycler.VisitorSelectAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

/**
 * create by m-latifi on 4/12/2023
 */

@AndroidEntryPoint
class PeopleDialog(
    private val peopleType: EnumPeopleType,
    private val chooseItem: Click
) : DialogFragment() {

    lateinit var binding: DialogPeopleBinding

    private val peopleViewModel: PeopleViewModel by viewModels()

    private var job: Job? = null

    //---------------------------------------------------------------------------------------------- Click
    interface Click {
        fun selectCustomer(item: CustomerModel)
        fun selectVisitor(item: VisitorModel)
    }
    //---------------------------------------------------------------------------------------------- Click


    //---------------------------------------------------------------------------------------------- onCreateView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogPeopleBinding.inflate(inflater, container, false)
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
        setListener()
        observeUserMutableLiveData()
        if (peopleType == EnumPeopleType.Visitor)
            getPeople(null)
    }
    //---------------------------------------------------------------------------------------------- onCreateView


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {

        binding.imageViewClose.setOnClickListener { dismiss() }

        binding.textInputEditTextSearch.addTextChangedListener {
            binding.recyclerViewPersonnel.adapter = null
            job?.cancel()
            createJobForSearch(it.toString())
        }

    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- createJobForSearch
    private fun createJobForSearch(search: String) {
        job = CoroutineScope(IO).launch {
            delay(800)
            withContext(Main) {
                if (search.isNotEmpty() && peopleType == EnumPeopleType.Customer)
                    getPeople(search)
                else
                    getPeople(search)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- createJobForSearch


    //---------------------------------------------------------------------------------------------- observeUserMutableLiveData
    private fun observeUserMutableLiveData() {
        peopleViewModel.customerLiveData.observe(viewLifecycleOwner) {
            binding.textViewLoading.visibility = View.GONE
            setCustomerAdapter(it)
        }

        peopleViewModel.visitorLiveData.observe(viewLifecycleOwner){
            binding.textViewLoading.visibility = View.GONE
            setVisitorAdapter(it)
        }
    }
    //---------------------------------------------------------------------------------------------- observeUserMutableLiveData


    //---------------------------------------------------------------------------------------------- getPeople
    private fun getPeople(search: String?) {
        binding.textViewLoading.visibility = View.VISIBLE
        binding.recyclerViewPersonnel.adapter = null
        peopleViewModel.getPeople(peopleType, search)
    }
    //---------------------------------------------------------------------------------------------- getPeople


    //---------------------------------------------------------------------------------------------- setCustomerAdapter
    private fun setCustomerAdapter(items: List<CustomerModel>) {
        val select = object : CustomerSelectHolder.Click {
            override fun select(item: CustomerModel) {
                chooseItem.selectCustomer(item)
                dismiss()
            }
        }
        val adapter = CustomerSelectAdapter(items.toMutableList(), select)
        val manager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.recyclerViewPersonnel.layoutManager = manager
        binding.recyclerViewPersonnel.adapter = adapter
    }
    //---------------------------------------------------------------------------------------------- setCustomerAdapter




    //---------------------------------------------------------------------------------------------- setVisitorAdapter
    private fun setVisitorAdapter(items: List<VisitorModel>) {
        val select = object : VisitorSelectHolder.Click {
            override fun select(item: VisitorModel) {
                chooseItem.selectVisitor(item)
                dismiss()
            }
        }
        val adapter = VisitorSelectAdapter(items.toMutableList(), select)
        val manager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.recyclerViewPersonnel.layoutManager = manager
        binding.recyclerViewPersonnel.adapter = adapter
    }
    //---------------------------------------------------------------------------------------------- setVisitorAdapter



    //---------------------------------------------------------------------------------------------- dismiss
    override fun dismiss() {
        super.dismiss()
        job?.cancel()
    }
    //---------------------------------------------------------------------------------------------- dismiss
}