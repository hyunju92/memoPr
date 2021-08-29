package hyunju.com.memo2020.main.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import hyunju.com.memo2020.R
import hyunju.com.memo2020.databinding.ActivityMainBinding
import hyunju.com.memo2020.main.vm.MainAcitivityViewmodel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainAcitivityViewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainViewModel = MainAcitivityViewmodel()

        // sync toolbar and navigation
        setSupportActionBar(findViewById<View>(R.id.main_toolbar) as Toolbar)
        val navController = Navigation.findNavController(this, R.id.main_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)

        // permission check
        mainViewModel.checkPermission(this, this)
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.main_fragment).navigateUp()
    }


}