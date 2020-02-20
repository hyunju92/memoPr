package hyunju.com.memo2020.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import hyunju.com.memo2020.R
import hyunju.com.memo2020.databinding.DetailFragmentBinding
import hyunju.com.memo2020.model.Memo
import hyunju.com.memo2020.viewmodel.DetailFragmentViewmodel


class DetailFragment : Fragment() {

    protected lateinit var binding: DetailFragmentBinding
    protected val detailFragViewmodel: DetailFragmentViewmodel by lazy {
        ViewModelProvider(this).get(DetailFragmentViewmodel::class.java)
    }

    val memoItem: Memo? by lazy {
        DetailFragmentArgs.fromBundle(arguments!!).memoItem
    }

    private var menu: Menu? = null
    val imgList: ArrayList<String>? by lazy {
        ArrayList<String>().let {
            if (memoItem != null) {
                it.addAll(memoItem!!.getImageList())
            }
            it
        }
    }


//    val itemId: Long by lazy {
//        DetailFragmentArgs.fromBundle(arguments!!).itemId
//    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

//        val backPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(
//                true // default to enabled
//        ) {
//            override fun handleOnBackPressed() {
//                changeEditMode(TEXT_LAYOUT)
//                Log.d("testNav", "detail onattach")
//            }
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.detail_fragment, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("testNav", "memo id " + memoItem?.id)
        observerLiveData()
        setLayout()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail_frag_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

        this.menu = menu
        Log.d("testMenCre", "onCreateOptionsMenu = " + android.R.id.home)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("testOtpItem", "item = " + item.itemId)
        Log.d("testOtpItem", "item = " + android.R.id.home)

        when (item.itemId) {
            android.R.id.home -> {
                changeEditMode(null)
            }
        }

//        return super.onOptionsItemSelected(item)
        return true
    }


    private fun observerLiveData() {


    }

    private fun setLayout() {
        // set image recycler view
        val horizontalLayoutManagaer = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.imgRv.setLayoutManager(horizontalLayoutManagaer)

        if (memoItem != null) {
            DetailImgAdapter(memoItem!!.getImageList()).let {
                binding.imgRv.adapter = it
            }

            // set title, contents
            binding.titleEt.setText(memoItem!!.title)
            binding.contentsEt.setText(memoItem!!.contents)

        } else {
            changeEditMode(true)
            Log.d("testMenCre", "setLayout  changeEditMode")

        }

    }


    private fun changeEditMode(isEdit: Boolean?) {
        val isEditType = isEdit ?: !binding.titleEt.isEnabled

        // 텍스트
        binding.titleEt.isEnabled = isEditType
        binding.contentsEt.isEnabled = isEditType

        // 이미지 어댑터
        binding.imgRv.adapter = if (isEditType) {
            EditImgAdapter(imgList!!)
        } else {
            DetailImgAdapter(imgList!!)
        }

        // 메뉴
        if (menu != null) {
            menu!!.findItem(R.id.save).isVisible = isEditType
            menu!!.findItem(R.id.delete).isVisible = !isEditType
            menu!!.findItem(R.id.edit).isVisible = !isEditType
        }
    }


}