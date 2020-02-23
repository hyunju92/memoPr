package hyunju.com.memo2020.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hyunju.com.memo2020.R

class DetailModeImgAdapter(val imgList: ArrayList<String>)
    : RecyclerView.Adapter<DetailModeImgAdapter.MemoImgViewholder>() {

    override fun getItemCount(): Int {
        return imgList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoImgViewholder {
        return MemoImgViewholder(LayoutInflater.from(parent.context).inflate(R.layout.detail_img_item, parent, false))
    }

    override fun onBindViewHolder(holder: MemoImgViewholder, position: Int) {
        holder.bindTo(imgList.get(position))
    }


    inner class MemoImgViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindTo(imgStr: String) {
            val memoIv = itemView.findViewById<ImageView>(R.id.memo_iv_edit_img)

            var loadImg = if (imgStr.isNotEmpty()) imgStr else R.drawable.ic_image_black_24dp
            val errorImg = R.drawable.ic_sms_failed_black_24dp

            Glide.with(itemView.context)
                    .load(loadImg)
                    .error(errorImg)
                    .into(memoIv)
        }
    }

}