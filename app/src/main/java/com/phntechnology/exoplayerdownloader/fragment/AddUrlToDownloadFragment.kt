package com.phntechnology.exoplayerdownloader.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.phntechnology.exoplayerdownloader.R
import com.phntechnology.exoplayerdownloader.databinding.FragmentAddUrlToDownloadBinding
import com.phntechnology.exoplayerdownloader.downloadVideo.ExoDownloadManager
import com.phntechnology.exoplayerdownloader.util.backPressedHandle
import com.phntechnology.exoplayerdownloader.util.isValidName
import com.phntechnology.exoplayerdownloader.util.isValidVideoUrl
import com.phntechnology.exoplayerdownloader.util.textChange
import com.phntechnology.exoplayerdownloader.util.toastMsg
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddUrlToDownloadFragment : Fragment() {
    private var _binding: FragmentAddUrlToDownloadBinding? = null

    private val binding get() = _binding!!

    @Inject
    lateinit var exoDownloadManager: ExoDownloadManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddUrlToDownloadBinding.inflate(layoutInflater, container, false)
        backPressedHandle {
            requireActivity().finishAffinity()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeListener()

        validateInput()

    }

    private fun validateInput() {
        binding.edtVideoUrl.textChange {
            binding.tilVideoUrl.error =
                if (isValidName(it) == null) if (isValidVideoUrl(it)) null else "please enter valid url" else isValidName(it, "Please enter url")
        }
    }

    private fun initializeListener() {
        binding.tilVideoUrl.setEndIconOnClickListener {
            val url = binding.edtVideoUrl.text.toString().trim()
            if (isValidName(url) == null) {
                if (isValidVideoUrl(url)) exoDownloadManager.startDownload(url) else toastMsg("please enter valid url")
            } else toastMsg(
                isValidName(
                    url,
                    "Please enter url"
                ) ?: ""
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}