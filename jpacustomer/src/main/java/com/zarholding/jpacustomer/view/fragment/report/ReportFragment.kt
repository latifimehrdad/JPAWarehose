package com.zarholding.jpacustomer.view.fragment.report

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.FragmentReportBinding
import com.zarholding.jpacustomer.view.adapter.recycler.ReportItemAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by m-latifi on 6/7/2023.
 */

@AndroidEntryPoint
class ReportFragment(
    override var layout: Int = R.layout.fragment_report
): JpaFragment<FragmentReportBinding>() {

    private val viewModel: ReportViewModel by viewModels()

    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (context != null) {
            val adapter = ReportItemAdapter(items = viewModel.getReport()) {
                gotoFragment(fragment = it.action, bundle = it.bundle)
            }
            val manager = GridLayoutManager(
                requireContext(),
                3,
                GridLayoutManager.VERTICAL,
                false
            )
            binding.recyclerApp.layoutDirection = View.LAYOUT_DIRECTION_RTL
            binding.recyclerApp.layoutManager = manager
            binding.recyclerApp.adapter = adapter
        }
    }
    //---------------------------------------------------------------------------------------------- onViewCreated

}