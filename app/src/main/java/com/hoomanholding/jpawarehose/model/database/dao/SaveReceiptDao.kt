package com.hoomanholding.jpawarehose.model.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hoomanholding.jpawarehose.model.database.entity.SupplierEntity

/**
 * Create by Mehrdad on 1/18/2023
 */

@Dao
interface SaveReceiptDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSaveReceipts(supplies: List<SupplierEntity>)

}