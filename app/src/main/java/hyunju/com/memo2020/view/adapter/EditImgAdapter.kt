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

class EditImgAdapter(imgList: ArrayList<String>, val itemClick: (View, Int) -> Unit)
    : RecyclerView.Adapter<EditImgAdapter.EditImgViewholder>() {

    // make temp list (to show last view showing 3 btn to get image btn)
    var tempList: ArrayList<String> = ArrayList()

    init {
        tempList.clear()
        tempList.addAll(imgList)
        tempList.add("")
    }

    // override
    override fun getItemCount(): Int {
        return tempList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditImgViewholder {
        val inflater = LayoutInflater.from(parent!!.getContext())
        val view = inflater.inflate(R.layout.edit_img_item, parent, false)
        return EditImgViewholder(view)

    }

    override fun onBindViewHolder(holder: EditImgViewholder, position: Int) {
        holder.bind(position, tempList[position])
    }


    inner class EditImgViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // item view
        val itemCl = itemView.findViewById<ConstraintLayout>(R.id.item_cl_edit_img)
        val deltetBtn = itemView.findViewById<ImageButton>(R.id.delete_btn_edit_img)

        // last item view
        val lastCl = itemView.findViewById<ConstraintLayout>(R.id.last_cl_edit_img)
        val cameraBtn = itemView.findViewById<ImageButton>(R.id.camera_btn_edit_img)
        val albumBtn = itemView.findViewById<ImageButton>(R.id.album_btn_edit_img)
        val uriBtn = itemView.findViewById<ImageButton>(R.id.uri_btn_edit_img)


        fun bind(position: Int, imgStr: String) {
            if (tempList.size == position + 1) {
                // set last view
                cameraBtn.setOnClickListener { itemClick(it, position) }
                albumBtn.setOnClickListener { itemClick(it, position) }
                uriBtn.setOnClickListener { itemClick(it, position) }

                itemCl.visibility = GONE
                lastCl.visibility = VISIBLE
                return
            }

            deltetBtn.setOnClickListener { itemClick(it, position) }

            val memoIv = itemView.findViewById<ImageView>(R.id.memo_iv_edit_img)
            val loadImg = if (imgStr.isNotEmpty()) imgStr else R.drawable.ic_image_black_24dp
            val errorImg = R.drawable.ic_sms_failed_black_24dp

            Glide.with(itemView.context)
                    .load(loadImg)
                    .error(errorImg)
                    .into(memoIv)

        }

    }
}