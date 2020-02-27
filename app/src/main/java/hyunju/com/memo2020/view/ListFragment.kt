package hyunju.com.memo2020.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import hyunju.com.memo2020.R
import hyunju.com.memo2020.databinding.ListFragmentBinding
import hyunju.com.memo2020.model.Memo
import hyunju.com.memo2020.view.adapter.ListItemAdapter
import hyunju.com.memo2020.viewmodel.ListFragmentViewmodel

class ListFragment : Fragment(), ListItemAdapter.OnItemClickListener {
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
                    val adapter = binding.listRv.adapter as ListItemAdapter
                    adapter.submitList(it)
                    adapter.notifyDataSetChanged()
                }
        )
    }

    private fun setLayout() {
        binding.listRv.setLayoutManager(LinearLayoutManager(requireContext()))
        binding.listRv.setHasFixedSize(true)

        ListItemAdapter().let {
            it.setOnItemClickListener(this)
            binding.listRv.adapter = it
        }

        binding.addBtn.setOnClickListener {
            listFragViewmodel.moveItemFragment(view = it, memoItem = null, mode = 2)

        }
    }


    // ListAdapter.OnItemClickListener
    override fun onItemClick(v: View, memo: Memo) {
        listFragViewmodel.moveItemFragment(v, memo, 1)
    }


}