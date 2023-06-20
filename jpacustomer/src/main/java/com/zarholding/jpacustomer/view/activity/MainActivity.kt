package com.zarholding.jpacustomer.view.activity

import android.Manifest
import android.content.res.Configuration
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.hoomanholding.applibrary.ext.downloadProfileImage
import com.hoomanholding.applibrary.ext.setTitleAndValue
import com.hoomanholding.applibrary.model.data.enums.EnumEntityType
import com.hoomanholding.applibrary.model.data.enums.EnumSystemType
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var navController: NavController? = null

    private val mainViewModel: MainViewModel by viewModels()

    //---------------------------------------------------------------------------------------------- onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        checkNotificationPermission()
        initView()
    }
    //---------------------------------------------------------------------------------------------- onCreate


    //---------------------------------------------------------------------------------------------- showMessage
    fun showMessage(message: String) {
        val snack = Snackbar.make(binding.constraintLayoutParent, message, 5 * 1000)
        val view = snack.view
        val textView = (view).findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        val font = Typeface.createFromAsset(assets, "font/yekan_bakh_medium.ttf")
        textView.typeface = font
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        snack.setBackgroundTint(resources.getColor(R.color.primaryColor, theme))
        snack.setTextColor(resources.getColor(R.color.a_textButton, theme))
        snack.setAction(getString(R.string.dismiss)) { snack.dismiss() }
        snack.setActionTextColor(resources.getColor(R.color.a_gradiantEnd, theme))
        snack.show()
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- initView
    private fun initView() {
        setAppTheme()
        setListener()
        hideProfileView()
//        mainViewModel.fireBaseToken()
        mainViewModel.userInfoLiveData.observe(this) { user ->
            user?.let {
                binding.textViewUserName.setTitleAndValue(
                    it.fullName,
                    it.personnelNumber,
                    getString(R.string.enterChar)
                )
                binding.imageViewProfiel.downloadProfileImage(
                    it.profileImageName,
                    EnumSystemType.Customers.name,
                    EnumEntityType.ProfileImage.name,
                    mainViewModel.getBearerToke()
                )
            } ?: run {
                binding.textViewUserName.text = ""
            }
        }
    }
    //---------------------------------------------------------------------------------------------- initView


    //______________________________________________________________________________________________ setAppTheme
    private fun setAppTheme() {
        when (mainViewModel.applicationTheme()) {
            Configuration.UI_MODE_NIGHT_YES ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

            Configuration.UI_MODE_NIGHT_NO ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
    //______________________________________________________________________________________________ setAppTheme


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        navController = navHostFragment?.navController
        navController?.addOnDestinationChangedListener { _, destination, _ ->
            showAndHideBottomNavigationMenu(destination.id)
        }

        binding.imageViewBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.customMenuHome.setOnClickListener {
            gotoHomeFragment()
        }

        binding.customMenuProduct.setOnClickListener {
            gotoFragment(R.id.action_goto_productFragment)
        }

        binding.customMenuProfile.setOnClickListener {
            gotoFragment(R.id.action_goto_profileFragment)
        }

        binding.customMenuCart.setOnClickListener {
            gotoFragment(R.id.action_goto_basketFragment)
        }

        binding.customMenuReport.setOnClickListener {
            gotoFragment(R.id.action_goto_reportFragment)
        }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- showAndHideBottomNavigationMenu
    private fun showAndHideBottomNavigationMenu(fragmentId: Int) {
        hideProfileView()
        when (fragmentId) {
            R.id.splashFragment,
            R.id.loginFragment,
            R.id.downloadFragment,
            R.id.reportPDFFragment -> {
                binding.cardViewMenu.visibility = View.GONE
                binding.customMenuHome.clearSelected()
                binding.customMenuProduct.clearSelected()
                binding.customMenuProfile.clearSelected()
                binding.customMenuReport.clearSelected()
                binding.customMenuCart.clearSelected()
            }

            R.id.homeFragment -> if (!binding.customMenuHome.isSelectedMenu()) {
                binding.cardViewMenu.visibility = View.VISIBLE
                binding.imageViewBack.visibility = View.VISIBLE
                binding.cardViewProfile.visibility = View.VISIBLE
                binding.textViewUserName.visibility = View.VISIBLE
                binding.customMenuHome.selected()
                binding.customMenuProduct.clearSelected()
                binding.customMenuProfile.clearSelected()
                binding.customMenuReport.clearSelected()
                binding.customMenuCart.clearSelected()
            }

            R.id.productFragment -> if (!binding.customMenuProduct.isSelectedMenu()) {
                binding.cardViewMenu.visibility = View.VISIBLE
                binding.imageViewBack.visibility = View.VISIBLE
                binding.cardViewProfile.visibility = View.VISIBLE
                binding.textViewUserName.visibility = View.VISIBLE
                binding.customMenuProduct.selected()
                binding.customMenuHome.clearSelected()
                binding.customMenuProfile.clearSelected()
                binding.customMenuReport.clearSelected()
                binding.customMenuCart.clearSelected()
            }

            R.id.profileFragment,
            R.id.myStateFragment -> if (!binding.customMenuProfile.isSelectedMenu()) {
                binding.cardViewMenu.visibility = View.VISIBLE
                binding.imageViewBack.visibility = View.GONE
                binding.cardViewProfile.visibility = View.GONE
                binding.textViewUserName.visibility = View.GONE
                binding.customMenuProfile.selected()
                binding.customMenuHome.clearSelected()
                binding.customMenuProduct.clearSelected()
                binding.customMenuReport.clearSelected()
                binding.customMenuCart.clearSelected()
            }

            R.id.videoFragment,
            R.id.aboutFragment -> if (!binding.customMenuProfile.isSelectedMenu()) {
                binding.cardViewMenu.visibility = View.VISIBLE
                binding.imageViewBack.visibility = View.VISIBLE
                binding.cardViewProfile.visibility = View.VISIBLE
                binding.textViewUserName.visibility = View.VISIBLE
                binding.customMenuProfile.selected()
                binding.customMenuHome.clearSelected()
                binding.customMenuProduct.clearSelected()
                binding.customMenuReport.clearSelected()
                binding.customMenuCart.clearSelected()
            }

            R.id.basketFragment -> if (!binding.customMenuCart.isSelectedMenu()) {
                binding.cardViewMenu.visibility = View.VISIBLE
                binding.imageViewBack.visibility = View.VISIBLE
                binding.cardViewProfile.visibility = View.VISIBLE
                binding.textViewUserName.visibility = View.VISIBLE
                binding.customMenuCart.selected()
                binding.customMenuHome.clearSelected()
                binding.customMenuProduct.clearSelected()
                binding.customMenuProfile.clearSelected()
                binding.customMenuReport.clearSelected()
            }

            R.id.reportFragment,
            R.id.billingReturnReportFragment -> if (!binding.customMenuReport.isSelectedMenu()) {
                binding.cardViewMenu.visibility = View.VISIBLE
                binding.imageViewBack.visibility = View.VISIBLE
                binding.cardViewProfile.visibility = View.VISIBLE
                binding.textViewUserName.visibility = View.VISIBLE
                binding.customMenuReport.selected()
                binding.customMenuHome.clearSelected()
                binding.customMenuProduct.clearSelected()
                binding.customMenuProfile.clearSelected()
                binding.customMenuCart.clearSelected()
            }

            R.id.customerBalanceReportFragment -> if (!binding.customMenuReport.isSelectedMenu()) {
                binding.cardViewMenu.visibility = View.VISIBLE
                binding.customMenuReport.selected()
                binding.customMenuHome.clearSelected()
                binding.customMenuProduct.clearSelected()
                binding.customMenuProfile.clearSelected()
                binding.customMenuCart.clearSelected()
            }
        }
    }
    //---------------------------------------------------------------------------------------------- showAndHideBottomNavigationMenu


    //---------------------------------------------------------------------------------------------- gotoFirstFragment
    fun gotoFirstFragment(deleteUser: Boolean = true) {
        if (deleteUser)
            deleteAllData()
        CoroutineScope(Dispatchers.IO).launch {
            delay(500)
            withContext(Dispatchers.Main) {
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
        binding.constraintLayoutParent.alpha = 0.1f
    }
    //---------------------------------------------------------------------------------------------- hideFragmentContainer


    //---------------------------------------------------------------------------------------------- showFragmentContainer
    fun showFragmentContainer() {
        binding.constraintLayoutParent.alpha = 1f
    }
    //---------------------------------------------------------------------------------------------- showFragmentContainer


    //---------------------------------------------------------------------------------------------- hideProfileView
    private fun hideProfileView() {
        binding.imageViewBack.visibility = View.GONE
        binding.cardViewProfile.visibility = View.GONE
        binding.textViewUserName.visibility = View.GONE
    }
    //---------------------------------------------------------------------------------------------- hideProfileView


    //---------------------------------------------------------------------------------------------- getCartView
    fun getCartView() = binding.customMenuCart
    //---------------------------------------------------------------------------------------------- getCartView


    //---------------------------------------------------------------------------------------------- setCartBadge
    fun setCartBadge(count: Int) {
        binding.customMenuCart.setBadgeCount(count)
    }
    //---------------------------------------------------------------------------------------------- setCartBadge


    //---------------------------------------------------------------------------------------------- gotoHomeFragment
    fun gotoHomeFragment() {
        gotoFragment(R.id.action_goto_homeFragment)
    }
    //---------------------------------------------------------------------------------------------- gotoHomeFragment


    //---------------------------------------------------------------------------------------------- checkNotificationPermission
    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = mutableListOf(Manifest.permission.POST_NOTIFICATIONS)
            Dexter.withContext(this)
                .withPermissions(permission)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {

                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?
                    ) {

                    }
                })
                .check()
        }
    }
    //---------------------------------------------------------------------------------------------- checkNotificationPermission

}