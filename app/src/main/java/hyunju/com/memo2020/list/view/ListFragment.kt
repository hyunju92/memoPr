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
import hyunju.com.memo2020.db.MemoDatabase
import hyunju.com.memo2020.list.vm.ListViewModel
import hyunju.com.memo2020.model.MemoRepository

class ListFragment : Fragment() {
    private lateinit var binding: ListFragmentBinding
    private lateinit var listViewModel: ListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            val repository = MemoRepository(MemoDatabase.get(it.applicationContext))
            listViewModel = ListViewModel(repository,this@ListFragment)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate<ListFragmentBinding>(inflater, R.layout.list_fragment, container, false).apply {
            viewModel = listViewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        setLayout()
    }

    private fun observeLiveData() {
        listViewModel.allMemos.observe(viewLifecycleOwner, {
                val adapter = binding.listRv.adapter as ListItemAdapter
                adapter.submitList(it)
                adapter.notifyDataSetChanged()
            }
        )
    }

    private fun setLayout() {
        binding.listRv.run {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)

            adapter = ListItemAdapter(listViewModel)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listViewModel.onDestroyViewModel()
    }


}