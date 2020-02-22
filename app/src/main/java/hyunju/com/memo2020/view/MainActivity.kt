package hyunju.com.memo2020.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import hyunju.com.memo2020.R
import hyunju.com.memo2020.databinding.ActivityMainBinding
import hyunju.com.memo2020.viewmodel.MainAcitivityViewmodel

class MainActivity : AppCompatActivity() {

    protected lateinit var binding: ActivityMainBinding
    protected val mainViewModel: MainAcitivityViewmodel by lazy {
        ViewModelProvider(this).get(MainAcitivityViewmodel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(findViewById<View>(R.id.main_toolbar) as Toolbar)
        val navController = Navigation.findNavController(this, R.id.main_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)


    }

    override fun onSupportNavigateUp(): Boolean {
        val currentFrag = Navigation.findNavController(this, R.id.main_fragment).getCurrentDestination()?.getId()
        Log.d("testNav", "onSupportNavigateUp currentFrag = " + currentFrag)
        if (currentFrag == R.id.itemFragment) {
            Log.d("testNav", "onSupportNavigateUp detailFragment")

        } else {
            Log.d("testNav", "onSupportNavigateUp no detailFragment")

        }

        return Navigation.findNavController(this, R.id.main_fragment).navigateUp()
    }

    override fun onBackPressed() {
        if (!Navigation.findNavController(this, R.id.main_fragment).popBackStack()) {
            // Call finish() on your Activity
            finish()
        } else {
            Log.d("testNav", "onBackPressed no detailFragment")
            Navigation.findNavController(this, R.id.main_fragment).navigateUp()
        }

    }


}