package hyunju.com.memo2020.view

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import hyunju.com.memo2020.R
import hyunju.com.memo2020.databinding.ItemFragmentBinding
import hyunju.com.memo2020.view.adapter.DetailModeImgAdapter
import hyunju.com.memo2020.view.adapter.EditModeImgAdapter
import hyunju.com.memo2020.viewmodel.ItemFragmentViewmodel


class ItemFragment : Fragment(), EditModeImgAdapter.OnItemBtnClickListener {

    protected lateinit var binding: ItemFragmentBinding
    protected val viewmodel: ItemFragmentViewmodel by lazy {
        ViewModelProvider(requireActivity()).get(ItemFragmentViewmodel::class.java)
    }

    private var menu: Menu? = null


    // set toolbar menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.item_frag_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // prevent parsing toolbar back btn
            android.R.id.home -> return false

            // when default clicked, set inner menu by mode status
            R.id.default_menu -> setMenuItem(viewmodel.isEditMode())


            // * (real) inner menu item 3
            R.id.edit -> {
                viewmodel.currentMode.value = viewmodel.EDIT_MODE
                if (!viewmodel.isEdittedAtLeastOnce) viewmodel.isEdittedAtLeastOnce = true
            }

            R.id.save -> {
//                viewmodel.save(requireContext(),
//                        binding.titleEt.text.toString(), binding.contentsEt.text.toString())
//                Navigation.findNavController(requireActivity(), R.id.main_fragment).navigateUp()

                viewmodel.currentMode.value = viewmodel.DETAIL_MODE
            }

            R.id.delete -> {
                viewmodel.isNeedToSaveIfFinished = false
                viewmodel.delete(requireContext())
                Navigation.findNavController(requireActivity(), R.id.main_fragment).navigateUp()
            }
        }
        return true
    }

    fun setMenuItem(isEdit: Boolean) {
        menu!!.findItem(R.id.save).isVisible = isEdit
        menu!!.findItem(R.id.delete).isVisible = !isEdit
        menu!!.findItem(R.id.edit).isVisible = !isEdit
    }


    // fragment lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // receive arg (a memo item) from previous frag
        ItemFragmentArgs.fromBundle(arguments!!).memoItem.let {
            viewmodel.setMemoItem(it)
        }
        // receive arg (mode) from previous frag
        ItemFragmentArgs.fromBundle(arguments!!).initMode.let {
            viewmodel.currentMode.value = it
            viewmodel.isEdittedAtLeastOnce = it == viewmodel.EDIT_MODE
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.item_fragment, container, false)

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observerLiveData()
        setLayout()
    }

    private fun observerLiveData() {
        // observe mode status
        viewmodel.currentMode.observe(this, Observer {
            setLayoutByMode()
        })

        // observe imgList (changed when editing)
        viewmodel.imgList.observe(this, Observer {
            viewmodel.isNeedToSaveIfFinished = true
            setImgRvAdapter(viewmodel.isEditMode(), it)
        })
    }

    private fun setLayout() {
        // set img recycler view
        val horizonLayoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.imgRv.setLayoutManager(horizonLayoutManager)

        // set text contents
        binding.titleEt.setText(viewmodel.memoItem.value?.title ?: "")
        binding.dateEt.setText(viewmodel.memoItem.value?.getDateText())
        binding.contentsEt.setText(viewmodel.memoItem.value?.contents ?: "")
    }

    private fun setLayoutByMode() {
        val isEditMode = viewmodel.isEditMode()

        // set editable mode
        binding.titleEt.isEnabled = isEditMode
        binding.contentsEt.isEnabled = isEditMode

        // set img list adapter
        setImgRvAdapter(isEditMode, viewmodel.imgList.value!!)
    }

    private fun setImgRvAdapter(isEditMode: Boolean, imgList: ArrayList<String>) {
        binding.imgRv.adapter = if (isEditMode) {
            // adapter for editing
            EditModeImgAdapter(imgList).apply {
                this.setOnItemBtnClickListener(this@ItemFragment)
            }
        } else {
            // adapter just show image
            DetailModeImgAdapter(imgList)
        }
    }


    // EditModeImgAdapter.OnItemBtnClickListener
    override fun onItemBtnClick(v: View, postion: Int, requestBtnId: Int) {
        viewmodel.isNeedToSaveIfFinished = false
        viewmodel.onEditAdapterItemClickEvent(requireContext(), requireActivity(),
                v, postion, requestBtnId)

    }


    override fun onStop() {
        super.onStop()
        if (viewmodel.isNeedToSaveIfFinished && viewmodel.isEdittedAtLeastOnce) {
            //  save memo
            viewmodel.save(requireContext(), binding.titleEt.text.toString(), binding.contentsEt.text.toString())
        }
    }

}

