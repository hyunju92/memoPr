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


class ItemFragment : Fragment() {

    protected lateinit var binding: ItemFragmentBinding
    protected val viewmodel: ItemFragmentViewmodel by lazy {
        ViewModelProvider(this).get(ItemFragmentViewmodel::class.java)
    }

    private var menu: Menu? = null

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
                return false
            }
            R.id.default_menu -> {
                setMenuItem(viewmodel.isEdit())
            }
            R.id.edit -> {
                viewmodel.setType(viewmodel.TYPE_EDIT)
            }
            R.id.save -> {
                viewmodel.save(requireContext(), binding.titleEt.text.toString(), binding.contentsEt.text.toString())
                viewmodel.setType(viewmodel.TYPE_DETAIL)
            }
            R.id.delete -> {
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.item_fragment, container, false)

        setHasOptionsMenu(true)
        viewmodel.memoItem.value = ItemFragmentArgs.fromBundle(arguments!!).memoItem

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerLiveData()
        setLayout()
    }

    private fun observerLiveData() {
        viewmodel.type.observe(this, Observer {
            Log.d("testObsever", "observerLiveData type = " + it.toString())
            setLayoutByType()
        })
    }

    private fun setLayout() {
        // set image recycler view
        val horizontalLayoutManagaer = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.imgRv.setLayoutManager(horizontalLayoutManagaer)

        viewmodel.memoItem.value.let {
            if (it == null) {
                viewmodel.setType(viewmodel.TYPE_EDIT)

            } else {
                viewmodel.setType(viewmodel.TYPE_DETAIL)

                binding.titleEt.setText(it.title)
                binding.contentsEt.setText(it.contents)

            }
        }
    }

    private fun setLayoutByType() {
        val isEdit = viewmodel.isEdit()
        val imgList = viewmodel.getImgList()
        // 텍스트
        binding.titleEt.isEnabled = isEdit
        binding.contentsEt.isEnabled = isEdit

        // 이미지 어댑터
        binding.imgRv.adapter = if (isEdit) {
            EditTypeImgAdapter(imgList)
        } else {
            DetailTypeImgAdapter(imgList)
        }
    }


    override fun onStop() {
        viewmodel.save(requireContext(), binding.titleEt.text.toString(), binding.contentsEt.text.toString())
        super.onStop()
    }


}