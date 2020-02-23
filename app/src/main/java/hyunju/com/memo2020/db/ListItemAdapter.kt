package hyunju.com.memo2020.db

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hyunju.com.memo2020.R
import hyunju.com.memo2020.model.Memo
import kotlinx.android.synthetic.main.memo_item.view.*

class ListItemAdapter :
        PagedListAdapter<Memo, ListItemAdapter.MemoViewholder>(DIFF_CALLBACK) {

    var mListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(v: View, memo: Memo)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewholder {
        return MemoViewholder(parent)
    }

    override fun onBindViewHolder(holder: MemoViewholder, position: Int) {
        val memo = getItem(position)
        if (memo != null) {
            holder.bindTo(memo)
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

    inner class MemoViewholder(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.memo_item, parent, false)) {

        init {
            itemView.setOnClickListener {
                mListener?.onItemClick(it, memo!!)
            }
        }

        var memo: Memo? = null


        fun bindTo(memo: Memo?) {
            this.memo = memo

            val loadImg = memo?.getImageList()!!.let {
                if (it.size > 0) it[0] else null
            } ?: R.drawable.ic_image_black_24dp

            val errorImg = R.drawable.ic_sms_failed_black_24dp

            Glide.with(itemView)
                    .load(loadImg)
                    .error(errorImg)
                    .into(itemView.thumb_iv)

            itemView.title_tv.text = memo.title
            itemView.contents_tv.text = memo.contents

        }

    }


}