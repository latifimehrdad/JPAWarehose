package com.hoomanholding.jpamanager.view.activity

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private var navController: NavController? = null

    private val mainViewModel: MainViewModel by viewModels()

    //---------------------------------------------------------------------------------------------- onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initView()
    }
    //---------------------------------------------------------------------------------------------- onCreate


    //---------------------------------------------------------------------------------------------- initView
    private fun initView() {
        setListener()
        mainViewModel.userInfoLiveData.observe(this) {
            binding.textViewUser.text = it
        }
    }
    //---------------------------------------------------------------------------------------------- initView


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        navController = navHostFragment?.navController
        navController?.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.label != null)
                showAndHideBottomNavigationMenu(destination.label.toString())
        }

        binding.menuHome.setOnClickListener {
            gotoFragment(R.id.action_goto_HomeFragment)
        }

        binding.menuCardboard.setOnClickListener {
            gotoFragment(R.id.action_goto_CardBoardFragment)
        }

        binding.menuReport.setOnClickListener {
            gotoFragment(R.id.action_goto_ReportFragment)
        }

        binding.menuProfile.setOnClickListener {
            gotoFragment(R.id.action_goto_ProfileFragment)
        }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- showAndHideBottomNavigationMenu
    private fun showAndHideBottomNavigationMenu(fragmentLabel: String) {
        resetMenuColor()
        when (fragmentLabel) {
            "SplashFragment",
            "LoginFragment" -> {
                binding.menuHome.visibility = View.GONE
                binding.menuCardboard.visibility = View.GONE
                binding.menuReport.visibility = View.GONE
                binding.menuProfile.visibility = View.GONE
                binding.textViewUser.visibility = View.GONE
                binding.textViewLogOut.visibility = View.GONE
            }
            "HomeFragment" -> {
                if (!binding.menuHome.isSelectedMenu()) {
                    resetMenuColor()
                    binding.menuHome.selected()
                }
            }
            "ReportFragment" -> {
                if (!binding.menuReport.isSelectedMenu()) {
                    resetMenuColor()
                    binding.menuReport.selected()
                }
            }
            "ProfileFragment" -> {
                if (!binding.menuProfile.isSelectedMenu()) {
                    resetMenuColor()
                    binding.menuProfile.selected()
                }
            }
            "CardBoardFragment",
            "InvoiceFragment" -> {
                if (!binding.menuCardboard.isSelectedMenu()) {
                    resetMenuColor()
                    binding.menuCardboard.selected()
                }
            }
        }
    }
    //---------------------------------------------------------------------------------------------- showAndHideBottomNavigationMenu


    //---------------------------------------------------------------------------------------------- resetMenuColor
    private fun resetMenuColor() {
        binding.textViewUser.visibility = View.VISIBLE
        binding.textViewLogOut.visibility = View.VISIBLE
        binding.menuHome.visibility = View.VISIBLE
        binding.menuCardboard.visibility = View.VISIBLE
        binding.menuReport.visibility = View.VISIBLE
        binding.menuProfile.visibility = View.VISIBLE
        binding.menuHome.clearSelected()
        binding.menuCardboard.clearSelected()
        binding.menuReport.clearSelected()
        binding.menuProfile.clearSelected()
    }
    //---------------------------------------------------------------------------------------------- resetMenuColor


    //---------------------------------------------------------------------------------------------- showMessage
    fun showMessage(message: String) {
        val snack = Snackbar.make(binding.constraintLayoutParent, message, 5 * 1000)
        val view = snack.view
        val textView = (view).findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        val font = Typeface.createFromAsset(assets, "font/iransans_medium.ttf")
        textView.typeface = font
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        snack.setBackgroundTint(resources.getColor(R.color.snackBack, theme))
        snack.setTextColor(resources.getColor(R.color.primaryColorVariant, theme))
        snack.setAction(getString(R.string.dismiss)) { snack.dismiss() }
        snack.setActionTextColor(resources.getColor(R.color.primaryColorVariant, theme))
        snack.show()
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- gotoFirstFragment
    fun gotoFirstFragment() {
        deleteAllData()
        CoroutineScope(IO).launch {
            delay(500)
            withContext(Main) {
                gotoFragment(R.id.action_goto_SplashFragment)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- gotoFirstFragment


    //---------------------------------------------------------------------------------------------- deleteAllData
    fun deleteAllData() {
        mainViewModel.deleteAllData()
    }
    //---------------------------------------------------------------------------------------------- deleteAllData


    //---------------------------------------------------------------------------------------------- gotoFragment
    private fun gotoFragment(fragment: Int) {
        navController?.navigate(fragment, null)
    }
    //---------------------------------------------------------------------------------------------- gotoFragment


    //---------------------------------------------------------------------------------------------- getUserInfo
    fun getUserInfo() {
        mainViewModel.getUserInfo()
    }
    //---------------------------------------------------------------------------------------------- getUserInfo

}