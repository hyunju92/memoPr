package hyunju.com.memo2020.util

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hyunju.com.memo2020.R

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

@BindingAdapter("replaceAll")
fun <T> RecyclerView.replaceAll(listItem: List<T>?) {
    (this.adapter as? RecyclerAdapter<T>)?.replaceAll(this, listItem)
}

@BindingAdapter("setImgUri")
fun setImgUri(imageView: ImageView, uri: String?) {
    val loadImg = if (uri.isNullOrEmpty()) R.drawable.ic_image_black_24dp else uri
    val errorImg = R.drawable.ic_sms_failed_black_24dp

        Glide.with(imageView.rootView.context)
            .load(loadImg)
            .error(errorImg)
            .into(imageView)
}

@BindingAdapter("setFirstImgUri")
fun setFirstImgUri(imageView: ImageView, uriList:List<String>) {
    val uri = if(uriList.isNullOrEmpty()) null else uriList[0]
    val loadImg = if (uri.isNullOrEmpty()) R.drawable.ic_image_black_24dp else uri
    val errorImg = R.drawable.ic_sms_failed_black_24dp

    Glide.with(imageView.rootView.context)
        .load(loadImg)
        .error(errorImg)
        .into(imageView)
}