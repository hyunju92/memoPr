package hyunju.com.memo2020.edit.view

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import hyunju.com.memo2020.R
import hyunju.com.memo2020.databinding.EditFragmentBinding
import hyunju.com.memo2020.db.MemoDatabase
import hyunju.com.memo2020.edit.vm.EditUiEvent
import hyunju.com.memo2020.edit.vm.EditViewModel
import hyunju.com.memo2020.model.ImgUriRepository
import hyunju.com.memo2020.model.MemoRepository
import hyunju.com.memo2020.model.PrefRepository
import hyunju.com.memo2020.util.replaceAll
import io.reactivex.rxjava3.disposables.Disposable


class EditFragment : Fragment() {

    private lateinit var editViewModel: EditViewModel
    private lateinit var binding: EditFragmentBinding
    private var eventDisposable: Disposable? = null


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.edit_frag_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home, R.id.save -> save()
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            val memoRepository = MemoRepository(MemoDatabase.get(it.applicationContext))
            val imageUriRepository = ImgUriRepository(it.applicationContext)
            val preferenceRepository = PrefRepository(it.applicationContext)

            editViewModel = EditViewModel(memoRepository, imageUriRepository, preferenceRepository)
        }
        initData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
         binding = DataBindingUtil.inflate<EditFragmentBinding>(inflater, R.layout.edit_fragment, container, false).apply {
            memo = editViewModel.memoItem.value
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLiveData()
        initView()
    }

    private fun observeLiveData() {
        // observe imgList (changed when editing)
        editViewModel.imgList.observe(viewLifecycleOwner, {
            binding.imgRv.replaceAll(it)
        })
        eventDisposable = editViewModel.uiEvent.subscribe {
            handleUiEvent(it)
        }

    }

    private fun initData() {
        EditFragmentArgs.fromBundle(requireArguments()).memoItem.let {
            editViewModel.setMemoItem(it)
        }
    }

    private fun handleUiEvent(uiEvent: EditUiEvent) = when(uiEvent) {
        EditUiEvent.MoveListFragment -> moveListFragment()
        is EditUiEvent.ShowToast -> showToast(uiEvent.msg)
        is EditUiEvent.StartActivityForImgUri -> startActivityForImgUri(uiEvent.requestCode, uiEvent.intent)
    }

    private fun initView() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                save()
            }
        })

        binding.imgRv.run {
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = EditImgAdapter(editViewModel)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        editViewModel.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        editViewModel.onDestroyViewModel()
        eventDisposable?.dispose()
    }

    private fun save() {
        editViewModel.save(binding.titleEt.text.toString(), binding.contentsEt.text.toString())
    }

    private fun showToast(msg : String) {
        context?.let { Toast.makeText(it, msg, Toast.LENGTH_SHORT).show() }
    }

    private fun startActivityForImgUri(requestCode: Int, intent: Intent) {
        startActivityForResult(intent, requestCode)
    }

    private fun moveListFragment() {
        Navigation.findNavController(requireActivity(), R.id.main_fragment).navigateUp()
    }

}

