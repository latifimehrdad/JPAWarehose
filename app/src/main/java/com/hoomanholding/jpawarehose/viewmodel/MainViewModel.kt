package com.hoomanholding.jpawarehose.viewmodel

import android.content.SharedPreferences
import com.hoomanholding.jpawarehose.utility.CompanionValues
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,

) : JpaViewModel() {


    //---------------------------------------------------------------------------------------------- deleteAllData
    fun deleteAllData() {
        sharedPreferences
            .edit()
            .putString(CompanionValues.TOKEN, null)
            .putInt(CompanionValues.notificationLast, 0)
            .apply()
    }
    //---------------------------------------------------------------------------------------------- deleteAllData


}