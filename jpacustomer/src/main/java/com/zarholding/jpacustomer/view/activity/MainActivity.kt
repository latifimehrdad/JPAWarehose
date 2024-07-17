package com.zarholding.jpacustomer.view.activity

import android.content.res.Configuration
import android.graphics.Typeface
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
import com.hoomanholding.applibrary.tools.CompanionValues
import com.hoomanholding.applibrary.tools.RoleManager
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.ActivityMainBinding
import com.zarholding.jpacustomer.model.EnumProductPageType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var navController: NavController? = null

    private val mainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var roleManager: RoleManager

    //---------------------------------------------------------------------------------------------- onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initView()
    }
    //---------------------------------------------------------------------------------------------- onCreate


    //---------------------------------------------------------------------------------------------- showMessage
    fun showMessage(message: String) {
        val snack = Snackbar.make(binding.constraintLayoutParent, message, 3 * 1000)
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
        binding.blurView.setupWith(binding.constraintLayoutParent)
            .setBlurRadius(10f).setBlurEnabled(false)

        mainViewModel.userInfoLiveData.observe(this) { user ->
            user?.let {
                binding.textViewUserName.setTitleAndValue(
                    it.fullName,
                    getString(R.string.customerCode2, it.personnelNumber),
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


    //---------------------------------------------------------------------------------------------- checkPermissions
    fun checkPermissions() {
        if (roleManager.isAccessToBasketMenu())
            binding.customMenuCart.visibility = View.VISIBLE
        else
            binding.customMenuCart.visibility = View.GONE

        if (roleManager.isAccessToReportMenu())
            binding.customMenuReport.visibility = View.VISIBLE
        else
            binding.customMenuReport.visibility = View.GONE
    }
    //---------------------------------------------------------------------------------------------- checkPermissions


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
            val bundle = Bundle()
            bundle.putInt(CompanionValues.Type, EnumProductPageType.Product.type)
            gotoFragment(fragment = R.id.action_goto_productFragment, bundle = bundle)
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
            R.id.downloadFragment -> {
                binding.cardViewMenu.visibility = View.GONE
                binding.customMenuHome.clearSelected()
                binding.customMenuProduct.clearSelected()
                binding.customMenuProfile.clearSelected()
                binding.customMenuReport.clearSelected()
                binding.customMenuCart.clearSelected()
            }

            R.id.homeFragment,
            R.id.subUserOrderFragment-> {
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

            R.id.videoFragment-> if (!binding.customMenuProfile.isSelectedMenu()) {
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
            R.id.customerBalanceReportFragment,
            R.id.billingReturnReportFragment,
            R.id.customerOrderReportFragment,
            R.id.customerBasketReturnReportFragment -> {
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

            R.id.criticFragment,
            R.id.aboutFragment,
            R.id.criticDetailFragment -> {
                binding.cardViewMenu.visibility = View.VISIBLE
                binding.imageViewBack.visibility = View.VISIBLE
                binding.cardViewProfile.visibility = View.GONE
                binding.textViewUserName.visibility = View.GONE
                binding.customMenuReport.clearSelected()
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
    private fun gotoFragment(fragment: Int, bundle: Bundle? = null) {
        navController?.navigate(fragment, bundle)
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


    //---------------------------------------------------------------------------------------------- enableBlurView
    fun enableBlurView() {
        binding.blurView.setBlurEnabled(true)
    }
    //---------------------------------------------------------------------------------------------- enableBlurView


    //---------------------------------------------------------------------------------------------- disableBlurView
    fun disableBlurView() {
        binding.blurView.setBlurEnabled(false)
    }
    //---------------------------------------------------------------------------------------------- disableBlurView

    
}