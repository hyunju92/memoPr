package hyunju.com.memo2020.view

import android.text.TextUtils
import android.util.Log
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
        Log.d("testImgAd", "onBindViewHolder imgList.size = " + imgList.size)

        return imgList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoImgViewholder {
        return MemoImgViewholder(LayoutInflater.from(parent.context).inflate(R.layout.detail_img_item, parent, false))
    }

    override fun onBindViewHolder(holder: MemoImgViewholder, position: Int) {
        Log.d("testImgAd", "onBindViewHolder position = " + position)

        holder.bindTo(imgList.get(position))
    }


    inner class MemoImgViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val memoIv = itemView.findViewById<ImageView>(R.id.memo_iv)

        fun bindTo(imgStr: String) {
            if (!TextUtils.isEmpty(imgStr)) {
                Log.d("testImgAd", "imgStr = " + imgStr)
//
                Glide.with(itemView.context)
                        .load(imgStr)
                        .into(memoIv)

            }
        }

    }
}