package hyunju.com.memo2020

import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("matchHeightToDeviceWidth")
fun matchHeightToDeviceWidth(view: View, boolean: Boolean) {

    val pxWidth = view.resources.displayMetrics.widthPixels
    val layoutParams: ViewGroup.LayoutParams = view.layoutParams
    layoutParams.height = pxWidth
    view.layoutParams = layoutParams
}


@BindingAdapter("setOnLongClick")
fun setOnLongClick(view: View, func: () -> Unit) {
    view.setOnLongClickListener {
        func()
        return@setOnLongClickListener true
    }
}

interface RecyclerAdapter<T> {
    fun replaceAll(recyclerView: RecyclerView, listItem: List<T>?)
}

fun <T> RecyclerView.replaceAll(listItem: List<T>?) {
    (this.adapter as? RecyclerAdapter<T>)?.replaceAll(this, listItem)
}