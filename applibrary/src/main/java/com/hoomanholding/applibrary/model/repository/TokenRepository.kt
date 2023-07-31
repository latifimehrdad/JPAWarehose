package com.hoomanholding.applibrary.model.repository

import com.hoomanholding.applibrary.tools.SharedPreferencesManager
import javax.inject.Inject

/**
 * Created by m-latifi on 11/22/2022.
 */

class TokenRepository @Inject constructor(
    private val sharedPreferencesManager: SharedPreferencesManager
) {

    //---------------------------------------------------------------------------------------------- getBearerToken
    fun getBearerToken() = sharedPreferencesManager.getBearerToken()
    //---------------------------------------------------------------------------------------------- getBearerToken

    //---------------------------------------------------------------------------------------------- getToken
    fun getToken() = sharedPreferencesManager.getToken()
    //---------------------------------------------------------------------------------------------- getToken


    //---------------------------------------------------------------------------------------------- deleteToken
    fun deleteToken() {
        sharedPreferencesManager.deleteUserLogin()
    }
    //---------------------------------------------------------------------------------------------- deleteToken
}