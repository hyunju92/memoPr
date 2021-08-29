package hyunju.com.memo2020.edit.view

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import hyunju.com.memo2020.R
import hyunju.com.memo2020.databinding.EditFragmentBinding
import hyunju.com.memo2020.edit.vm.EditFragmentViewmodel
import hyunju.com.memo2020.model.getDateText


class EditFragment : Fragment() {

    private lateinit var binding: EditFragmentBinding
    private lateinit var vm: EditFragmentViewmodel

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
        context?.let { vm = EditFragmentViewmodel(it.applicationContext) }

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


        binding.imgRv.adapter = getAdapter(vm.imgList.value?:ArrayList())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                save()
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        context?.let { vm.onActivityResult(it.applicationContext, requestCode, resultCode, data) }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        vm.onDestroyViewModel()
    }

    private fun save() {
        vm.save(requireActivity(),
                binding.titleEt.text.toString(), binding.contentsEt.text.toString())
    }

    private fun getAdapter(imgList: ArrayList<String>): EditImgAdapter {
        return EditImgAdapter(imgList, object : (View, Int) -> Unit {
            override fun invoke(view: View, position: Int) {
                vm.onEditAdapterItemClickEvent(requireContext(), this@EditFragment, view, position, view.id)
            }
        })
    }

}

