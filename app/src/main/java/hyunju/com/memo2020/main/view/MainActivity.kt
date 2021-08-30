package hyunju.com.memo2020.main.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import hyunju.com.memo2020.R
import hyunju.com.memo2020.databinding.ActivityMainBinding
import java.util.ArrayList


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setLayout()
        checkPermission()
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.main_fragment).navigateUp()
    }

    private fun setLayout() {
        // sync toolbar and navigation
        setSupportActionBar(findViewById<View>(R.id.main_toolbar) as Toolbar)
        val navController = Navigation.findNavController(this, R.id.main_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)

    }

    private fun checkPermission() {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {

            }

            override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
                finish()
            }
        }

        TedPermission.with(this)
            .setPermissionListener(permissionListener)
            .setRationaleMessage(getString(R.string.permission_rationale_msg))
            .setDeniedMessage(getString(R.string.permission_denied_msg))
            .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .check()

    }

}