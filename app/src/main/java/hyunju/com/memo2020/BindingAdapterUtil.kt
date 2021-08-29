package hyunju.com.memo2020

import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("memo:match_height_to_device_width")
fun matchHeightToDeviceWidth(view: View, boolean: Boolean) {

    val pxWidth = view.resources.displayMetrics.widthPixels
    val layoutParams: ViewGroup.LayoutParams = view.layoutParams
    layoutParams.height = pxWidth
    view.layoutParams = layoutParams
}


interface RecyclerAdapter<T> {
    fun replaceAll(recyclerView: RecyclerView, listItem: List<T>?)
}

@BindingAdapter("replace")
fun <T> RecyclerView.replace(listItem: List<T>?) {
    (this.adapter as? RecyclerAdapter<T>)?.replaceAll(this, listItem)
}