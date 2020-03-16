package hyunju.com.memo2020.view

import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter


object BindingAdapter {

    @BindingAdapter("memo:match_height_to_device_width")
    @JvmStatic
    fun matchHeightToDeviceWidth(view: View, boolean: Boolean) {

        val pxWidth = view.getResources().getDisplayMetrics().widthPixels
        val layoutParams: ViewGroup.LayoutParams = view.layoutParams
        layoutParams.height = pxWidth
        view.layoutParams = layoutParams
    }


}