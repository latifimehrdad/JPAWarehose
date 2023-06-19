package com.hoomanholding.applibrary.view.fragment

import android.content.SharedPreferences
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.tools.SingleLiveEvent
import com.hoomanholding.applibrary.model.data.database.entity.RoleEntity
import com.hoomanholding.applibrary.model.data.response.update.AppVersionModel
import com.hoomanholding.applibrary.model.repository.AppUpdateRepository
import com.hoomanholding.applibrary.model.repository.UserRepository
import com.hoomanholding.applibrary.tools.CompanionValues
import com.zar.core.tools.manager.DeviceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val appUpdateRepository: AppUpdateRepository,
    private val deviceManager: DeviceManager
) : JpaViewModel() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    val successLiveData = SingleLiveEvent<Boolean>()
    val userIsEnteredLiveData = SingleLiveEvent<Boolean>()
    val downloadVersionLiveData = SingleLiveEvent<String>()


    //---------------------------------------------------------------------------------------------- userIsEntered
    private fun userIsEntered() {
        userIsEnteredLiveData.postValue(userRepository.isEntered())
    }
    //---------------------------------------------------------------------------------------------- userIsEntered


    //---------------------------------------------------------------------------------------------- requestGetAppVersion
    fun requestGetAppVersion(appName: String) {
        viewModelScope.launch(IO + exceptionHandler()) {
            val response = checkResponse(appUpdateRepository.requestGetAppVersion(appName))
            response?.let {
                checkAppVersion(it)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- requestGetAppVersion


    //---------------------------------------------------------------------------------------------- checkAppVersion
    private fun checkAppVersion(appVersionModel: AppVersionModel) {
        val currentVersion = deviceManager.appVersionCode()
        if (currentVersion < appVersionModel.currentVersion) {
            appVersionModel.fileName?.let {
                downloadVersionLiveData.postValue(it)
            }
        } else
            userIsEntered()
    }
    //---------------------------------------------------------------------------------------------- checkAppVersion


    //---------------------------------------------------------------------------------------------- requestGetData
    fun requestGetData() {
        viewModelScope.launch(IO + exceptionHandler()) {
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
            response?.let {
                userRepository.insertUserInfo(it)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- requestUserInfo


    //---------------------------------------------------------------------------------------------- requestUserPermission
    private fun requestUserPermission(): Job {
        return CoroutineScope(IO + exceptionHandler()).launch {
            delay(500)
            val response = checkResponse(userRepository.requestUserPermission())
            response?.let { permissions ->
                val roles: List<RoleEntity> = permissions.map { RoleEntity(it) }
                userRepository.insertUserRole(roles)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- requestUserPermission


    //---------------------------------------------------------------------------------------------- getAllData
    private fun getAllData(): Job {
        return viewModelScope.launch(IO + exceptionHandler()) {
            successLiveData.postValue(true)
        }
    }
    //---------------------------------------------------------------------------------------------- getAllData


    //---------------------------------------------------------------------------------------------- saveNewIp
    fun saveNewIp(ip: String?) {
        sharedPreferences
            .edit()
            .putString(CompanionValues.URL, ip)
            .apply()
    }
    //---------------------------------------------------------------------------------------------- saveNewIp

}