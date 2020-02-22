package hyunju.com.memo2020.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import hyunju.com.memo2020.R
import hyunju.com.memo2020.databinding.ListFragmentBinding
import hyunju.com.memo2020.db.MemoAdapter
import hyunju.com.memo2020.model.Memo
import hyunju.com.memo2020.viewmodel.ListFragmentViewmodel

class ListFragment : Fragment(), MemoAdapter.OnItemClickListener {
    protected lateinit var binding: ListFragmentBinding
    protected val listFragViewmodel: ListFragmentViewmodel by lazy {
        ViewModelProvider(this).get(ListFragmentViewmodel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.list_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observerLiveData()
        setLayout()

    }

    private fun observerLiveData() {
        listFragViewmodel.allMemos.observe(this,
                androidx.lifecycle.Observer {
                    val adapter = binding.listRv.adapter as MemoAdapter

                    Log.d("testsObserver", "in observe " + it.size)
                    adapter.submitList(it)
                    adapter.notifyDataSetChanged()
                }
        )
    }

    private fun setLayout() {
        binding.listRv.setLayoutManager(LinearLayoutManager(requireContext()))
        binding.listRv.setHasFixedSize(true)

        MemoAdapter().let {
            it.setOnItemClickListener(this)
            binding.listRv.adapter = it
        }

        binding.testBtn.setOnClickListener {
        }

        binding.addBtn.setOnClickListener {
            listFragViewmodel.moveDetailFrag(view = it, memoItem = null, mode = 2)

        }
    }


    // MemoAdapter.OnItemClickListener
    override fun onItemClick(v: View, memo: Memo) {
        Log.d("testsObserver", "in onItemClick id = " + memo.id)
//
//        val newContents = memo.contents + " 수정"
//        memo.contents = newContents
//        memo.images += " " + "https://postfiles.pstatic.net/MjAxODEwMjVfMTc3/MDAxNTQwNDY2MjY2MDM1.TovTMgYAZn8WJggpdZvlHWqBxsVCCf_Z6897OJ0WNTgg.o-KiNShXVmEh8ZJxdrVNELLzCe1XRh-T1ZfP84xSDq8g.JPEG.hyelim5012/IMG_20181023_061859.jpg?type=w966"
//        listFragViewmodel.update(memo)

        listFragViewmodel.moveDetailFrag(v, memo, 1)

    }


}