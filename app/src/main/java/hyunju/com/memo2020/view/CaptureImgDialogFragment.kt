package hyunju.com.memo2020.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import hyunju.com.memo2020.R
import hyunju.com.memo2020.databinding.CaptureImgDialogFragmentBinding
import hyunju.com.memo2020.util.ImgUtil
import hyunju.com.memo2020.viewmodel.ItemFragmentViewmodel

class CaptureImgDialogFragment : DialogFragment() {
    protected lateinit var binding: CaptureImgDialogFragmentBinding
    protected val itemFragViewmodel: ItemFragmentViewmodel by lazy {
        ViewModelProvider(requireActivity()).get(ItemFragmentViewmodel::class.java)
    }

    companion object {
        private var INSTANCE: CaptureImgDialogFragment? = null

        @Synchronized
        fun get(): CaptureImgDialogFragment {
            if (INSTANCE == null) {
                return CaptureImgDialogFragment()
            }
            return INSTANCE!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.capture_img_dialog_fragment, null, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
    }

    private fun setLayout() {
        setSecreenWebview()
        binding.inputBtn.setOnClickListener {
            val imgUrl = binding.urlInputEt.text.toString()

            binding.screenWv.loadUrl(imgUrl)

            binding.urlInputCl.visibility = View.GONE
            binding.saveCl.visibility = View.VISIBLE

        }

        binding.removeBtn.setOnClickListener {
            binding.urlInputEt.text = null
        }

        binding.retryBtn.setOnClickListener {
            binding.saveCl.visibility = View.GONE
            binding.urlInputCl.visibility = View.VISIBLE
        }

        binding.saveBtn.setOnClickListener {
            val savedUri = ImgUtil.getCapturedImgUri(requireContext(), binding.screenWv)

            // share viewmodel to access other fragment
            itemFragViewmodel.addImgList(savedUri)
            Navigation.findNavController(it).popBackStack()
        }

    }

    private fun setSecreenWebview() {
        try {
            binding.screenWv.webViewClient = object : WebViewClient() {
                override fun onLoadResource(view: WebView?, url: String?) {
                    view?.loadUrl("javascript:document.getElementsByName(\"viewport\")[0].setAttribute(\"content\", \"width=device-width, initial-scale=1.0, maximum-scale=5.0, user-scalable=yes\");");
                    super.onLoadResource(view, url)
                }

                override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                    super.onReceivedError(view, request, error)
                    retry()
                }

                override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
                    super.onReceivedHttpError(view, request, errorResponse)
                    retry()
                }
            }

            val webSetting = binding.screenWv.settings
            webSetting.javaScriptEnabled = true

            webSetting.setSupportZoom(true)
            webSetting.displayZoomControls = true
            webSetting.builtInZoomControls = true

            webSetting.setLoadWithOverviewMode(true)
            webSetting.setUseWideViewPort(true)


        } catch (e: Exception) {
            retry()
            Log.d("testImgLoadExce", "e " + e)

        }
    }

    private fun retry() {
        Toast.makeText(context, "이미지를 가져오는데에 실패하였습니다.", Toast.LENGTH_SHORT)
    }


}