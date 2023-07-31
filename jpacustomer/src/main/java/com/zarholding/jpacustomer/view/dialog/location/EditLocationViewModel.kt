package com.zarholding.jpacustomer.view.dialog.location

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.database.entity.UserInfoEntity
import com.hoomanholding.applibrary.model.repository.UserRepository
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.zarholding.jpacustomer.model.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by m-latifi on 5/27/2023.
 */

@HiltViewModel
class EditLocationViewModel @Inject constructor(
    private val customerRepository: CustomerRepository,
    private val userRepository: UserRepository
): JpaViewModel() {


    val editLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val userLiveData: MutableLiveData<UserInfoEntity> by lazy {
        MutableLiveData<UserInfoEntity>()
    }


    //---------------------------------------------------------------------------------------------- requestEditCustomerLocation
    fun requestEditCustomerLocation(lat: Double, long: Double) {
        viewModelScope.launch(IO + exceptionHandler()) {
            callApi(
                request = customerRepository.requestEditCustomerLocation(lat, long),
                onReceiveData = { editCustomerLocation(lat, long, it) }
            )
        }
    }
    //---------------------------------------------------------------------------------------------- requestEditCustomerLocation



    //---------------------------------------------------------------------------------------------- editCustomerLocation
    private fun editCustomerLocation(lat: Double, long: Double, isEdit: Boolean) {
        viewModelScope.launch(IO){
            userRepository.updateXY(lat, long)
            delay(500)
            editLiveData.postValue(isEdit)
        }
    }
    //---------------------------------------------------------------------------------------------- editCustomerLocation


    //---------------------------------------------------------------------------------------------- getUserInfo
    fun getUserInfo() {
        val user = userRepository.getUser()
        user?.let {
            userLiveData.postValue(it)
        }
    }
    //---------------------------------------------------------------------------------------------- getUserInfo

}