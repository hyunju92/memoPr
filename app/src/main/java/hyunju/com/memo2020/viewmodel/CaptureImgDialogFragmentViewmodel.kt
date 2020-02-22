package hyunju.com.memo2020.viewmodel

import android.app.Application
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.Navigation
import hyunju.com.memo2020.view.ItemFragmentDirections

class CaptureImgDialogFragmentViewmodel(application: Application) : AndroidViewModel(application) {

    // nav
    fun moveCaptureImgDialogFrag(view: View, uriStr: String?) {
        if (TextUtils.isEmpty(uriStr)) return
        val action = ItemFragmentDirections.actionItemFragmentToCaptureImgDialogFragment(uriStr!!)
        Navigation.findNavController(view).navigate(action)
    }
}


