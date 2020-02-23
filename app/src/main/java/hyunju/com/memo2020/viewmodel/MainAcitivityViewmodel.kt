package hyunju.com.memo2020.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import hyunju.com.memo2020.db.MemoDatabase
import hyunju.com.memo2020.model.Memo
import java.util.*

class MainAcitivityViewmodel(application: Application) : AndroidViewModel(application) {

    val dao = MemoDatabase.get(application).memoDao()
    var allMemos: LiveData<PagedList<Memo>> = LivePagedListBuilder(
            dao.getAllMemoById(),
            20
    ).build()


    fun insert(memo: Memo) {
        doAsync {
            dao.insert(memo)
        }.execute()
    }

    fun update(memo: Memo) {
        doAsync {
            dao.update(memo)
        }.execute()
    }

    class doAsync(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            handler()
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            Log.d("testsObserver", "onPostExecute ")

        }
    }

    // permission check and granted
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


