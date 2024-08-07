package com.hoomanholding.applibrary.model.data.request

data class AddWarehouseReceipt(
    val supplierId : Long,
    val description : String?,
    val SupplierBillingNumber : String,
    val warehouseReceiptProducts : List<WarehouseReceiptItem>
)
