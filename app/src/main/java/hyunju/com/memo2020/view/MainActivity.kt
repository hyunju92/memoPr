package hyunju.com.memo2020.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import hyunju.com.memo2020.R
import hyunju.com.memo2020.databinding.ActivityMainBinding
import hyunju.com.memo2020.viewmodel.ItemFragmentViewmodel
import hyunju.com.memo2020.viewmodel.MainAcitivityViewmodel


class MainActivity : AppCompatActivity() {

    protected lateinit var binding: ActivityMainBinding
    protected val mainViewModel: MainAcitivityViewmodel by lazy {
        ViewModelProvider(this).get(MainAcitivityViewmodel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

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

    override fun onBackPressed() {
        if (!Navigation.findNavController(this, R.id.main_fragment).popBackStack()) {
            // finish when not exist back stack
            finish()

        } else {
            Navigation.findNavController(this, R.id.main_fragment).navigateUp()

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // when result was from startactivityforresult of ItemFragment (to get image)
        // send back it to ItemFragment
        val currentFrag = Navigation.findNavController(this, R.id.main_fragment).getCurrentDestination()?.getId()

        if (currentFrag == R.id.itemFragment) {
            val itemFragViemodel: ItemFragmentViewmodel =
                    ViewModelProvider(this).get(ItemFragmentViewmodel::class.java)

            itemFragViemodel.onActivityResult(requestCode, resultCode, data)

        }
    }


}