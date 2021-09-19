package hyunju.com.memo2020.list.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hyunju.com.memo2020.R
import hyunju.com.memo2020.databinding.ListItemBinding
import hyunju.com.memo2020.list.vm.ListViewModel
import hyunju.com.memo2020.db.Memo

class ListItemAdapter(private val vm: ListViewModel) :
        PagedListAdapter<Memo, ListItemAdapter.ListItemViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
       return DataBindingUtil.inflate<ListItemBinding>(LayoutInflater.from(parent.context), R.layout.list_item, parent, false).let {
            it.viewModel = vm
            ListItemViewHolder(it)
        }
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val memo = getItem(position)
        if (memo != null) {
            holder.bind(memo)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Memo>() {
            override fun areItemsTheSame(oldMemo: Memo,
                                         newMemo: Memo
            ): Boolean = oldMemo.id == newMemo.id

            override fun areContentsTheSame(oldMemo: Memo,
                                            newMemo: Memo
            ): Boolean = oldMemo == newMemo
        }
    }

    class ListItemViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(memo: Memo) {
            binding.data = memo

            val loadImg = memo.imageUriList.get()?.let { if (it.isNotEmpty()) it[0] else null } ?: R.drawable.ic_image_black_24dp
            val errorImg = R.drawable.ic_sms_failed_black_24dp

            Glide.with(binding.root.context)
                    .load(loadImg)
                    .error(errorImg)
                    .into(binding.thumbIv)

        }

    }


}