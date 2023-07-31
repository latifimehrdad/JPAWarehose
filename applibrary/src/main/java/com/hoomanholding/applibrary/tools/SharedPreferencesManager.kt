package com.hoomanholding.applibrary.tools

import android.content.SharedPreferences
import javax.inject.Inject

/**
 * Created by m-latifi on 7/31/2023.
 */

class SharedPreferencesManager @Inject constructor(private val sp: SharedPreferences) {

    private val tokenKey = "token"
    private val userNameKey = "userName"
    private val passcodeKey = "password"
    private val biometricKey = "biometric"
    private val urlKey = "newUrl"
    private val fireBaseTokenKey = "firebase_token"

    //---------------------------------------------------------------------------------------------- isBiometricEnable
    fun isBiometricEnable() = sp.getBoolean(biometricKey, false)
    //---------------------------------------------------------------------------------------------- isBiometricEnable


    //---------------------------------------------------------------------------------------------- getUserName
    fun getUserName() = sp.getString(userNameKey, "")
    //---------------------------------------------------------------------------------------------- getUserName


    //---------------------------------------------------------------------------------------------- getPassword
    fun getPassword() = sp.getString(passcodeKey, "")
    //---------------------------------------------------------------------------------------------- getPassword


    //---------------------------------------------------------------------------------------------- saveUserLogin
    fun saveUserLogin(token: String, userName: String, password: String) {
        sp.edit()
            .putString(this.tokenKey, token)
            .putString(userNameKey, userName)
            .putString(passcodeKey, password)
            .apply()
    }
    //---------------------------------------------------------------------------------------------- saveUserLogin



    //---------------------------------------------------------------------------------------------- saveToken
    fun saveToken(token: String?) {
        sp.edit()
            .putString(this.tokenKey, token)
            .apply()
    }
    //---------------------------------------------------------------------------------------------- saveToken



    //---------------------------------------------------------------------------------------------- getBearerToken
    fun getBearerToken() = "Bearer ${sp.getString(tokenKey, null)}"
    //---------------------------------------------------------------------------------------------- getBearerToken


    //---------------------------------------------------------------------------------------------- getToken
    fun getToken() = sp.getString(tokenKey, null)
    //---------------------------------------------------------------------------------------------- getToken


    //---------------------------------------------------------------------------------------------- changeBiometricEnable
    fun changeBiometricEnable(biometric: Boolean) {
        sp.edit().putBoolean(biometricKey, biometric).apply()
    }
    //---------------------------------------------------------------------------------------------- changeBiometricEnable


    //---------------------------------------------------------------------------------------------- saveNewIp
    fun saveNewIp(ip: String?) {
        sp.edit().putString(urlKey, ip).apply()
    }
    //---------------------------------------------------------------------------------------------- saveNewIp


    //---------------------------------------------------------------------------------------------- deleteUserLogin
    fun deleteUserLogin() {
        sp.edit()
            .putString(tokenKey, null)
            .apply()
    }
    //---------------------------------------------------------------------------------------------- deleteUserLogin


    //---------------------------------------------------------------------------------------------- getFirebaseToken
    fun getFirebaseToken() = sp.getString(fireBaseTokenKey, null)
    //---------------------------------------------------------------------------------------------- getFirebaseToken



    //---------------------------------------------------------------------------------------------- getFirebaseToken
    fun setFirebaseToken(newToken: String){
        sp.edit().putString(fireBaseTokenKey, newToken).apply()
    }
    //---------------------------------------------------------------------------------------------- getFirebaseToken
}