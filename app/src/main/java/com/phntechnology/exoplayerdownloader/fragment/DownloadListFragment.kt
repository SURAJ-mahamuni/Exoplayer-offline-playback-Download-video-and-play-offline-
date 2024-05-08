package com.phntechnology.exoplayerdownloader.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.phntechnology.exoplayerdownloader.R
import com.phntechnology.exoplayerdownloader.adapters.GenericAdapter
import com.phntechnology.exoplayerdownloader.databinding.DownloadListItemBinding
import com.phntechnology.exoplayerdownloader.databinding.FragmentDownloadListBinding
import com.phntechnology.exoplayerdownloader.model.ExoDownloadInfo
import com.phntechnology.exoplayerdownloader.util.BytesConvertor
import com.phntechnology.exoplayerdownloader.util.backPressedHandle
import com.phntechnology.exoplayerdownloader.util.hideView
import com.phntechnology.exoplayerdownloader.util.showView
import com.phntechnology.exoplayerdownloader.viewModels.DownloadListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DownloadListFragment : Fragment() {
    private var _binding: FragmentDownloadListBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<DownloadListViewModel>()

    private var _downloadListAdapter: GenericAdapter<ExoDownloadInfo, DownloadListItemBinding>? =
        null

    private val downloadListAdapter get() = _downloadListAdapter!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getDownloadsFromExoDB(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDownloadListBinding.inflate(layoutInflater, container, false)
        backPressedHandle {
            requireActivity().finishAffinity()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeAdapter()

        observables()

    }


    private fun initializeAdapter() {
        _downloadListAdapter = GenericAdapter(
            bindingInflater = DownloadListItemBinding::inflate,
            onBind = { itemData, itemBinding, position, listSize ->
                itemBinding.apply {
                    try {
                        videoName.text = itemData.uri?.split("/")?.last()?.split(".")?.get(0)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        videoName.text = itemData.uri
                    }
                    progressBar3.progress = itemData.percentDownloaded?.toInt() ?: 0
                    videoSizeCompleteAndRemaining.text = getString(
                        R.string.video_download_size, BytesConvertor.bytesToHumanReadableSize(
                            itemData.bytesDownloaded ?: 0.0
                        ), BytesConvertor.bytesToHumanReadableSize(
                            itemData.contentLength?.toDouble() ?: 0.0
                        )
                    )

                }
            })
        binding.downloadListRv.adapter = downloadListAdapter
    }

    private fun observables() {
        viewModel.downloadListLiveData.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                downloadListAdapter.setData(it)
                downloadListAdapter.notifyDataSetChanged()
                binding.lottie.hideView()
            } else {
                binding.lottie.showView()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}