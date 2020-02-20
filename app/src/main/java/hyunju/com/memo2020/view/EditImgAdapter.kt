package hyunju.com.memo2020.view

import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hyunju.com.memo2020.R

class EditImgAdapter(val imgList: ArrayList<String>)
    : RecyclerView.Adapter<EditImgAdapter.MemoImgViewholder>() {

    override fun getItemCount(): Int {
        Log.d("testImgAd", "onBindViewHolder imgList.size = " + imgList.size)

        return imgList.size + 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoImgViewholder {
        return MemoImgViewholder(LayoutInflater.from(parent.context).inflate(R.layout.edit_img_item, parent, false))
    }

    override fun onBindViewHolder(holder: MemoImgViewholder, position: Int) {
        Log.d("testImgAd", "onBindViewHolder position = " + position)

        val isLast = position == imgList.size
        val imgStr = if (!isLast) imgList.get(position) else ""

        holder.bindTo(imgStr, isLast)
    }


    inner class MemoImgViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val memoIv = itemView.findViewById<ImageView>(R.id.memo_iv)

        fun bindTo(imgStr: String, isLast: Boolean) {
            if (isLast) {
                itemView.findViewById<ConstraintLayout>(R.id.item_cl).visibility = GONE
                itemView.findViewById<ConstraintLayout>(R.id.add_cl).visibility = VISIBLE
                return
            }

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