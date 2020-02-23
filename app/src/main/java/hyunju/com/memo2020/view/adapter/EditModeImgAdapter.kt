package hyunju.com.memo2020.view.adapter

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

    // * make temp list
    // to show last edit view (show 3-way get image btn )
    var tempList: ArrayList<String> = ArrayList()

    init {
        tempList.clear()
        tempList.addAll(imgList)
        tempList.add("")
    }


    // * listener to send ItemFragment
    // when deletem/get image event occur
    var mListener: OnItemBtnClickListener? = null

    interface OnItemBtnClickListener {
        fun onItemBtnClick(v: View, postion: Int, requestBtnId: Int)
    }

    fun setOnItemBtnClickListener(listener: OnItemBtnClickListener) {
        mListener = listener
    }


    // override method
    override fun getItemCount(): Int {
        return tempList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoImgViewholder {
        return MemoImgViewholder(LayoutInflater.from(parent.context).inflate(R.layout.edit_img_item, parent, false))
    }

    override fun onBindViewHolder(holder: MemoImgViewholder, position: Int) {
        holder.bindTo(position, tempList[position])
    }


    inner class MemoImgViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var positon: Int? = null

        init {
            // btn click event listener
            itemView.findViewById<ImageButton>(R.id.camera_btn_edit_img).setOnClickListener {
                mListener?.onItemBtnClick(it, this.positon!!, R.id.camera_btn_edit_img)
            }
            itemView.findViewById<ImageButton>(R.id.album_btn_edit_img).setOnClickListener {
                mListener?.onItemBtnClick(it, this.positon!!, R.id.album_btn_edit_img)
            }
            itemView.findViewById<ImageButton>(R.id.uri_btn_edit_img).setOnClickListener {
                mListener?.onItemBtnClick(it, this.positon!!, R.id.uri_btn_edit_img)
            }
            itemView.findViewById<ImageButton>(R.id.delete_btn_edit_img).setOnClickListener {
                mListener?.onItemBtnClick(it, this.positon!!, R.id.delete_btn_edit_img)
            }
        }

        fun bindTo(position: Int, imgStr: String) {
            this.positon = position

            if (tempList.size == position + 1) {
                // set delte btn and last edit view
                itemView.findViewById<ConstraintLayout>(R.id.item_cl_edit_img).visibility = GONE
                itemView.findViewById<ConstraintLayout>(R.id.last_cl_edit_img).visibility = VISIBLE
                return
            }

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