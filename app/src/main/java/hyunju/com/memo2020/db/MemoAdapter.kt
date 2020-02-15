package hyunju.com.memo2020.db

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import hyunju.com.memo2020.R
import hyunju.com.memo2020.model.Memo
import kotlinx.android.synthetic.main.memo_item.view.*

class MemoAdapter :
        PagedListAdapter<Memo, MemoAdapter.MemoViewholder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewholder {
        return MemoViewholder(parent)
    }

    override fun onBindViewHolder(holder: MemoViewholder, position: Int) {
        val memo = getItem(position)
        if(memo != null) {
            holder.bindTo(memo)
        } else {
//            holder.clear()
        }
    }


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Memo>() {
            override fun areItemsTheSame(oldMemo: Memo,
                                         newMemo: Memo): Boolean =
                    oldMemo.id == newMemo.id

            override fun areContentsTheSame(oldMemo: Memo,
                                            newMemo: Memo): Boolean =
                    oldMemo == newMemo
        }
    }

    inner class MemoViewholder(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.memo_item, parent, false)
    ) {

        var memo : Memo? = null
        val thumIv = itemView.thumb_iv
        val titleTv = itemView.title_tv
        val contentsTv = itemView.contents_tv

        fun bindTo(memo: Memo?) {
            this.memo = memo
            thumIv.setImageURI(Uri.parse(memo?.images))
            titleTv.text = memo?.title
            contentsTv.text = memo?.contents

        }

    }



}