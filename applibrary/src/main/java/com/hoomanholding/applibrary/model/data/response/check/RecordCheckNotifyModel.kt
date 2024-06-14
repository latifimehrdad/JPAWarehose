package com.hoomanholding.applibrary.model.data.response.check

data class RecordCheckNotifyModel(
    val id: Int,
    val isForceExit: Boolean,
    val message: String,
    val checkList: List<RecordCheckModel>
)
