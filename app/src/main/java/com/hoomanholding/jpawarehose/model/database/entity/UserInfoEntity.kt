package com.hoomanholding.jpawarehose.model.database.entity

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
    val rolesId : List<Int>?,
) {
    @PrimaryKey(autoGenerate = false)
    var id : Int = 0
}

