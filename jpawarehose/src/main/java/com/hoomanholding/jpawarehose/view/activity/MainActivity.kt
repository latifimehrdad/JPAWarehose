package com.hoomanholding.jpawarehose.view.activity

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.hoomanholding.jpawarehose.R
import com.hoomanholding.jpawarehose.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
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
        showImageViewShelf()
        setListener()
    }
    //---------------------------------------------------------------------------------------------- initView


    //---------------------------------------------------------------------------------------------- showImageViewShelf
    fun showImageViewShelf() {
        mainViewModel.setActionImageViewShelf()
        if (mainViewModel.actionImageViewShelf == 0)
            binding.imageViewShelf.visibility = View.GONE
        else
            binding.imageViewShelf.visibility = View.VISIBLE
    }
    //---------------------------------------------------------------------------------------------- showImageViewShelf


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        navController = navHostFragment?.navController
        navController?.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.label != null)
                showAndHideBottomNavigationMenu(destination.label.toString())
        }

        binding.imageViewUpdate.setOnClickListener {
            navController?.navigate(R.id.action_goto_UpdateFragment)
        }

        binding.imageViewHome.setOnClickListener {
            navController?.navigate(R.id.action_goto_HomeFragment)
        }

        binding.imageViewHistory.setOnClickListener {
            navController?.navigate(R.id.action_goto_HistorySaveReceiptFragment)
        }

        binding.imageViewShelf.setOnClickListener { clickOnImageShelf() }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- clickOnImageShelf
    private fun clickOnImageShelf() {
        navController?.navigate(mainViewModel.actionImageViewShelf)
    }
    //---------------------------------------------------------------------------------------------- clickOnImageShelf


    //---------------------------------------------------------------------------------------------- showAndHideBottomNavigationMenu
    private fun showAndHideBottomNavigationMenu(fragmentLabel: String) {
        resetMenuColor()
        when (fragmentLabel) {
            "SplashFragment",
            "LoginFragment" -> {
                binding.constraintLayoutFooterMenu.visibility = View.GONE
            }
            "UpdateFragment" -> selectCurrentMenu(binding.imageViewUpdate)
            "HomeFragment" -> selectCurrentMenu(binding.imageViewHome)
            "HistorySaveReceiptFragment" -> selectCurrentMenu(binding.imageViewHistory)
            "SaveReceiptFragment",
            "ArrangeFragment" -> selectCurrentMenu(binding.imageViewShelf)
        }
    }
    //---------------------------------------------------------------------------------------------- showAndHideBottomNavigationMenu


    //---------------------------------------------------------------------------------------------- resetMenuColor
    private fun resetMenuColor() {
        resetImageNavigationMenu(binding.imageViewHistory)
        resetImageNavigationMenu(binding.imageViewUpdate)
        resetImageNavigationMenu(binding.imageViewHome)
        resetImageNavigationMenu(binding.imageViewShelf)
    }
    //---------------------------------------------------------------------------------------------- resetMenuColor


    //---------------------------------------------------------------------------------------------- resetImageNavigationMenu
    private fun resetImageNavigationMenu(imageIcon: ImageView) {
        imageIcon.setColorFilter(
            ContextCompat.getColor(this, R.color.white),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
        imageIcon.scaleX = 0.75f
        imageIcon.scaleY = 0.75f
    }
    //---------------------------------------------------------------------------------------------- resetImageNavigationMenu


    //---------------------------------------------------------------------------------------------- selectCurrentMenu
    private fun selectCurrentMenu(imageIcon: ImageView) {
        binding.constraintLayoutFooterMenu.visibility = View.VISIBLE
        imageIcon.setColorFilter(
            ContextCompat.getColor(this, R.color.primaryColor),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
        imageIcon.scaleX = 1.2f
        imageIcon.scaleY = 1.2f
    }
    //---------------------------------------------------------------------------------------------- selectCurrentMenu


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

}