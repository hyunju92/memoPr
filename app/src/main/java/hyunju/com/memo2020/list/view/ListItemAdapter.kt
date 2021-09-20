package hyunju.com.memo2020.list.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import hyunju.com.memo2020.R
import hyunju.com.memo2020.databinding.ListItemBinding
import hyunju.com.memo2020.list.vm.ListViewModel
import hyunju.com.memo2020.db.Memo

class ListItemAdapter(private val vm: ListViewModel) :
    PagedListAdapter<Memo, ListItemAdapter.ListItemViewHolder>(ListDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        return DataBindingUtil.inflate<ListItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_item,
            parent,
            false
        ).let {
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

    class ListDiffUtil : DiffUtil.ItemCallback<Memo>() {
        override fun areItemsTheSame(oldItem: Memo, newItem: Memo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Memo, newItem: Memo): Boolean {
            return oldItem == newItem
        }

    }

    class ListItemViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(memo: Memo) {
            binding.data = memo
        }

    }


}