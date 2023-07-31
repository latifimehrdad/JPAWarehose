package com.hoomanholding.applibrary.view.fragment

import android.os.Environment
import android.os.StatFs
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.tools.SingleLiveEvent
import com.hoomanholding.applibrary.model.data.database.entity.RoleEntity
import com.hoomanholding.applibrary.model.data.response.update.AppVersionModel
import com.hoomanholding.applibrary.model.repository.AppUpdateRepository
import com.hoomanholding.applibrary.model.repository.UserRepository
import com.hoomanholding.applibrary.tools.SharedPreferencesManager
import com.zar.core.tools.manager.DeviceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class SplashViewModel @Inject constructor() : JpaViewModel() {

    @Inject lateinit var sharedPreferencesManager: SharedPreferencesManager
    @Inject lateinit var userRepository: UserRepository
    @Inject lateinit var appUpdateRepository: AppUpdateRepository
    @Inject lateinit var deviceManager: DeviceManager


    val successLiveData = SingleLiveEvent<Boolean>()
    val userIsEnteredLiveData = SingleLiveEvent<Boolean>()
    val downloadVersionLiveData = SingleLiveEvent<String>()
    val appDescriptionLiveData = SingleLiveEvent<String?>()


    //---------------------------------------------------------------------------------------------- userIsEntered
    private fun userIsEntered() {
        userIsEnteredLiveData.postValue(userRepository.isEntered())
    }
    //---------------------------------------------------------------------------------------------- userIsEntered


    //---------------------------------------------------------------------------------------------- requestGetAppVersion
    fun requestGetAppVersion(appName: String) {
        viewModelScope.launch(IO + exceptionHandler()) {
            callApi(
                request = appUpdateRepository.requestGetAppVersion(appName),
                onReceiveData = {
                    appDescriptionLiveData.postValue(it.appDescription)
                    checkAppVersion(it)
                }
            )
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
            requestUserInfo(sharedPreferencesManager.getFirebaseToken()).join()
            requestUserPermission().join()
            getAllData().join()
        }
    }
    //---------------------------------------------------------------------------------------------- requestGetData


    //---------------------------------------------------------------------------------------------- requestUserInfo
    private fun requestUserInfo(fireBaseToken: String?): Job {
        return CoroutineScope(IO + exceptionHandler()).launch {
            Log.e("meri", "requestGetData : ${fireBaseToken.toString()}")
            delay(500)
            callApi(
                request = userRepository.requestUserInfo(fireBaseToken),
                onReceiveData = { userRepository.insertUserInfo(it) }
            )
        }
    }
    //---------------------------------------------------------------------------------------------- requestUserInfo


    //---------------------------------------------------------------------------------------------- requestUserPermission
    private fun requestUserPermission(): Job {
        return CoroutineScope(IO + exceptionHandler()).launch {
            delay(500)
            callApi(
                request = userRepository.requestUserPermission(),
                onReceiveData = { permissions ->
                    val roles: List<RoleEntity> = permissions.map { RoleEntity(it) }
                    userRepository.insertUserRole(roles)
                }
            )
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
        sharedPreferencesManager.saveNewIp(ip)
    }
    //---------------------------------------------------------------------------------------------- saveNewIp


    //---------------------------------------------------------------------------------------------- getInternalMemoryFreeSize
    fun getInternalMemoryFreeSize(): Long {
        val stat = StatFs(Environment.getExternalStorageDirectory().path)
        val bytesAvailable: Long = stat.blockSizeLong * stat.availableBlocksLong
        return bytesAvailable / (1024 * 1024)
    }
    //---------------------------------------------------------------------------------------------- getInternalMemoryFreeSize

}