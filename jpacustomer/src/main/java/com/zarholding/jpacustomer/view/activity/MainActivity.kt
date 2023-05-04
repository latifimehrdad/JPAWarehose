package com.zarholding.jpacustomer.view.activity

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
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
/*        mainViewModel.userInfoLiveData.observe(this) { user ->
            user?.let {
                binding.textViewPersonnelCode.setTitleAndValue(
                    getString(R.string.personnelNumber),
                    it.personnelNumber,
                    getString(R.string.colon)
                )
                binding.textViewUser.text = it.fullName
                binding.imageViewProfile.downloadImage(
                    it.profileImageName,
                    it.systemType,
                    EnumEntityType.ProfileImage.name,
                    mainViewModel.getBearerToke()
                )
            } ?: run {
                binding.textViewPersonnelCode.text = ""
                binding.textViewUser.text = ""
            }
        }*/
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
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- showAndHideBottomNavigationMenu
    private fun showAndHideBottomNavigationMenu(fragmentId: Int) {

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


}