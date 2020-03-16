package hyunju.com.memo2020.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hyunju.com.memo2020.R

class ViewImgAdapter(val imgList: ArrayList<String>)
    : RecyclerView.Adapter<ViewImgAdapter.ViewImgViewholder>() {

    override fun getItemCount(): Int {
        return imgList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewImgViewholder {
        val inflater = LayoutInflater.from(parent.getContext())
        val view = inflater.inflate(R.layout.view_img_item, parent, false)
        return ViewImgViewholder(view)

    }

    override fun onBindViewHolder(holder: ViewImgViewholder, position: Int) {
        holder.bind(imgList.get(position))
    }


    inner class ViewImgViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val memoIv = itemView.findViewById<ImageView>(R.id.memo_iv_edit_img)

        fun bind(imgStr: String) {

            var loadImg = if (imgStr.isNotEmpty()) imgStr else R.drawable.ic_image_black_24dp
            val errorImg = R.drawable.ic_sms_failed_black_24dp

            Glide.with(itemView.context)
                    .load(loadImg)
                    .error(errorImg)
                    .into(memoIv)
        }
    }

}