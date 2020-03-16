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
import hyunju.com.memo2020.databinding.CaptureDialogFragmentBinding
import hyunju.com.memo2020.util.ImgUtil
import hyunju.com.memo2020.viewmodel.EditFragmentViewmodel

class CaptureFragment : DialogFragment() {
    protected lateinit var binding: CaptureDialogFragmentBinding
    protected val itemFragViewmodel: EditFragmentViewmodel by lazy {
        ViewModelProvider(requireActivity()).get(EditFragmentViewmodel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.capture_dialog_fragment, null, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
    }

    private fun setLayout() {
        setSecreenWebview()
        setUrlLoadLayout()
        setSaveLayout()

    }


    private fun setSecreenWebview() {
        try {
            binding.saveScreenWv.webViewClient = object : WebViewClient() {
                override fun onLoadResource(view: WebView?, url: String?) {
                    view?.loadUrl("javascript:document.getElementsByName(\"viewport\")[0].setAttribute(\"content\", \"width=device-width, initial-scale=1.0, maximum-scale=5.0, user-scalable=yes\");")
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

            val webSetting = binding.saveScreenWv.settings
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

    private fun setUrlLoadLayout() {
        binding.urlLoadBtn.setOnClickListener {
            val imgUrl = binding.urlLoadEt.text.toString()

            binding.saveScreenWv.loadUrl(imgUrl)

            binding.urlLoadCl.visibility = View.GONE
            binding.saveCl.visibility = View.VISIBLE

        }

        binding.urlLoadRemoveBtn.setOnClickListener { binding.urlLoadEt.text = null }

    }

    private fun setSaveLayout() {
        binding.saveRetryBtn.setOnClickListener {
            binding.saveCl.visibility = View.GONE
            binding.urlLoadCl.visibility = View.VISIBLE
        }

        binding.saveBtn.setOnClickListener {
            val savedUri = ImgUtil.getCapturedImgUri(requireContext(), binding.saveScreenWv)

            // share viewmodel to access other fragment
            itemFragViewmodel.addImg(savedUri)
            Navigation.findNavController(it).popBackStack()
        }

    }


    private fun retry() {
        Toast.makeText(context, context?.getString(R.string.fail_img_loading)
                ?: "", Toast.LENGTH_SHORT).show()
    }


}