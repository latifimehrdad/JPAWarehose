package com.hoomanholding.applibrary.view.fragment.download

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import com.hoomanholding.applibrary.R
import com.hoomanholding.applibrary.databinding.FragmentDownloadBinding
import com.hoomanholding.applibrary.tools.CompanionValues
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


/**
 * create by m-latifi on 4/25/2023
 */

@AndroidEntryPoint
class DownloadFragment(override var layout: Int = R.layout.fragment_download) :
    JpaFragment<FragmentDownloadBinding>() {

    private val viewModel: DownloadViewModel by viewModels()


    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveDate()
        getFileNameFromArgument()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- observeLiveDate
    private fun observeLiveDate() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner){
            binding.textViewFailed.text = it.message
        }


        viewModel.downloadSuccessLiveData.observe(viewLifecycleOwner){
            installApp(it)
        }

        viewModel.downloadProgress.observe(viewLifecycleOwner){
            binding.progressBar.setProgressPercentage(it.toDouble(), true)
        }

    }
    //---------------------------------------------------------------------------------------------- observeLiveDate


    //---------------------------------------------------------------------------------------------- getFileNameFromArgument
    private fun getFileNameFromArgument() {
        arguments?.let { bundle ->
            val file = bundle.getString(CompanionValues.DOWNLOAD_URL, null)
            val appName = bundle.getString(CompanionValues.APP_NAME, "")
            file?.let {
                viewModel.downloadLastVersion(it, appName)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- getFileNameFromArgument


    //---------------------------------------------------------------------------------------------- installApp
    private fun installApp(file: File) {
        if (context != null) {
            val fileURI = FileProvider.getUriForFile(
                requireContext(),
                requireContext().applicationContext.packageName + ".provider",
                file
            )
            val intent = Intent(Intent.ACTION_VIEW, fileURI)
            intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
            intent.setDataAndType(fileURI, "application/vnd.android.package-archive")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            requireContext().startActivity(intent)
            requireActivity().finish()
        }
    }
    //---------------------------------------------------------------------------------------------- installApp

}