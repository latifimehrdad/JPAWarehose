package com.hoomanholding.jpawarehose.view.activity

import android.content.SharedPreferences
import com.hoomanholding.jpawarehose.R
import com.hoomanholding.jpawarehose.model.repository.UserRepository
import com.hoomanholding.jpawarehose.utility.CompanionValues
import com.hoomanholding.jpawarehose.JpaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val userRepository: UserRepository
) : JpaViewModel() {

    var actionImageViewShelf = R.id.action_goto_SaveReceiptFragment

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
        val roleReceiptAdd = userRepository.getPermission("Warehouse.Receipt.Add")
        roleReceiptAdd?.let {
            actionImageViewShelf = R.id.action_goto_SaveReceiptFragment
        } ?: run {
            val roleReceiptView = userRepository.getPermission("Warehouse.Receipt.View")
            roleReceiptView?.let {
                actionImageViewShelf = R.id.action_goto_ArrangeFragment
            }?: run {
                actionImageViewShelf = 0
            }
        }
    }
    //---------------------------------------------------------------------------------------------- setActionImageViewShelf


}