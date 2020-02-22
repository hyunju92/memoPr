package hyunju.com.memo2020

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import hyunju.com.memo2020.databinding.CaptureImgDialogFragmentBinding
import hyunju.com.memo2020.viewmodel.ItemFragmentViewmodel

class CaptureImgDialogFragment : DialogFragment() {
    protected lateinit var binding: CaptureImgDialogFragmentBinding


//    protected val viewmodel: CaptureImgDialogFragmentViewmodel by lazy {
//        ViewModelProvider(this).get(CaptureImgDialogFragmentViewmodel::class.java)
//    }

    protected val viewmodel: ItemFragmentViewmodel by lazy {
        ViewModelProvider(requireActivity()).get(ItemFragmentViewmodel::class.java)
    }

    var imgUri: String? = null


    companion object {
        private var INSTANCE: CaptureImgDialogFragment? = null

        @Synchronized
        fun get(imgUri: Uri): CaptureImgDialogFragment {
            if (INSTANCE == null) {
                val dialog = CaptureImgDialogFragment()

                // put Args (imgUri)
                return dialog
            }
            return INSTANCE!!
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imgUri = CaptureImgDialogFragmentArgs.fromBundle(arguments!!).imgUri

        // get Args (imgUri)
//        val testUrl = "https://postfiles.pstatic.net/MjAxNzA5MjVfMjcg/MDAxNTA2MzA0Mzc1MTM4.prbIrhy_KnEJAq0I4WGX0yHDuCRhQsbKcHU6RGDVVNog.5o4kxdfkv-bGtI4-gXRwXjh045Yz77L8WnFFOt6PCQIg.JPEG.rla2945/13402575_643830889104534_68262404_n.jpg?type=w580"
//        imgUri = testUrl

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.capture_img_dialog_fragment, null, false)

        Glide.with(binding.screenIv.rootView)
                .load(imgUri)
//                .override(600, 600)
                .fitCenter()
                .into(binding.screenIv)

        binding.saveBtn.setOnClickListener {
            Log.d("testDialog", "uri " + imgUri)
            val savedUri = viewmodel.saveCapture(requireContext(), binding.screenIv)
            viewmodel.moveItemFragment(it, savedUri)
        }

        return binding.root
    }


}