package hyunju.com.memo2020.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
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
                .setRationaleMessage("메모 기능 사용을 위해 권한이 필요합니다.")
                .setDeniedMessage("권한을 허용하지 않아 앱을 실행할 수 없습니다. \n[설정]-[권한]에서 권한을 다시 허용할 수 있습니다.")
                .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check()
    }
}


