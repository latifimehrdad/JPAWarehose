package com.hoomanholding.applibrary.view.fragment

import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.tools.SingleLiveEvent
import com.hoomanholding.applibrary.model.data.database.entity.RoleEntity
import com.hoomanholding.applibrary.model.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository
) : JpaViewModel() {

    val successLiveData = SingleLiveEvent<Boolean>()

    //---------------------------------------------------------------------------------------------- userIsEntered
    fun userIsEntered() = userRepository.isEntered()
    //---------------------------------------------------------------------------------------------- userIsEntered


    //---------------------------------------------------------------------------------------------- requestGetData
    fun requestGetData() {
        viewModelScope.launch(IO){
            requestUserInfo().join()
            requestUserPermission().join()
            getAllData().join()
        }
    }
    //---------------------------------------------------------------------------------------------- requestGetData


    //---------------------------------------------------------------------------------------------- requestUserInfo
    private fun requestUserInfo(): Job {
        return CoroutineScope(IO + exceptionHandler()).launch {
            delay(500)
            val response = checkResponse(userRepository.requestUserInfo())
            response?.let { userRepository.insertUserInfo(it) }
        }
    }
    //---------------------------------------------------------------------------------------------- requestUserInfo


    //---------------------------------------------------------------------------------------------- requestUserPermission
    private fun requestUserPermission(): Job {
        return CoroutineScope(IO + exceptionHandler()).launch {
            delay(500)
            val response = checkResponse(userRepository.requestUserPermission())
            response?.let {permissions ->
                val roles: List<RoleEntity> = permissions.map { RoleEntity(it) }
                userRepository.insertUserRole(roles)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- requestUserPermission


    //---------------------------------------------------------------------------------------------- getAllData
    private fun getAllData(): Job {
        return CoroutineScope(Main).launch {
            successLiveData.value = true
        }
    }
    //---------------------------------------------------------------------------------------------- getAllData

}