package hyunju.com.memo2020.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import hyunju.com.memo2020.R
import hyunju.com.memo2020.databinding.ItemFragmentBinding
import hyunju.com.memo2020.viewmodel.ItemFragmentViewmodel


class ItemFragment : Fragment(), EditModeImgAdapter.OnItemBtnClickListener {

    protected lateinit var binding: ItemFragmentBinding
    protected val viewmodel: ItemFragmentViewmodel by lazy {
        ViewModelProvider(requireActivity()).get(ItemFragmentViewmodel::class.java)
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
//                viewmodel.currentMode.value = viewmodel.EDIT_MODE
                viewmodel.setCurrentMode(viewmodel.EDIT_MODE)
            }

            R.id.save -> {
                viewmodel.save(requireContext(),
                        binding.titleEt.text.toString(), binding.contentsEt.text.toString())
                viewmodel.currentMode.value = viewmodel.DETAIL_MODE


            }
            R.id.delete -> {
                viewmodel.delete(requireContext())

                isDeleteAction = true
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
        // receive arg (a memo item) from previous frag
        ItemFragmentArgs.fromBundle(arguments!!).initMode.let {
            //            viewmodel.currentMode.value = it

            viewmodel.setCurrentMode(it)

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
        viewmodel.currentMode.observe(this, Observer {
            // observe when mode status changed
            Log.d("testImgList", "Observer currentmode = " + it)
            setLayoutByMode()
        })

        viewmodel.imgList.observe(this, Observer {
            val imgList = it

            binding.imgRv.adapter =
                    if (viewmodel.isEditMode()) {
                        EditModeImgAdapter(imgList).apply {
                            this.setOnItemBtnClickListener(this@ItemFragment)
                        }
                    } else {
                        DetailModeImgAdapter(imgList) // just view img list
                    }


            Log.d("testImgListobserve", "remove " + viewmodel.imgList.value!!.size)
        })

    }

    private fun setLayoutByMode() {
        val isEditMode = viewmodel.isEditMode()
        val imgList = viewmodel.imgList.value!!

        // set editable mode
        binding.titleEt.isEnabled = isEditMode
        binding.contentsEt.isEnabled = isEditMode


        // set img list adapter
        binding.imgRv.adapter = if (isEditMode) {
            EditModeImgAdapter(imgList).apply {
                this.setOnItemBtnClickListener(this@ItemFragment)
            }
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
//        viewmodel.currentMode.value = viewmodel.firstInitMode
        Log.d("testImgList", "setLayout mode " + viewmodel.currentMode.value)

    }


    override fun onStop() {
        if (!isDeleteAction) {
            // save item excluding from deleting
            viewmodel.save(requireContext(),
                    binding.titleEt.text.toString(), binding.contentsEt.text.toString())
        }
        super.onStop()
    }


    val REQ_PICK_FROM_ALBUM = 1000
    val REQ_PICK_FROM_CAMERA = 1001
    val REQ_PICK_FROM_URL = 1002

    // EditModeImgAdapter.OnItemBtnClickListener
    override fun onItemBtnClick(v: View, postion: Int, request: Int) {
        Log.d("testItemonItemBtnClick", "postion " + postion)
        Log.d("testItemonItemBtnClick", "request " + request)

        when (request) {
            R.id.delete_btn_edit_img -> {
                val tempList: ArrayList<String> = viewmodel.imgList.value!!
                tempList.removeAt(postion)
                viewmodel.imgList.value = tempList
            }

            R.id.uri_btn_edit_img -> {
                Log.d("testItemonItemBtnClick", "init uri_btn_edit_img ")
                viewmodel.moveCaptureImgDialogFrag(view!!, "testUrl")
            }

            R.id.camera_btn_edit_img -> {
                viewmodel.getImgByReqcode(requireActivity(), requireContext(), request)
            }

            R.id.album_btn_edit_img -> {
                viewmodel.getImgByReqcode(requireActivity(), requireContext(), request)
            }
        }
    }
}

