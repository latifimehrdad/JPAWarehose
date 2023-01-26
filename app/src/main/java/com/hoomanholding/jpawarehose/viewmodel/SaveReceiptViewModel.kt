package com.hoomanholding.jpawarehose.viewmodel

import com.hoomanholding.jpawarehose.model.repository.BrandRepository
import com.hoomanholding.jpawarehose.model.repository.SupplierRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Create by Mehrdad on 1/26/2023
 */

@HiltViewModel
class SaveReceiptViewModel @Inject constructor(
    private val supplierRepository: SupplierRepository,
    private val brandRepository: BrandRepository
) : JpaViewModel(){


    //---------------------------------------------------------------------------------------------- getSuppliers
    fun getSuppliers() = supplierRepository.getSupplierFromDB()
    //---------------------------------------------------------------------------------------------- getSuppliers


    //---------------------------------------------------------------------------------------------- getBrands
    fun getBrands() = brandRepository.getBrands()
    //---------------------------------------------------------------------------------------------- getBrands

}