package hyunju.com.memo2020.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import hyunju.com.memo2020.R
import hyunju.com.memo2020.databinding.ItemFragmentBinding
import hyunju.com.memo2020.viewmodel.ItemFragmentViewmodel


class ItemFragment : Fragment() {

    protected lateinit var binding: ItemFragmentBinding
    protected val viewmodel: ItemFragmentViewmodel by lazy {
        ViewModelProvider(this).get(ItemFragmentViewmodel::class.java)
    }

    private var menu: Menu? = null
    private var isDeleteAction: Boolean = false


    // menu setting
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.item_frag_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

        this.menu = menu
        Log.d("testMenCre", "onCreateOptionsMenu = " + android.R.id.home)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("testOtpItem", "item = " + item.itemId)
        Log.d("testOtpItem", "item = " + android.R.id.home)

        when (item.itemId) {
            // prevent parsing toolbar back btn
            android.R.id.home -> {
                return false
            }

            // when default clicked, set inner menu by mode status
            R.id.default_menu -> {
                setMenuItem(viewmodel.isEditMode())
            }


            // * (real) inner menu item 3
            R.id.edit -> {
                viewmodel.changeViewMode(viewmodel.EDIT_MODE)
            }
            R.id.save -> {
                viewmodel.save(requireContext(),
                        binding.titleEt.text.toString(), binding.contentsEt.text.toString())
                viewmodel.changeViewMode(viewmodel.DETAIL_MODE)
            }
            R.id.delete -> {
//                viewmodel.delete(requireContext())
//
//                isDeleteAction = true
//                Navigation.findNavController(requireActivity(), R.id.main_fragment).navigateUp()

                viewmodel.moveCaptureImgDialogFrag(view!!, "testUrl")
            }

        }
        return true
    }

    fun setMenuItem(isEdit: Boolean) {
        menu!!.findItem(R.id.save).isVisible = isEdit
        menu!!.findItem(R.id.delete).isVisible = !isEdit
        menu!!.findItem(R.id.edit).isVisible = !isEdit
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // receive arg (a memo item) from previous frag
        ItemFragmentArgs.fromBundle(arguments!!).memoItem.let {
            viewmodel.memoItem.value = it
        }
    }

    // fragment lifecycle
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
        viewmodel.mode.observe(this, Observer {
            // observe when mode status changed
            setLayoutByMode()
        })

    }

    private fun setLayoutByMode() {
        val isEditMode = viewmodel.isEditMode()
        val imgList = viewmodel.getImgList()

        // set editable mode
        binding.titleEt.isEnabled = isEditMode
        binding.contentsEt.isEnabled = isEditMode

        // set img list adapter
        binding.imgRv.adapter = if (isEditMode) {
            EditModeImgAdapter(imgList) // can edit img list
        } else {
            DetailModeImgAdapter(imgList) // just view img list
        }

    }

    private fun setLayout() {
        // set img recycler view
        val horizonLayoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.imgRv.setLayoutManager(horizonLayoutManager)

        // set text contents
        binding.titleEt.setText(viewmodel.memoItem.value?.title ?: "")
        binding.contentsEt.setText(viewmodel.memoItem.value?.contents ?: "")

        // set mode
        val mode = if (viewmodel.memoItem.value == null) viewmodel.EDIT_MODE else viewmodel.DETAIL_MODE
        viewmodel.changeViewMode(mode)

    }


    override fun onStop() {
        if (!isDeleteAction) {
            // save item excluding from deleting
            viewmodel.save(requireContext(),
                    binding.titleEt.text.toString(), binding.contentsEt.text.toString())
        }
        super.onStop()
    }


}