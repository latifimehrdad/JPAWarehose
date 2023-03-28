package com.hoomanholding.jpamanager.model.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Role")
data class RoleEntity(
    @PrimaryKey val permission : String
)
