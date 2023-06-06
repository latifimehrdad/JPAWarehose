package com.hoomanholding.applibrary.view.fragment

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

/**
 * Created by m-latifi on 10/8/2022.
 */

abstract class JpaFragment<DB : ViewDataBinding> : Fragment() {

    abstract var layout : Int
    protected lateinit var binding : DB


    //---------------------------------------------------------------------------------------------- onCreateView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layout, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
    //---------------------------------------------------------------------------------------------- onCreateView


    //---------------------------------------------------------------------------------------------- parcelable
    inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
        Build.VERSION.SDK_INT >= 33 -> getParcelable(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelable(key) as? T
    }
    //---------------------------------------------------------------------------------------------- parcelable


    //---------------------------------------------------------------------------------------------- gotoFragment
    fun gotoFragment(fragment: Int, bundle: Bundle? = null) {
        try {
            findNavController().navigate(fragment, bundle)
        }catch (_: Exception) { }
    }
    //---------------------------------------------------------------------------------------------- gotoFragment

}