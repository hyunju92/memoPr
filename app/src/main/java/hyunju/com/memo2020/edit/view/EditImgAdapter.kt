package hyunju.com.memo2020.edit.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hyunju.com.memo2020.R
import hyunju.com.memo2020.RecyclerAdapter
import hyunju.com.memo2020.databinding.EditImgItemBinding
import hyunju.com.memo2020.edit.vm.EditFragmentViewmodel

class EditImgAdapter(private val vm: EditFragmentViewmodel, val itemClick: (View, Int) -> Unit)
    : RecyclerView.Adapter<EditImgAdapter.EditImgViewHolder>(), RecyclerAdapter<String> {

    private var imgItemList = mutableListOf<String>()

    override fun replaceAll(recyclerView: RecyclerView, listItem: List<String>?) {
        listItem?.let { newList ->
            imgItemList.clear()
            imgItemList.addAll(newList)
            notifyDataSetChanged()
        }
    }

    // override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditImgViewHolder {
        return DataBindingUtil.inflate<EditImgItemBinding>(LayoutInflater.from(parent.context), R.layout.edit_img_item, parent, false).let {
            it.viewModel = vm
            EditImgViewHolder(vm, it)
        }
    }

    override fun onBindViewHolder(holder: EditImgViewHolder, position: Int) {
        holder.bind(position, imgItemList[position])
    }

    override fun getItemCount(): Int {
        return imgItemList.size
    }

    class EditImgViewHolder(private val viewModel: EditFragmentViewmodel, private val binding: EditImgItemBinding) : RecyclerView.ViewHolder(binding.root) {
        // last item view
        val lastCl = itemView.findViewById<ConstraintLayout>(R.id.last_cl_edit_img)
        val cameraBtn = itemView.findViewById<ImageButton>(R.id.camera_btn_edit_img)
        val albumBtn = itemView.findViewById<ImageButton>(R.id.album_btn_edit_img)

        fun bind(position: Int, imgStr: String) {


//            if (imgItemList.size == position + 1) {
//                // set last view
//                cameraBtn.setOnClickListener { itemClick(it, position) }
//                albumBtn.setOnClickListener { itemClick(it, position) }
//
//                itemCl.visibility = GONE
//                lastCl.visibility = VISIBLE
//                return
//            }


            val loadImg = if (imgStr.isNotEmpty()) imgStr else R.drawable.ic_image_black_24dp
            val errorImg = R.drawable.ic_sms_failed_black_24dp

            binding.memoIvEditImg.let {
                Glide.with(itemView.context)
                    .load(loadImg)
                    .error(errorImg)
                    .into(it)
            }
            binding.deleteBtnEditImg.setOnClickListener { viewModel.deleteImg(position) }

        }
    }


}