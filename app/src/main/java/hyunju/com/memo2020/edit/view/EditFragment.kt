package hyunju.com.memo2020.edit.view

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import hyunju.com.memo2020.R
import hyunju.com.memo2020.databinding.EditFragmentBinding
import hyunju.com.memo2020.db.MemoDatabase
import hyunju.com.memo2020.edit.vm.EditViewModel
import hyunju.com.memo2020.model.MemoRepository
import hyunju.com.memo2020.replaceAll


class EditFragment : Fragment() {

    private lateinit var binding: EditFragmentBinding
    private lateinit var editViewModel: EditViewModel

    private var menu: Menu? = null

    // set toolbar menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_frag_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                save()
                return true
            }
            R.id.save -> save()

        }
        return true
    }

    // fragment lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context?.let {
            val repository = MemoRepository(MemoDatabase.get(it.applicationContext))
            editViewModel = EditViewModel(repository, it.applicationContext, this@EditFragment)
        }

        // receive arg (a memo item) from previous frag
        EditFragmentArgs.fromBundle(requireArguments()).memoItem.let { editViewModel.setMemoItem(it) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate<EditFragmentBinding>(inflater, R.layout.edit_fragment, container, false).apply {
            memo = editViewModel.memoItem.value
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                save()
            }
        })

        observeLiveData()
        setLayout()
    }

    private fun observeLiveData() {
        // observe imgList (changed when editing)
        editViewModel.imgList.observe(viewLifecycleOwner, {
            binding.imgRv.replaceAll(it)
        })
    }

    private fun setLayout() {
        // set img recycler view
        binding.imgRv.run {
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = EditImgAdapter(editViewModel)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        context?.let { editViewModel.onActivityResult(it.applicationContext, requestCode, resultCode, data) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        editViewModel.onDestroyViewModel()
    }

    private fun save() {
        editViewModel.save(requireActivity(), this,
                binding.titleEt.text.toString(), binding.contentsEt.text.toString())
    }


}

