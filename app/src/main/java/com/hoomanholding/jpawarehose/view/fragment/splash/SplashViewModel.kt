package com.hoomanholding.jpawarehose.view.fragment.splash

import com.hoomanholding.jpawarehose.R
import com.hoomanholding.jpawarehose.model.repository.UserRepository
import com.hoomanholding.applibrary.utility.SingleLiveEvent
import com.hoomanholding.jpawarehose.di.ResourcesProvider
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.hoomanholding.jpawarehose.model.data.database.entity.RoleEntity
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
    private val userRepository: UserRepository,
    private val resourcesProvider: ResourcesProvider
) : JpaViewModel() {

    val successLiveData =
        SingleLiveEvent<Boolean>()

    //---------------------------------------------------------------------------------------------- userIsEntered
    fun userIsEntered() = userRepository.isEntered()
    //---------------------------------------------------------------------------------------------- userIsEntered


    //---------------------------------------------------------------------------------------------- requestGetData
    fun requestGetData() {
        job?.cancel()
        job = CoroutineScope(IO).launch {
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
            val response = userRepository.requestUserInfo()
            if (response?.isSuccessful == true) {
                val userInfo = response.body()
                userInfo?.let {
                    if (it.hasError)
                        setMessage(it.message)
                    else {
                        it.data?.let { userInfoEntity ->
                            userRepository.insertUserInfo(userInfoEntity)
                        } ?: run {
                            setMessage(resourcesProvider.getString(R.string.dataReceivedIsEmpty))
                        }
                    }
                } ?: run {
                    setMessage(resourcesProvider.getString(R.string.dataReceivedIsEmpty))
                }
            } else
                setMessage(response)
        }
    }
    //---------------------------------------------------------------------------------------------- requestUserInfo


    //---------------------------------------------------------------------------------------------- requestUserPermission
    private fun requestUserPermission(): Job {
        return CoroutineScope(IO + exceptionHandler()).launch {
            delay(500)
            val response = userRepository.requestUserPermission()
            if (response?.isSuccessful == true) {
                val userPermissionResponse = response.body()
                userPermissionResponse?.let { userPermission ->
                    if (userPermission.hasError)
                        setMessage(userPermission.message)
                    else {
                        userPermission.data?.let { permissions ->
                            val roles: List<RoleEntity> = permissions.map { RoleEntity(it) }
                            userRepository.insertUserRole(roles)
                        } ?: run {
                            setMessage(resourcesProvider.getString(R.string.dataReceivedIsEmpty))
                        }
                    }
                } ?: run {
                    setMessage(resourcesProvider.getString(R.string.dataReceivedIsEmpty))
                }
            } else {
                setMessage(response)
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