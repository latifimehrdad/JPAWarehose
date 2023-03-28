package com.hoomanholding.jpamanager.model.repository

import com.hoomanholding.jpamanager.model.api.Api
import com.hoomanholding.jpamanager.model.data.database.dao.RoleDao
import com.hoomanholding.jpamanager.model.data.database.dao.UserInfoDao
import com.hoomanholding.jpamanager.model.data.database.entity.RoleEntity
import com.hoomanholding.jpamanager.model.data.database.entity.UserInfoEntity
import com.zar.core.tools.api.apiCall
import javax.inject.Inject

/**
 * Created by m-latifi on 11/26/2022.
 */

class UserRepository @Inject constructor(
    private val api: Api,
    private val tokenRepository: TokenRepository,
    private val userInfoDao: UserInfoDao,
    private val roleDao: RoleDao
) {


    //---------------------------------------------------------------------------------------------- isEntered
    fun isEntered() = tokenRepository.getToken()?.let {
        true
    } ?: false
    //---------------------------------------------------------------------------------------------- isEntered


    //---------------------------------------------------------------------------------------------- requestUserInfo
    suspend fun requestUserInfo() =
        apiCall { api.requestGetUserInfo(tokenRepository.getBearerToken()) }
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


    //---------------------------------------------------------------------------------------------- insertUserRole
    fun insertUserRole(roles: List<RoleEntity>) {
        roleDao.deleteAllRecord()
        roleDao.insert(roles)
    }
    //---------------------------------------------------------------------------------------------- insertUserRole


    //---------------------------------------------------------------------------------------------- getUser
    fun getUser(): UserInfoEntity? {
        return userInfoDao.getUserInfo()
    }
    //---------------------------------------------------------------------------------------------- getUser


    //---------------------------------------------------------------------------------------------- deleteUser
    fun deleteUser() {
        userInfoDao.deleteAll()
        tokenRepository.deleteToken()
    }
    //---------------------------------------------------------------------------------------------- deleteUser


    //---------------------------------------------------------------------------------------------- getBearerToken
    fun getBearerToken() = tokenRepository.getBearerToken()
    //---------------------------------------------------------------------------------------------- getBearerToken


    //---------------------------------------------------------------------------------------------- getPermission
    fun getPermission(permission: String) = roleDao.getPermission(permission)
    //---------------------------------------------------------------------------------------------- getPermission

}