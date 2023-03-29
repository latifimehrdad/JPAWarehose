package com.hoomanholding.jpawarehose.view.activity

import android.content.SharedPreferences
import com.hoomanholding.applibrary.tools.CompanionValues
import com.hoomanholding.jpawarehose.R
import com.hoomanholding.applibrary.model.repository.UserRepository
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val userRepository: UserRepository
) : JpaViewModel() {

    var actionImageViewReceiptAdd = R.id.action_goto_SaveReceiptFragment
    var actionImageViewReceiptView = R.id.action_goto_ArrangeFragment

    //---------------------------------------------------------------------------------------------- deleteAllData
    fun deleteAllData() {
        sharedPreferences
            .edit()
            .putString(CompanionValues.TOKEN, null)
            .apply()
    }
    //---------------------------------------------------------------------------------------------- deleteAllData


    //---------------------------------------------------------------------------------------------- setActionImageViewReceipt
    fun setActionImageViewReceipt() {
        val roleReceiptAdd = userRepository.getPermission("Warehouse.Receipt.Add")
        roleReceiptAdd?.let {
            actionImageViewReceiptAdd = R.id.action_goto_SaveReceiptFragment
        } ?: run {
            actionImageViewReceiptAdd = 0
        }

        val roleReceiptView = userRepository.getPermission("Warehouse.Receipt.View")
        roleReceiptView?.let {
            actionImageViewReceiptView = R.id.action_goto_ArrangeFragment
        } ?: run {
            actionImageViewReceiptView = 0
        }
    }
    //---------------------------------------------------------------------------------------------- setActionImageViewReceipt


}