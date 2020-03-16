package hyunju.com.memo2020.view

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import hyunju.com.memo2020.R
import hyunju.com.memo2020.databinding.ViewFragmentBinding
import hyunju.com.memo2020.view.adapter.ViewImgAdapter
import hyunju.com.memo2020.viewmodel.ViewFragmentViewmodel


class ViewFragment : Fragment() {

    protected lateinit var binding: ViewFragmentBinding
    protected val vm: ViewFragmentViewmodel by lazy {
        ViewModelProvider(requireActivity()).get(ViewFragmentViewmodel::class.java)
    }

    private var menu: Menu? = null


    // set toolbar menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.view_frag_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // prevent parsing toolbar back btn
            android.R.id.home -> return false
            R.id.delete -> vm.delete(requireActivity())
            R.id.edit -> vm.moveEditFragment(this.view!!)

        }
        return true
    }


    // fragment lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // receive arg (a memo item) from previous frag
        ViewFragmentArgs.fromBundle(arguments!!).memoItem.let { vm.setMemoItem(it) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.view_fragment, container, false)

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observerLiveData()
        setLayout()
    }

    private fun observerLiveData() {
        vm.memoItem.observe(this, Observer {
            setTextview()
        })

        // observe imgList (changed when editing)
        vm.imgList.observe(this, Observer {
            setimgRv()
        })
    }


    private fun setLayout() {
        // set img recycler view
        setimgRv()

        // set text contents
        setTextview()

    }

    private fun setimgRv() {
        val horizonLayoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.imgRv.setLayoutManager(horizonLayoutManager)
        binding.imgRv.adapter = ViewImgAdapter(vm.imgList.value!!)

        (binding.imgRv.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
    }

    private fun setTextview() {
        binding.titleEt.setText(vm.memoItem.value?.title ?: "")
        binding.dateEt.setText(vm.memoItem.value?.getDateText(requireContext()))
        binding.contentsEt.setText(vm.memoItem.value?.contents ?: "")
    }


}

