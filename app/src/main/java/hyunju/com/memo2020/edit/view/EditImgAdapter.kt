package hyunju.com.memo2020.edit.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hyunju.com.memo2020.R
import hyunju.com.memo2020.RecyclerAdapter
import hyunju.com.memo2020.databinding.EditImgItemBinding
import hyunju.com.memo2020.edit.vm.EditFragmentViewmodel

class EditImgAdapter(private val vm: EditFragmentViewmodel)
    : RecyclerView.Adapter<EditImgAdapter.EditImgViewHolder>(), RecyclerAdapter<String> {

    private var imgItemList = mutableListOf<String>()
    companion object { private const val LAST_ITEM_STR = "LAST_ITEM_STR" }

    override fun replaceAll(recyclerView: RecyclerView, listItem: List<String>?) {
        listItem?.let { newList ->
            imgItemList.clear()
            imgItemList.addAll(newList)
            imgItemList.add(LAST_ITEM_STR)
            notifyDataSetChanged()
        }
    }

    // override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditImgViewHolder {
        return DataBindingUtil.inflate<EditImgItemBinding>(LayoutInflater.from(parent.context), R.layout.edit_img_item, parent, false).let {
            it.viewModel = vm
            EditImgViewHolder(it)
        }
    }

    override fun onBindViewHolder(holder: EditImgViewHolder, position: Int) {
        holder.bind(position, imgItemList[position])
    }

    override fun getItemCount(): Int {
        return imgItemList.size
    }

    class EditImgViewHolder(private val binding: EditImgItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int, imgStr: String) {
            binding.position = position
            binding.isLastItem = imgStr == "LAST_ITEM_STR"

            val loadImg = if (imgStr.isNotEmpty()) imgStr else R.drawable.ic_image_black_24dp
            val errorImg = R.drawable.ic_sms_failed_black_24dp

            binding.memoIvEditImg.let {
                Glide.with(binding.root.context)
                    .load(loadImg)
                    .error(errorImg)
                    .into(it)
            }

        }
    }


}