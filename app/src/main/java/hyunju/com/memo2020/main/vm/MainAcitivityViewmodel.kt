package hyunju.com.memo2020.main.vm

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import hyunju.com.memo2020.R
import java.util.*


class MainAcitivityViewmodel(application: Application) : AndroidViewModel(application) {

    // permission check and granted
    // * libarary - TedPermission
    fun checkPermission(context: Context, activity: Activity) {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {

            }

            override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
                activity.finish()
            }
        }

        TedPermission.with(context)
            .setPermissionListener(permissionListener)
            .setRationaleMessage(context.getString(R.string.permission_rationale_msg))
            .setDeniedMessage(context.getString(R.string.permission_denied_msg))
            .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .check()


    }
}


