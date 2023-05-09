package com.zarholding.jpacustomer.view.activity

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
import com.hoomanholding.applibrary.ext.downloadProfileImage
import com.hoomanholding.applibrary.ext.setTitleAndValue
import com.hoomanholding.applibrary.model.data.enums.EnumEntityType
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
        snack.setTextColor(resources.getColor(R.color.bottomTextColor, theme))
        snack.setAction(getString(R.string.dismiss)) { snack.dismiss() }
        snack.setActionTextColor(resources.getColor(R.color.primaryGradientEnd, theme))
        snack.show()
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- initView
    private fun initView() {
        setListener()
        resetMenuColor()
        mainViewModel.userInfoLiveData.observe(this) { user ->
            user?.let {
                binding.textViewUserName.setTitleAndValue(
                    it.fullName,
                    it.personnelNumber,
                    getString(R.string.enterChar)
                )
                binding.imageViewProfiel.downloadProfileImage(
                    it.profileImageName,
                    it.systemType,
                    EnumEntityType.ProfileImage.name,
                    mainViewModel.getBearerToke()
                )
            } ?: run {
                binding.textViewUserName.text = ""
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

        binding.customMenuHome.setOnClickListener {
            gotoFragment(R.id.action_goto_homeFragment)
        }

        binding.customMenuProduct.setOnClickListener {
            gotoFragment(R.id.action_goto_productFragment)
        }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- showAndHideBottomNavigationMenu
    private fun showAndHideBottomNavigationMenu(fragmentId: Int) {
        resetMenuColor()
        when (fragmentId) {
            R.id.splashFragment,
            R.id.loginFragment,
            R.id.downloadFragment -> {
                binding.cardViewMenu.visibility = View.GONE
            }
            R.id.homeFragment ->
                if (!binding.customMenuHome.isSelectedMenu()) {
                    resetMenuColor()
                    binding.cardViewMenu.visibility = View.VISIBLE
                    binding.imageViewBack.visibility = View.VISIBLE
                    binding.cardViewProfile.visibility = View.VISIBLE
                    binding.customMenuHome.selected()
                }
            R.id.productFragment -> if (!binding.customMenuProduct.isSelectedMenu()){
                resetMenuColor()
                binding.cardViewMenu.visibility = View.VISIBLE
                binding.imageViewBack.visibility = View.VISIBLE
                binding.cardViewProfile.visibility = View.VISIBLE
                binding.customMenuProduct.selected()
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
        binding.navHostFragment.alpha = 0.2f
    }
    //---------------------------------------------------------------------------------------------- hideFragmentContainer


    //---------------------------------------------------------------------------------------------- showFragmentContainer
    fun showFragmentContainer() {
        binding.navHostFragment.alpha = 1f
    }
    //---------------------------------------------------------------------------------------------- showFragmentContainer


    //---------------------------------------------------------------------------------------------- resetMenuColor
    private fun resetMenuColor() {
        binding.imageViewBack.visibility = View.GONE
        binding.cardViewProfile.visibility = View.GONE
        binding.customMenuHome.clearSelected()
        binding.customMenuProduct.clearSelected()
        binding.customMenuProfile.clearSelected()
        binding.customMenuReport.clearSelected()
    }
    //---------------------------------------------------------------------------------------------- resetMenuColor


}