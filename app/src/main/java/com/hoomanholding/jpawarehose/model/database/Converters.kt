package com.hoomanholding.jpawarehose.model.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hoomanholding.jpawarehose.model.database.entity.SupplierEntity
import com.hoomanholding.jpawarehose.model.database.join.ProductWithBrandModel
import java.lang.reflect.Type
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

class Converters {

    //---------------------------------------------------------------------------------------------- fromTimestamp
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime?{
        return value?.let { LocalDateTime.ofInstant(Instant.ofEpochMilli(it), TimeZone.getDefault().toZoneId()) }
    }
    //---------------------------------------------------------------------------------------------- fromTimestamp


    //---------------------------------------------------------------------------------------------- dateToTimestamp
    @TypeConverter
    fun dateToTimestamp(localDateTime: LocalDateTime?) : Long? {
        return localDateTime?.atZone(TimeZone.getDefault().toZoneId())?.toEpochSecond()
    }
    //---------------------------------------------------------------------------------------------- dateToTimestamp


    //---------------------------------------------------------------------------------------------- fromString
    @TypeConverter
    fun fromString(value: String?): List<Long>? {
        val listType: Type = object : TypeToken<List<Long>?>() {}.type
        return Gson().fromJson(value, listType)
    }
    //---------------------------------------------------------------------------------------------- fromString


    //---------------------------------------------------------------------------------------------- fromString
    @TypeConverter
    fun fromStringProduct(value: String?): ProductWithBrandModel? {
        val product: Type = object : TypeToken<ProductWithBrandModel?>() {}.type
        return Gson().fromJson(value, product)
    }
    //---------------------------------------------------------------------------------------------- fromString


    //---------------------------------------------------------------------------------------------- fromList
    @TypeConverter
    fun fromProduct(product: ProductWithBrandModel?): String? {
        val gson = Gson()
        return gson.toJson(product)
    }
    //---------------------------------------------------------------------------------------------- fromList


    //---------------------------------------------------------------------------------------------- fromList
    @TypeConverter
    fun fromList(list: List<Long>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
    //---------------------------------------------------------------------------------------------- fromList


    //---------------------------------------------------------------------------------------------- fromStringSupplier
    @TypeConverter
    fun fromStringSupplier(value: String?): SupplierEntity? {
        val supplier: Type = object : TypeToken<SupplierEntity?>() {}.type
        return Gson().fromJson(value, supplier)
    }
    //---------------------------------------------------------------------------------------------- fromStringSupplier


    //---------------------------------------------------------------------------------------------- fromSupplier
    @TypeConverter
    fun fromSupplier(supplier: SupplierEntity?): String? {
        val gson = Gson()
        return gson.toJson(supplier)
    }
    //---------------------------------------------------------------------------------------------- fromSupplier

}