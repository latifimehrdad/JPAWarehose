package com.zarholding.jpacustomer.view.fragment.report.balance

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.database.entity.UserInfoEntity
import com.hoomanholding.applibrary.model.data.response.report.CustomerBalanceReportDetailModel
import com.hoomanholding.applibrary.model.repository.ReportRepository
import com.hoomanholding.applibrary.model.repository.TokenRepository
import com.hoomanholding.applibrary.model.repository.UserRepository
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by m-latifi on 6/7/2023.
 */

@HiltViewModel
class CustomerBalanceReportViewModel @Inject constructor(
    private val reportRepository: ReportRepository,
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository
): JpaViewModel() {

    val userInfoLiveData: MutableLiveData<UserInfoEntity> by lazy {
        MutableLiveData<UserInfoEntity>()
    }

    val reportDetailLiveData: MutableLiveData<List<CustomerBalanceReportDetailModel>> by lazy {
        MutableLiveData<List<CustomerBalanceReportDetailModel>>()
    }


    //---------------------------------------------------------------------------------------------- getUserInfo
    fun getUserInfo() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler()) {
            val userInfo = userRepository.getUser()
            userInfo?.let {
                userInfoLiveData.postValue(userInfo)
                requestCustomerBalanceDetail(userInfo.id)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- getUserInfo




    //---------------------------------------------------------------------------------------------- requestCustomerBalanceDetail
    private fun requestCustomerBalanceDetail(customerId : Long){
        viewModelScope.launch(Dispatchers.IO + exceptionHandler()) {
            val response = checkResponse(reportRepository.requestCustomerBalanceDetail(customerId))
            response?.let {
                reportDetailLiveData.postValue(it)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- requestCustomerBalanceDetail


    //---------------------------------------------------------------------------------------------- getBearerToken
    fun getBearerToken() = tokenRepository.getBearerToken()
    //---------------------------------------------------------------------------------------------- getBearerToken


}