package com.hoomanholding.jpamanager.view.fragment.lisence.assignment

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.response.user.UserModel
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.model.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * create by m-latifi on 3/6/2023
 */

@HiltViewModel
class LicenseAssignmentViewModel @Inject constructor(
    private val customerRepository: CustomerRepository
) : JpaViewModel() {

    private var usersList: List<UserModel>? = null
    val usersLiveData: MutableLiveData<List<UserModel>?> by lazy { MutableLiveData<List<UserModel>?>() }
    val successLiveDate: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    //---------------------------------------------------------------------------------------------- getAllCustomers
    fun getAllCustomers(search: String) {
        viewModelScope.launch(IO + exceptionHandler()) {
            callApi(
                request = customerRepository.requestGetAllCustomers()
            ) {
                usersList = it
                searchUser(search)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- getAllCustomers


    //---------------------------------------------------------------------------------------------- searchUser
    fun searchUser(search: String) {
        if (search.isEmpty() || usersList.isNullOrEmpty())
            usersLiveData.postValue(usersList)
        else {
            val filter = usersList?.filter { user ->
                user.customerName?.contains(search) == true ||
                        user.storeName?.contains(search) == true ||
                        (search.isDigitsOnly() && user.customerCode == search.toLong())
            }
            usersLiveData.postValue(filter)
        }
    }
    //---------------------------------------------------------------------------------------------- searchUser


    //---------------------------------------------------------------------------------------------- requestAddCustomerLicensing
    fun requestAddCustomerLicensing() {
        viewModelScope.launch(IO + exceptionHandler()) {
            val request = usersList?.filter { user -> user.select }?.map { it.id }
            if (request.isNullOrEmpty())
                setMessage(resourcesProvider.getString(R.string.listIsEmpty))
            else
                callApi(
                    request = customerRepository.requestAddCustomerLicensing(request),
                    showMessageAfterSuccessResponse = true
                ) {
                    if (it)
                        successLiveDate.postValue(it)
                }
        }
    }
    //---------------------------------------------------------------------------------------------- requestAddCustomerLicensing
}