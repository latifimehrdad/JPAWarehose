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
import com.hoomanholding.applibrary.ext.setTitleAndValue
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
        mainViewModel.userInfoLiveData.observe(this) { user ->
            user?.let {
                binding.textViewPersonnelCode.setTitleAndValue(
                    getString(R.string.personnelNumber),
                    it.personnelNumber,
                    getString(R.string.colon)
                )
                binding.textViewUser.text = it.fullName
            } ?: run {
                binding.textViewPersonnelCode.text = ""
                binding.textViewUser.text = ""
            }
        }
    }
    //---------------------------------------------------------------------------------------------- initView


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        navController = navHostFragment?.navController
        navController?.addOnDestinationChangedListener { _, destination, _ ->
            showAndHideBottomNavigationMenu(destination.id)
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

        binding.imageViewBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- showAndHideBottomNavigationMenu
    private fun showAndHideBottomNavigationMenu(fragmentId: Int) {
        resetMenuColor()
        when (fragmentId) {
            R.id.splashFragment,
            R.id.loginFragment -> {
                binding.menuHome.visibility = View.GONE
                binding.menuCardboard.visibility = View.GONE
                binding.menuReport.visibility = View.GONE
                binding.menuProfile.visibility = View.GONE
                binding.textViewUser.visibility = View.GONE
                binding.imageViewBack.visibility = View.GONE
            }
            R.id.homeFragment -> {
                if (!binding.menuHome.isSelectedMenu()) {
                    resetMenuColor()
                    binding.imageViewBack.visibility = View.GONE
                    binding.menuHome.selected()
                }
            }
            R.id.reportFragment -> {
                if (!binding.menuReport.isSelectedMenu()) {
                    resetMenuColor()
                    binding.menuReport.selected()
                }
            }
            R.id.profileFragment -> {
                if (!binding.menuProfile.isSelectedMenu()) {
                    resetMenuColor()
                    binding.menuProfile.selected()
                }
            }
            R.id.cardBoardFragment,
            R.id.invoiceFragment,
            R.id.invoiceFragmentDetail,
            R.id.customerFinancialFragment -> {
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
        binding.imageViewBack.visibility = View.VISIBLE
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
        val font = Typeface.createFromAsset(assets, "font/yekan_bakh_medium.ttf")
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


    //---------------------------------------------------------------------------------------------- hideFragmentContainer
    fun hideFragmentContainer() {
        binding.navHostFragment.alpha = 0.2f
    }
    //---------------------------------------------------------------------------------------------- hideFragmentContainer


    //---------------------------------------------------------------------------------------------- showFragmentContainer
    fun showFragmentContainer() {
        binding.navHostFragment.alpha = 1f
    }
    //---------------------------------------------------------------------------------------------- showFragmentContainer

}