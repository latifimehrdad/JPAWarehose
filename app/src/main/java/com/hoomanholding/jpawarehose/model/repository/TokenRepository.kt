package com.hoomanholding.jpawarehose.model.repository

import android.content.SharedPreferences
import com.hoomanholding.jpawarehose.utility.CompanionValues
import javax.inject.Inject

/**
 * Created by m-latifi on 11/22/2022.
 */

class TokenRepository @Inject constructor(private val sp: SharedPreferences ) {

    //---------------------------------------------------------------------------------------------- getBearerToken
    fun getBearerToken() = "Bearer ${sp.getString(CompanionValues.TOKEN, null)}"
    //---------------------------------------------------------------------------------------------- getBearerToken

    //---------------------------------------------------------------------------------------------- getToken
    fun getToken() = sp.getString(CompanionValues.TOKEN, null)
    //---------------------------------------------------------------------------------------------- getToken

}