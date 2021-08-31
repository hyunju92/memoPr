package hyunju.com.memo2020.list.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hyunju.com.memo2020.R
import hyunju.com.memo2020.databinding.ListFragmentBinding
import hyunju.com.memo2020.db.MemoDatabase
import hyunju.com.memo2020.list.vm.ListUiEvent
import hyunju.com.memo2020.list.vm.ListViewModel
import hyunju.com.memo2020.model.Memo
import hyunju.com.memo2020.model.Repository
import io.reactivex.rxjava3.disposables.Disposable

class ListFragment : Fragment() {
    private lateinit var binding: ListFragmentBinding
    private lateinit var listViewModel: ListViewModel
    private var eventDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            val repository = Repository(MemoDatabase.get(it.applicationContext), it.applicationContext)
            listViewModel = ListViewModel(repository)
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

        eventDisposable = listViewModel.uiEvent.subscribe {
            handelUiEvent(it)
        }
    }

    private fun handelUiEvent(uiEvent: ListUiEvent) = when(uiEvent) {
        is ListUiEvent.MoveEditFragment -> moveEditFragment(uiEvent.memoItem)
        is ListUiEvent.ShowSelectDialog -> showSelectDialog(uiEvent.memoItem)
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
        eventDisposable?.dispose()
    }

    private fun moveEditFragment(memoItem: Memo? = null) {
        val action = ListFragmentDirections.actionListFragmentToEditFragment(memoItem)
        requireActivity().findNavController(R.id.main_fragment).navigate(action)
    }

    private fun showSelectDialog(memoItem: Memo?) {
        val dialogView = requireActivity().layoutInflater.inflate(R.layout.select_dialog, null)

        val builder = AlertDialog.Builder(requireActivity())
        val dialog = builder.setView(dialogView).create()

        val deleteBtn = dialogView.findViewById<ImageButton>(R.id.select_dialog_delete)
        val editBtn = dialogView.findViewById<ImageButton>(R.id.select_dialog_edit)

        deleteBtn.setOnClickListener {
            dialog.dismiss()
            listViewModel.delete(requireActivity(), memoItem!!)
        }
        editBtn.setOnClickListener {
            dialog.dismiss()
            moveEditFragment(memoItem)
        }

        dialog.show()
    }
}