package com.hoomanholding.jpawarehose.model.repository

import com.hoomanholding.jpawarehose.model.api.Api
import com.hoomanholding.jpawarehose.model.database.dao.UserInfoDao
import com.zar.core.tools.api.apiCall
import com.hoomanholding.jpawarehose.model.database.dao.RoleDao
import com.hoomanholding.jpawarehose.model.database.entity.RoleEntity
import com.hoomanholding.jpawarehose.model.database.entity.UserInfoEntity
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
    suspend fun requestUserInfo() =
        apiCall{ api.requestGetUserInfo(tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestUserInfo



    //---------------------------------------------------------------------------------------------- insertUserInfo
    fun insertUserInfo(user: UserInfoEntity) {
        userInfoDao.insertUserInfo(user)
    }
    //---------------------------------------------------------------------------------------------- insertUserInfo



    //---------------------------------------------------------------------------------------------- requestUserPermission
    suspend fun requestUserPermission() =
        apiCall{ api.requestUserPermission(tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestUserPermission



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
    }
    //---------------------------------------------------------------------------------------------- deleteUser



    //---------------------------------------------------------------------------------------------- isEntered
    fun isEntered() = tokenRepository.getToken()?.let {
        true
    } ?: false
    //---------------------------------------------------------------------------------------------- isEntered


    //---------------------------------------------------------------------------------------------- getBearerToken
    fun getBearerToken() = tokenRepository.getBearerToken()
    //---------------------------------------------------------------------------------------------- getBearerToken


    //---------------------------------------------------------------------------------------------- getPermission
    fun getPermission(permission : String) = userInfoDao.getUserInfo()
    //---------------------------------------------------------------------------------------------- getPermission
}