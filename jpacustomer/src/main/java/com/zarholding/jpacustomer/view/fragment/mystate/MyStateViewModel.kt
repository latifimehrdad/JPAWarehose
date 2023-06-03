package com.zarholding.jpacustomer.view.fragment.mystate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.database.entity.UserInfoEntity
import com.hoomanholding.applibrary.model.data.response.customer.CustomerStateModel
import com.hoomanholding.applibrary.model.repository.TokenRepository
import com.hoomanholding.applibrary.model.repository.UserRepository
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.zarholding.jpacustomer.model.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by m-latifi 5/24/2023.
 */

@HiltViewModel
class MyStateViewModel @Inject constructor(
    private val customerRepository: CustomerRepository,
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository
): JpaViewModel() {


    val customerStateModel: MutableLiveData<CustomerStateModel> by lazy { MutableLiveData<CustomerStateModel>() }
    val userInfoLiveData: MutableLiveData<UserInfoEntity> by lazy {
        MutableLiveData<UserInfoEntity>()
    }


    //---------------------------------------------------------------------------------------------- getCustomerState
    fun getCustomerState() {
        viewModelScope.launch(IO + exceptionHandler()) {
            if (customerStateModel.value == null) {
                val response = checkResponse(customerRepository.requestGetCustomerState())
                response?.let { customerStateModel.postValue(it) }
            }
        }
    }
    //---------------------------------------------------------------------------------------------- getCustomerState



    //---------------------------------------------------------------------------------------------- getUserInfo
    fun getUserInfo() {
        val userInfo = userRepository.getUser()
        userInfo?.let {
            userInfoLiveData.postValue(it)
        }
    }
    //---------------------------------------------------------------------------------------------- getUserInfo


    //---------------------------------------------------------------------------------------------- getBearerToken
    fun getBearerToken() = tokenRepository.getBearerToken()
    //---------------------------------------------------------------------------------------------- getBearerToken


}