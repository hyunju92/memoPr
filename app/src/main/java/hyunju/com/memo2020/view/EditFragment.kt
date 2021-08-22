package hyunju.com.memo2020.view

import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import hyunju.com.memo2020.R
import hyunju.com.memo2020.databinding.EditFragmentBinding
import hyunju.com.memo2020.view.adapter.EditImgAdapter
import hyunju.com.memo2020.viewmodel.EditFragmentViewmodel


class EditFragment : Fragment() {

    protected lateinit var binding: EditFragmentBinding
    protected val vm: EditFragmentViewmodel by lazy {
        ViewModelProvider(requireActivity()).get(EditFragmentViewmodel::class.java)
    }

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

        // receive arg (a memo item) from previous frag
        EditFragmentArgs.fromBundle(arguments!!).memoItem.let {
            vm.setMemoItem(it)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.edit_fragment, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observerLiveData()
        setLayout()
    }

    private fun observerLiveData() {
        // observe imgList (changed when editing)
        vm.imgList.observe(this, Observer {
            binding.imgRv.adapter = getAdapter(it)
            binding.imgRv.scrollToPosition(vm.imgPosition ?: 0)
        })
    }

    private fun setLayout() {
        // set img recycler view
        val horizonLayoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.imgRv.setLayoutManager(horizonLayoutManager)

        // set text contents
        binding.titleEt.setText(vm.memoItem.value?.title ?: "")
        binding.titleEt.requestFocus()
        binding.dateEt.setText(vm.memoItem.value?.getDateText(requireContext()))
        binding.contentsEt.setText(vm.memoItem.value?.contents ?: "")


        binding.imgRv.adapter = getAdapter(vm.imgList.value!!)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                save()
            }
        })
    }


    private fun save() {
        vm.save(requireActivity(),
                binding.titleEt.text.toString(), binding.contentsEt.text.toString())
    }

    private fun getAdapter(imgList: ArrayList<String>): EditImgAdapter {
        return EditImgAdapter(imgList, object : (View, Int) -> Unit {
            override fun invoke(view: View, position: Int) {
                vm.onEditAdapterItemClickEvent(requireContext(), requireActivity(), view, position, view.id, vm.memoItem.value?.id?.toString() + "_$position")
            }
        })
    }

}

