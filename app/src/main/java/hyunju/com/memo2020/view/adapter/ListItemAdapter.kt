package hyunju.com.memo2020.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hyunju.com.memo2020.R
import hyunju.com.memo2020.model.Memo
import hyunju.com.memo2020.model.getDateText
import kotlinx.android.synthetic.main.memo_item.view.*

class ListItemAdapter(val itemClick: (View, Memo) -> Unit, val itemLongClick: (View, Memo) -> Unit) :
        PagedListAdapter<Memo, ListItemAdapter.ListItemViewholder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewholder {
        return ListItemViewholder(parent)
    }

    override fun onBindViewHolder(holder: ListItemViewholder, position: Int) {
        val memo = getItem(position)
        if (memo != null) {
            holder.bind(memo)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Memo>() {
            override fun areItemsTheSame(oldMemo: Memo,
                                         newMemo: Memo): Boolean = oldMemo.id == newMemo.id

            override fun areContentsTheSame(oldMemo: Memo,
                                            newMemo: Memo): Boolean = oldMemo == newMemo
        }
    }

    inner class ListItemViewholder(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.memo_item, parent, false)) {

        fun bind(memo: Memo) {
            itemView.setOnLongClickListener {
                itemLongClick(it, memo)
                true
            }
            itemView.setOnClickListener { itemClick(it, memo) }

            val loadImg = memo.imageUrlList.let { if (it.isNotEmpty()) it[0] else null } ?: R.drawable.ic_image_black_24dp
            val errorImg = R.drawable.ic_sms_failed_black_24dp

            Glide.with(itemView)
                    .load(loadImg)
                    .error(errorImg)
                    .into(itemView.thumb_iv)

            itemView.title_tv.text = memo.title
            itemView.date_tv.text = memo.getDateText(itemView.context)
            itemView.contents_tv.text = memo.contents

        }

    }


}