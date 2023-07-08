package com.zarholding.jpacustomer.view.fragment.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Size
import android.view.View
import androidx.fragment.app.viewModels
import com.hoomanholding.applibrary.ext.config
import com.hoomanholding.applibrary.ext.setTitleAndValue
import com.hoomanholding.applibrary.ext.startLoading
import com.hoomanholding.applibrary.ext.stopLoading
import com.hoomanholding.applibrary.model.data.response.about.AboutModel
import com.hoomanholding.applibrary.tools.getShimmerBuild
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.zar.core.enums.EnumApiError
import com.zar.core.tools.manager.ThemeManager
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.FragmentAboutBinding
import com.zarholding.jpacustomer.tools.OsmManager
import com.zarholding.jpacustomer.view.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.util.GeoPoint
import java.util.Locale
import javax.inject.Inject


/**
 * Created by m-latifi on 6/6/2023.
 */

@AndroidEntryPoint
class AboutFragment(
    override var layout: Int = R.layout.fragment_about
): JpaFragment<FragmentAboutBinding>() {

    private val viewModel: AboutViewModel by viewModels()
    private lateinit var osmManager: OsmManager
    @Inject lateinit var themeManagers: ThemeManager

    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated



    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
        activity?.let { (it as MainActivity).showMessage(message) }
    }
    //---------------------------------------------------------------------------------------------- showMessage



    //---------------------------------------------------------------------------------------------- initView
    private fun initView() {
        binding.shimmerViewContainer.config(getShimmerBuild())
        observeLiveDate()
        setListener()
        osmManager = OsmManager(binding.mapView)
        osmManager.mapInitialize(themeManagers.applicationTheme())
        getAbout()
    }
    //---------------------------------------------------------------------------------------------- initView



    //---------------------------------------------------------------------------------------------- observeLiveDate
    private fun observeLiveDate() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            showMessage(it.message)
            when (it.type) {
                EnumApiError.UnAuthorization -> (activity as MainActivity?)?.gotoFirstFragment()
                else -> {}
            }
        }


        viewModel.aboutLiveData.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            setValueToXml(it)
        }

    }
    //---------------------------------------------------------------------------------------------- observeLiveDate



    //---------------------------------------------------------------------------------------------- getAbout
    private fun getAbout() {
        binding.cardViewMap.visibility = View.INVISIBLE
        binding.cardViewVideo.visibility = View.INVISIBLE
        binding.cardViewProfileHeader.visibility = View.INVISIBLE
        binding.shimmerViewContainer.startLoading()
        viewModel.getAbout()
    }
    //---------------------------------------------------------------------------------------------- getAbout



    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.imageviewAddress.setOnClickListener { openRouterApp() }
        binding.textViewAddress.setOnClickListener { openRouterApp() }
        binding.imageviewCall.setOnClickListener { openCall() }
        binding.textViewCall.setOnClickListener { openCall() }
        binding.cardViewVideo.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.parse(viewModel.getVideoLink()), "video/mp4")
            startActivity(intent)
        }
    }
    //---------------------------------------------------------------------------------------------- setListener



    //---------------------------------------------------------------------------------------------- setValueToXml
    private fun setValueToXml(item: AboutModel) {
        binding.cardViewMap.visibility = View.VISIBLE
        binding.cardViewVideo.visibility = View.VISIBLE
        binding.cardViewProfileHeader.visibility = View.VISIBLE
        binding.textViewCompanyName.text = item.companyName
        binding.textViewAbout.text = item.aboutUs
        binding.textViewCall.setTitleAndValue(
            title = getString(R.string.phoneNumber),
            splitter = getString(R.string.colon),
            value = item.telephone
        )
        binding.textViewAddress.setTitleAndValue(
            title = getString(R.string.address),
            splitter = getString(R.string.colon),
            value = item.address
        )
        val iconMarker = osmManager.createMarkerIconDrawable(
            Size(80,80), R.drawable.icon_marker)
        val marker = osmManager.addMarker(
            iconMarker,
            GeoPoint(item.lat, item.long),
            null
        )
        marker.setOnMarkerClickListener { _, _ ->
            openRouterApp()
            false
        }
    }
    //---------------------------------------------------------------------------------------------- setValueToXml


    //---------------------------------------------------------------------------------------------- openRouterApp
    private fun openRouterApp() {
        val item = viewModel.aboutLiveData.value ?: return
        val intent = Intent(
            Intent.ACTION_VIEW, Uri.parse(
                java.lang.String.format(
                    Locale.US,
                    "geo:%.8f,%.8f", item.lat, item.long
                )
            )
        )
        startActivity(Intent.createChooser(intent, "یک برنامه مسیریاب انتخاب کنید"))
    }
    //---------------------------------------------------------------------------------------------- openRouterApp



    //---------------------------------------------------------------------------------------------- openCall
    private fun openCall() {
        val tel = viewModel.aboutLiveData.value?.telephone?.replace("[^0-9]".toRegex(), "")
        if (tel.isNullOrEmpty())
            return
        val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", tel, null))
        startActivity(intent)
    }
    //---------------------------------------------------------------------------------------------- openCall

}