package hyunju.com.memo2020.list.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import hyunju.com.memo2020.R
import hyunju.com.memo2020.databinding.ListFragmentBinding
import hyunju.com.memo2020.list.vm.ListFragmentViewmodel

class ListFragment : Fragment() {
    private lateinit var binding: ListFragmentBinding
    private lateinit var listFragViewmodel: ListFragmentViewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let { listFragViewmodel = ListFragmentViewmodel(this@ListFragment, it.applicationContext) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.list_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeLiveData()
        setLayout()
    }

    private fun observeLiveData() {
        listFragViewmodel.allMemos.observe(viewLifecycleOwner, {
            val adapter = binding.listRv.adapter as ListItemAdapter
            adapter.submitList(it)
            adapter.notifyDataSetChanged() }
        )
    }

    private fun setLayout() {
        binding.listRv.setLayoutManager(LinearLayoutManager(requireContext()))
        binding.listRv.setHasFixedSize(true)

        binding.listRv.adapter = ListItemAdapter(listFragViewmodel)

        binding.addBtn.setOnClickListener {
            listFragViewmodel.moveEditFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listFragViewmodel.onDestroyViewModel()
    }


}