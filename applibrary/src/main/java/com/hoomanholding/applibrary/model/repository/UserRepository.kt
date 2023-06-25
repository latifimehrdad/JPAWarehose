package com.hoomanholding.applibrary.model.repository

import com.hoomanholding.applibrary.model.api.Api
import com.hoomanholding.applibrary.model.data.database.dao.RoleDao
import com.hoomanholding.applibrary.model.data.database.dao.UserInfoDao
import com.hoomanholding.applibrary.model.data.database.entity.RoleEntity
import com.hoomanholding.applibrary.model.data.database.entity.UserInfoEntity
import com.zar.core.tools.api.apiCall
import javax.inject.Inject

/**
 * Created by m-latifi on 11/26/2022.
 */

class UserRepository @Inject constructor(
    private val api: Api,
    private val userInfoDao: UserInfoDao,
    private val roleDao: RoleDao,
    private val tokenRepository: TokenRepository
) {


    //---------------------------------------------------------------------------------------------- requestUserInfo
    suspend fun requestUserInfo(fireBaseToken: String?) =
        apiCall { api.requestGetUserInfo(fireBaseToken, tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestUserInfo


    //---------------------------------------------------------------------------------------------- insertUserInfo
    fun insertUserInfo(user: UserInfoEntity) {
        userInfoDao.insertUserInfo(user)
    }
    //---------------------------------------------------------------------------------------------- insertUserInfo


    //---------------------------------------------------------------------------------------------- requestUserPermission
    suspend fun requestUserPermission() =
        apiCall { api.requestUserPermission(tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestUserPermission


    //---------------------------------------------------------------------------------------------- requestVerifyCode
    suspend fun requestVerifyCode(verificationCode: String, token: String) =
        apiCall { api.requestVerifyCode(verificationCode, token) }
    //---------------------------------------------------------------------------------------------- requestVerifyCode


    //---------------------------------------------------------------------------------------------- requestResendVerificationCode
    suspend fun requestResendVerificationCode(token: String) =
        apiCall { api.requestResendVerificationCode(token) }
    //---------------------------------------------------------------------------------------------- requestResendVerificationCode






    //---------------------------------------------------------------------------------------------- requestChangePassword
    suspend fun requestChangePassword(password: String, newPassword: String, token: String) =
        apiCall {
            api.requestChangePassword(password, newPassword, token)
        }
    //---------------------------------------------------------------------------------------------- requestChangePassword


    //---------------------------------------------------------------------------------------------- getUser
    fun getUser(): UserInfoEntity? {
        return userInfoDao.getUserInfo()
    }
    //---------------------------------------------------------------------------------------------- getUser


    //---------------------------------------------------------------------------------------------- insertUserRole
    fun insertUserRole(roles: List<RoleEntity>) {
        roleDao.deleteAllRecord()
        roleDao.insert(roles)
    }
    //---------------------------------------------------------------------------------------------- insertUserRole


    //---------------------------------------------------------------------------------------------- deleteUser
    fun deleteUser() {
        userInfoDao.deleteAll()
        tokenRepository.deleteToken()
    }
    //---------------------------------------------------------------------------------------------- deleteUser



    //---------------------------------------------------------------------------------------------- updateXY
    fun updateXY(x: Double, y: Double) {
        userInfoDao.updateXY(x, y)
    }
    //---------------------------------------------------------------------------------------------- updateXY


    //---------------------------------------------------------------------------------------------- isEntered
    fun isEntered() = tokenRepository.getToken()?.let {
        true
    } ?: false
    //---------------------------------------------------------------------------------------------- isEntered


    //---------------------------------------------------------------------------------------------- getBearerToken
    fun getBearerToken() = tokenRepository.getBearerToken()
    //---------------------------------------------------------------------------------------------- getBearerToken


    //---------------------------------------------------------------------------------------------- getPermission
    fun getPermission(permission: String) = roleDao.getPermission(permission)
    //---------------------------------------------------------------------------------------------- getPermission
}