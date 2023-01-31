package com.hoomanholding.jpawarehose.viewmodel

import android.content.SharedPreferences
import com.hoomanholding.jpawarehose.R
import com.hoomanholding.jpawarehose.model.repository.UserRepository
import com.hoomanholding.jpawarehose.utility.CompanionValues
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val userRepository: UserRepository
) : JpaViewModel() {

    var actionImageViewShelf = R.id.action_goto_ArrangeFragment

    //---------------------------------------------------------------------------------------------- deleteAllData
    fun deleteAllData() {
        sharedPreferences
            .edit()
            .putString(CompanionValues.TOKEN, null)
            .putInt(CompanionValues.notificationLast, 0)
            .apply()
    }
    //---------------------------------------------------------------------------------------------- deleteAllData


    //---------------------------------------------------------------------------------------------- setActionImageViewShelf
    fun setActionImageViewShelf() {
/*        val roleReceiptAdd = userRepository.getPermission("Warehouse.Receipt.Add")
        roleReceiptAdd?.let {
            actionImageViewShelf = R.id.action_goto_SaveReceiptFragment
        } ?: run {

        }*/
    }
    //---------------------------------------------------------------------------------------------- setActionImageViewShelf


}