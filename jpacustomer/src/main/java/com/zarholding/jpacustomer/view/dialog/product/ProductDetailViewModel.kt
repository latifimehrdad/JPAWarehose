package com.zarholding.jpacustomer.view.dialog.product

import com.hoomanholding.applibrary.model.repository.TokenRepository
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by m-latifi on 6/4/2023.
 */

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val tokenRepository: TokenRepository
): JpaViewModel() {

    //---------------------------------------------------------------------------------------------- getBearerToken
    fun getBearerToken() = tokenRepository.getBearerToken()
    //---------------------------------------------------------------------------------------------- getBearerToken


}