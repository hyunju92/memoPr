package hyunju.com.memo2020.viewmodel

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import hyunju.com.memo2020.db.MemoDatabase
import hyunju.com.memo2020.model.Memo
import hyunju.com.memo2020.view.ItemFragmentDirections
import java.util.*
import kotlin.collections.ArrayList

class ItemFragmentViewmodel(application: Application) : AndroidViewModel(application) {

    val dao = MemoDatabase.get(application).memoDao()
    val mode: MutableLiveData<Int> = MutableLiveData()
    var memoItem: MutableLiveData<Memo?> = MutableLiveData()


    // * 2 mode (detail/edit)
    val DETAIL_MODE = 1 // just view to show detail memo
    val EDIT_MODE = 2   // edit contents view

    // mode when first init
    // (from list item click or edit btn click)
    private var firstInitMode: Int = 0

    fun changeViewMode(currentMode: Int) {
        if (firstInitMode == 0) firstInitMode = currentMode
        this.mode.value = currentMode
    }

    fun isEditMode(): Boolean {
        return this.mode.value == EDIT_MODE
    }


    //  * get memo img list
    fun getImgList(): ArrayList<String> {
        return if (memoItem.value != null) memoItem.value!!.getImageList() else ArrayList()
    }


    // * core action (need db)
    fun save(context: Context, title: String, contents: String) {
        val newMemo = memoItem.value?.apply {
            this.title = title
            this.contents = contents
            this.date = Date()

        } ?: Memo(title = title, contents = contents, images = "", date = Date())


        if (firstInitMode == EDIT_MODE) {   // from edit btn
            insert(newMemo)
            Toast.makeText(context, "저장되었습니다.", Toast.LENGTH_SHORT).show()
            firstInitMode = DETAIL_MODE

        } else {
            update(newMemo)
            Toast.makeText(context, "수정되었습니다.", Toast.LENGTH_SHORT).show()

        }
    }

    fun delete(context: Context) {
        if (memoItem.value != null) {
            delete(memoItem.value!!.id)
            Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }


    // * access db
    private fun update(memo: Memo?) {
        doAsync {
            dao.update(memo)
        }.execute()
    }

    private fun insert(memo: Memo) {
        doAsync {
            dao.insert(memo)
        }.execute()
    }

    private fun delete(id: Long) {
        doAsync {
            dao.deleteById(id)
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


    // nav
    fun moveCaptureImgDialogFrag(view: View, uriStr: String?) {
        if (TextUtils.isEmpty(uriStr)) return

//        val testUrl = "https://postfiles.pstatic.net/MjAxNzA5MjVfMjcg/MDAxNTA2MzA0Mzc1MTM4.prbIrhy_KnEJAq0I4WGX0yHDuCRhQsbKcHU6RGDVVNog.5o4kxdfkv-bGtI4-gXRwXjh045Yz77L8WnFFOt6PCQIg.JPEG.rla2945/13402575_643830889104534_68262404_n.jpg?type=w580"
//        val testUrl = "http://blogfiles.naver.net/MjAxODEyMjBfMTI3/MDAxNTQ1Mjg4ODgzMTc3.o6BqoojNTIKMzP2SPe3Idpx_mo6bE1XaRh1OF7QGk-Ig.9eL4YyOfa1v_6aejx5gK1Bpoe78UmUGvz5AUQ2jr7YAg.JPEG.petgeek/20181220_110730.jpg"
//        val testUrl = "https://blogfiles.pstatic.net/MjAxOTA5MDRfNiAg/MDAxNTY3NTc5NTQ2ODI0.J_Hb7GFGIp5X1aosH-Mo9p73gMul6MznKEL-yrjpw1Mg.mgRAWlZU5_3XRcWudMrCJQezo_c2KcvU2rjZ6slyT3Mg.JPEG.cgdong10/%ED%81%AC%EA%B8%B0%EB%B3%80%ED%99%98_%ED%81%AC%EB%9F%AC%EC%89%AC-%EB%82%98%EB%B9%A0.jpg?type=w1"
//        val testUrl = "http://imgnews.naver.net/image/215/2019/12/03/A201912030013_1_20191203071403938.jpg"
//        val testUrl = "https://search.pstatic.net/common/?src=http%3A%2F%2Fmusicmeta.phinf.naver.net%2Fphoto%2F000%2F079%2F79132.jpg&type=b400"
//        val testUrl = "http://blogfiles.naver.net/20140605_23/qwerz00_14018944939128amlJ_JPEG/Teaser_Crush%28_%AC%EB%9F%AC_____%27Crush_On_You%27_Preview_%281080p%29.mp4_20140605_000624.135.jpg"
//        val testUrl = "http://image.nmv.naver.net/blog_2019_09_20_2925/ba9b42c1-db4e-11e9-bc16-0000000049a8_02.jpg"

        val testUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcTQlHniwP20JZYMssUzLYRpPfGTndcxsPNkwRK9S3aKADifMEel"

        val action = ItemFragmentDirections.actionItemFragmentToCaptureImgDialogFragment(testUrl!!)
        Navigation.findNavController(view).navigate(action)
    }


}


