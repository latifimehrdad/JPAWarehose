package com.hoomanholding.jpamanager.view.dialog.customer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.enums.EnumPeopleType
import com.hoomanholding.applibrary.model.data.request.FilterCustomerRequest
import com.hoomanholding.applibrary.model.data.response.customer.CustomerModel
import com.hoomanholding.applibrary.model.data.response.visitor.VisitorModel
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.hoomanholding.jpamanager.model.repository.CustomerRepository
import com.hoomanholding.jpamanager.model.repository.VisitorRepository
import com.zar.core.tools.extensions.persianNumberToEnglishNumber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * create by m-latifi on 4/12/2023
 */

@HiltViewModel
class PeopleViewModel @Inject constructor(
    private val customerRepository: CustomerRepository,
    private val visitorRepository: VisitorRepository
) : JpaViewModel() {

    private var visitorList: List<VisitorModel>? = null

    val customerLiveData: MutableLiveData<List<CustomerModel>> by lazy {
        MutableLiveData<List<CustomerModel>>()
    }

    val visitorLiveData: MutableLiveData<List<VisitorModel>> by lazy {
        MutableLiveData<List<VisitorModel>>()
    }

    //---------------------------------------------------------------------------------------------- getPeople
    fun getPeople(peopleType: EnumPeopleType, search: String?){
        when(peopleType){
            EnumPeopleType.Customer -> search?.let { requestGetCustomer(search) }
            EnumPeopleType.Visitor -> getVisitor(search)
        }
    }
    //---------------------------------------------------------------------------------------------- getPeople


    //---------------------------------------------------------------------------------------------- requestGetCustomer
    private fun requestGetCustomer(search: String) {
        viewModelScope.launch(IO + exceptionHandler()){
            val temp = search.persianNumberToEnglishNumber()
            val request = FilterCustomerRequest(0,temp)
            val response = checkResponse(customerRepository.requestGetCustomer(request))
            response?.let { customerLiveData.postValue(it) }
        }
    }
    //---------------------------------------------------------------------------------------------- requestGetCustomer


    //---------------------------------------------------------------------------------------------- getVisitor
    private fun getVisitor(search: String?) {
        viewModelScope.launch(IO + exceptionHandler()){
            visitorList?.let {
                findVisitor(search)
            } ?: run {
                val response = checkResponse(visitorRepository.requestGetVisitor())
                response?.let {
                    visitorList = it
                    findVisitor(search)
                }
            }
        }
    }
    //---------------------------------------------------------------------------------------------- getVisitor


    //---------------------------------------------------------------------------------------------- findVisitor
    private fun findVisitor(search: String?) {
        if (visitorList.isNullOrEmpty())
            return
        if (search.isNullOrEmpty())
            visitorLiveData.postValue(visitorList)
        else {
            val find = visitorList!!.filter {visitor ->
                visitor.visitorName?.contains(search) ?: false
            }
            visitorLiveData.postValue(find)
        }
    }
    //---------------------------------------------------------------------------------------------- findVisitor

}