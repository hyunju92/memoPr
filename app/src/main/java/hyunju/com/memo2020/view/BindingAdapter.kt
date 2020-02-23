package hyunju.com.memo2020.view

import android.util.Log
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
        Log.d("testBinding", "heigt " + pxWidth)
        view.layoutParams = layoutParams
    }


    @BindingAdapter("memo:match_width_to_device_width")
    @JvmStatic
    fun matchWidthToDeviceWidth(view: View, boolean: Boolean) {
        val pxWidth = view.getResources().getDisplayMetrics().widthPixels
        val layoutParams: ViewGroup.LayoutParams = view.layoutParams
        layoutParams.width = pxWidth
        Log.d("testBinding", "pxWidth " + pxWidth)
        view.layoutParams = layoutParams
    }

    @BindingAdapter("memo:match_heigth_to_device_height")
    @JvmStatic
    fun matchHeightToDeviceHeight(view: View, boolean: Boolean) {
        val pxHeight = view.getResources().getDisplayMetrics().heightPixels
        val layoutParams: ViewGroup.LayoutParams = view.layoutParams
        layoutParams.height = pxHeight
        Log.d("testBinding", "pxHeight " + pxHeight)
        view.layoutParams = layoutParams
    }



}