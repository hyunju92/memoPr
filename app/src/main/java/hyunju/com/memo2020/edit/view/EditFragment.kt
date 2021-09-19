package hyunju.com.memo2020.edit.view

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import hyunju.com.memo2020.R
import hyunju.com.memo2020.databinding.EditFragmentBinding
import hyunju.com.memo2020.edit.vm.EditUiEvent
import hyunju.com.memo2020.edit.vm.EditViewModel
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject
import androidx.activity.result.contract.ActivityResultContracts


@AndroidEntryPoint
class EditFragment @Inject constructor(): Fragment() {

    @Inject lateinit var editViewModel: EditViewModel
    private lateinit var binding: EditFragmentBinding
    private var eventDisposable: Disposable? = null

    private val requestAlbum = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) editViewModel.onAlbumResult(it)
    }
    private val requestCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) editViewModel.onCameraResult()
    }

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
        is EditUiEvent.StartAlbum -> requestAlbum.launch(uiEvent.intent)
        is EditUiEvent.StartCamera -> requestCamera.launch(uiEvent.intent)
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

    override fun onDestroyView() {
        super.onDestroyView()
        editViewModel.onDestroyViewModel()
        eventDisposable?.dispose()
    }

    private fun save() {
        editViewModel.save(binding.titleEt.text.toString(),
            binding.contentsEt.text.toString(),
            (binding.imgRv.adapter as EditImgAdapter).getItemList())
    }

    private fun showToast(msg : String) {
        context?.let { Toast.makeText(it, msg, Toast.LENGTH_SHORT).show() }
    }

    private fun moveListFragment() {
        Navigation.findNavController(requireActivity(), R.id.main_fragment).navigateUp()
    }

}

