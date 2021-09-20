package hyunju.com.memo2020.edit.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import hyunju.com.memo2020.R
import hyunju.com.memo2020.databinding.EditImgItemBinding
import hyunju.com.memo2020.edit.vm.EditViewModel
import hyunju.com.memo2020.util.RecyclerAdapter

class EditImgAdapter(private val vm: EditViewModel) :
    RecyclerView.Adapter<EditImgAdapter.EditImgViewHolder>(), RecyclerAdapter<String> {

    private var imgItemList : MutableList<String>? = null

    companion object {
        private const val LAST_ITEM_STR = "LAST_ITEM_STR"
    }

    override fun replaceAll(recyclerView: RecyclerView, listItem: List<String>?) {
        val newList = listItem?.toMutableList()?.apply {
            remove("")
        } ?: listOf()

        if (imgItemList == null) {
            imgItemList = mutableListOf()
            imgItemList!!.addAll(newList)
            imgItemList!!.add(LAST_ITEM_STR)

            notifyDataSetChanged()

        } else {
            imgItemList!!.clear()
            imgItemList!!.addAll(newList)
            imgItemList!!.add(LAST_ITEM_STR)

            val diffResult = DiffUtil.calculateDiff(EditDiffUtil(imgItemList!!, newList))
            diffResult.dispatchUpdatesTo(this)
        }

    }

    // override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditImgViewHolder {
        return DataBindingUtil.inflate<EditImgItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.edit_img_item,
            parent,
            false
        ).let {
            it.viewModel = vm
            EditImgViewHolder(it)
        }
    }

    override fun onBindViewHolder(holder: EditImgViewHolder, position: Int) {
        holder.bind(imgItemList!![position])
    }

    override fun getItemCount(): Int {
        return imgItemList!!.size
    }

    fun getItemList(): List<String> {
        return imgItemList!!.apply { if (isNotEmpty()) removeLast() }.toList()
    }


    class EditDiffUtil (private val oldList : List<String>, private val newList: List<String>) : DiffUtil.Callback(){
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return true
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

    }

    class EditImgViewHolder(private val binding: EditImgItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imgStr: String) {
            binding.imgStr = imgStr
            binding.isLastItem = imgStr == LAST_ITEM_STR
        }
    }


}