package com.hoomanholding.applibrary.model.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Created by m-latifi on 11/26/2022.
 */

@Entity(tableName = "UserInfo")
data class UserInfoEntity(
    val userName : String?,
    val fullName : String?,
    val personnelNumber : String?,
    val systemType: String?,
    val profileImageName: String?,
    val visitorImageName: String,
    val rolesId : List<Long>?,
    val mobileNumber: String?,
    val storeName: String?,
    val address: String?,
    val visitorId: Int,
    val visitorName: String?,
    val visitorMobile: String?,
    val userTopicstr: String?,
    val x: Double,
    val y: Double,
    val isSubset: Boolean
) {
    @PrimaryKey(autoGenerate = false)
    var id : Long = 0
}

