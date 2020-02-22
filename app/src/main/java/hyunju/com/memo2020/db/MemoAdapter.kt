package hyunju.com.memo2020.db

import android.util.Log
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

class MemoAdapter :
        PagedListAdapter<Memo, MemoAdapter.MemoViewholder>(DIFF_CALLBACK) {

    var mListener: OnItemClickListener? = null


    interface OnItemClickListener {
        fun onItemClick(v: View, memo: Memo)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewholder {
        Log.d("testsObserver", "onCreateViewHolder = ")

        return MemoViewholder(parent)
    }

    override fun onBindViewHolder(holder: MemoViewholder, position: Int) {
        val memo = getItem(position)
        Log.d("testsObserver", "onBindViewHolder ini = ")

        if (memo != null) {
            Log.d("testsObserver", "onBindViewHolder = ")

            holder.bindTo(memo)
        } else {
            Log.d("testsObserver", "onBindViewHolder null = ")

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

        init {
            itemView.setOnClickListener {
                mListener?.onItemClick(it, memo!!)
            }
        }


        var memo: Memo? = null

        val thumIv = itemView.thumb_iv
        val titleTv = itemView.title_tv
        val contentsTv = itemView.contents_tv

        fun bindTo(memo: Memo?) {

            this.memo = memo
            Log.d("testsObserver", "bindTo = " + memo?.title)
            Log.d("testsObserver", "bindTo id = " + memo?.id)

            val imgList = memo?.getImageList()!!
            val thmImg = imgList.let {
                if(it.size > 0) it[0] else null
            }

            Glide.with(itemView)
                    .load(thmImg)
                    .into(thumIv)

//        Glide.with()
//                .load("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory&fname=https://k.kakaocdn.net/dn/EShJF/btquPLT192D/SRxSvXqcWjHRTju3kHcOQK/img.png")
//                .into(binding.iv)

            titleTv.text = memo?.id.toString()
            contentsTv.text = memo?.contents

        }

    }


}