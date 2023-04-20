package com.hoomanholding.applibrary.model.data.response.order

import android.os.Parcel
import android.os.Parcelable


/**
 * create by m-latifi on 3/18/2023
 */

data class OrderModel(
    val id: Int,
    val orderDate: String?,
    val orderNumber: Int,
    val customerId: Int,
    val customerCode: Int,
    val customerName: String?,
    val visitorId: Int?,
    val visitorName: String?,
    val paymentTypeId: Int,
    val paymentTypeName: String?,
    val paymentDate: Short,
    val orderAmount: Long,
    val discount: Long,
    val orderFinaleAmount: Long,
    val visitorDescription: String?,
    val saleOrderState: Int,
    val saleOrderStateText: String?,
    var select : Boolean = false
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt().toShort(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(orderDate)
        parcel.writeInt(orderNumber)
        parcel.writeInt(customerId)
        parcel.writeInt(customerCode)
        parcel.writeString(customerName)
        parcel.writeValue(visitorId)
        parcel.writeString(visitorName)
        parcel.writeInt(paymentTypeId)
        parcel.writeString(paymentTypeName)
        parcel.writeInt(paymentDate.toInt())
        parcel.writeLong(orderAmount)
        parcel.writeLong(discount)
        parcel.writeLong(orderFinaleAmount)
        parcel.writeString(visitorDescription)
        parcel.writeInt(saleOrderState)
        parcel.writeString(saleOrderStateText)
        parcel.writeByte(if (select) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderModel> {
        override fun createFromParcel(parcel: Parcel): OrderModel {
            return OrderModel(parcel)
        }

        override fun newArray(size: Int): Array<OrderModel?> {
            return arrayOfNulls(size)
        }
    }
}
