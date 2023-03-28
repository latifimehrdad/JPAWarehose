package com.hoomanholding.jpamanager.model.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hoomanholding.jpamanager.model.data.database.entity.RoleEntity

@Dao
interface RoleDao {

    @Query("DELETE FROM Role")
    fun deleteAllRecord()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(roleEntities: List<RoleEntity>)

    @Query("SELECT * FROM Role WHERE permission = :permission LIMIT 1")
    fun getPermission(permission : String) : RoleEntity?

}