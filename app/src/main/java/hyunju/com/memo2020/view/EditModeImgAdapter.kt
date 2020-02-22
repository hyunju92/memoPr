package hyunju.com.memo2020.view

import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hyunju.com.memo2020.R

class EditModeImgAdapter(var imgList: ArrayList<String>)
    : RecyclerView.Adapter<EditModeImgAdapter.MemoImgViewholder>() {

    var tempList: ArrayList<String> = ArrayList()

    init {
        Log.d("testImgAd", "onBindViewHolder iinit = " + imgList.size)

        tempList.clear()
        tempList.addAll(imgList)
        tempList.add("")
    }

    var mListener: OnItemBtnClickListener? = null

    interface OnItemBtnClickListener {
        fun onItemBtnClick(v: View, postion: Int, request: Int)
    }

    fun setOnItemBtnClickListener(listener: OnItemBtnClickListener) {
        mListener = listener
    }


    override fun getItemCount(): Int {
        Log.d("testImgAd", "onBindViewHolder imgList.size = " + imgList.size)

        return tempList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoImgViewholder {
        return MemoImgViewholder(LayoutInflater.from(parent.context).inflate(R.layout.edit_img_item, parent, false))
    }

    override fun onBindViewHolder(holder: MemoImgViewholder, position: Int) {
        Log.d("testImgAd", "onBindViewHolder position = " + position)

        holder.bindTo(position, tempList.get(position))
    }


    inner class MemoImgViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindTo(position: Int, imgStr: String) {
            if (tempList.size == position + 1) {

                itemView.findViewById<ConstraintLayout>(R.id.item_cl_edit_img).visibility = GONE
                itemView.findViewById<ConstraintLayout>(R.id.add_cl_edit_img).visibility = VISIBLE

                // btn click event listener
                itemView.findViewById<ImageButton>(R.id.camera_btn_edit_img).setOnClickListener {
                    mListener?.onItemBtnClick(it, position, R.id.camera_btn_edit_img)
                }
                itemView.findViewById<ImageButton>(R.id.album_btn_edit_img).setOnClickListener {
                    mListener?.onItemBtnClick(it, position, R.id.album_btn_edit_img)
                }
                itemView.findViewById<ImageButton>(R.id.uri_btn_edit_img).setOnClickListener {
                    mListener?.onItemBtnClick(it, position, R.id.uri_btn_edit_img)
                }

                return
            }

            if (!TextUtils.isEmpty(imgStr)) {
                Log.d("testImgAd", "imgStr = " + imgStr)

                val memoIv = itemView.findViewById<ImageView>(R.id.memo_iv_edit_img)
                Glide.with(itemView.context)
                        .load(imgStr)
                        .into(memoIv)

                // btn click event listener
                itemView.findViewById<ImageButton>(R.id.delete_btn_edit_img).setOnClickListener {
                    mListener?.onItemBtnClick(it, position, R.id.delete_btn_edit_img)
                }

            }
        }


    }
}