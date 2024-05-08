package com.phntechnology.exoplayerdownloader.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.phntechnology.exoplayerdownloader.BaseApplication
import com.phntechnology.exoplayerdownloader.extensions.NetworkExtensions.networkConnectivity
import com.phntechnology.exoplayerdownloader.databinding.FragmentExoplayerBinding
import com.phntechnology.exoplayerdownloader.downloadVideo.ExoDownloadManager
import com.phntechnology.exoplayerdownloader.util.BytesConvertor
import com.phntechnology.exoplayerdownloader.util.backPressedHandle
import com.phntechnology.exoplayerdownloader.util.hideView
import com.phntechnology.exoplayerdownloader.util.showView
import com.phntechnology.exoplayerdownloader.viewModels.ExoplayerViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ExoplayerFragment : Fragment() {
    private var _binding: FragmentExoplayerBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<ExoplayerViewModel>()

    private var _exoPlayer: ExoPlayer? = null

    private val exoPlayer get() = _exoPlayer!!

    private val url =
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"

    @Inject
    lateinit var exoDownloadManager: ExoDownloadManager

    var app: BaseApplication? = null

    var contentId : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as BaseApplication
        viewModel.dataBase = app?.appContainer?.dataBase
        contentId = exoDownloadManager.contentId
        viewModel.getDownloads(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentExoplayerBinding.inflate(layoutInflater, container, false)
        backPressedHandle {
            requireActivity().finishAffinity()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeExoPlayer(url)

        initializeListener()

        observables()
    }

    @OptIn(UnstableApi::class)
    private fun observables() {
        viewModel.downloadListLiveData.observe(viewLifecycleOwner) {
            Log.e("downloads", it.toString())
            Log.e("contentId", "$contentId")
            if (!it.isNullOrEmpty()) {
                binding.button.hideView()
                binding.values.showView()
                viewModel.getCurrentVideoInfo(contentId ?: "")
                    ?.let { exoDownloadInfo ->
                        binding.progressBar2.progress =
                            exoDownloadInfo.percentDownloaded?.toInt() ?: 0
                        binding.downloadedSize.text = BytesConvertor.bytesToHumanReadableSize(exoDownloadInfo.bytesDownloaded ?: 0.0)
                        binding.totalSize.text = BytesConvertor.bytesToHumanReadableSize(exoDownloadInfo.contentLength?.toDouble() ?: 0.0)
                        binding.percentDownload.text = "${exoDownloadInfo.percentDownloaded?.toInt() ?: 0}%"
                    }
            }else{
                binding.button.showView()
                binding.values.hideView()
            }
        }
        viewModel.getCurrentVideoDownloadingStatus(url).observe(viewLifecycleOwner){
            if (!it.isNullOrEmpty()){
                contentId = it[0].contentId
                viewModel.getDownloads(requireContext())
            }
        }
    }

    private fun initializeListener() {
        binding.button.setOnClickListener {
            exoDownloadManager.startDownload(url)
            binding.button.hideView()
            binding.values.showView()
        }
    }

    @OptIn(UnstableApi::class)
    private fun initializeExoPlayer(videoUrl: String?) {
        _exoPlayer =
            ExoPlayer.Builder(requireContext()).setSeekBackIncrementMs(10000)
                .setSeekForwardIncrementMs(10000).build()
        binding.playerView.player = exoPlayer
        videoUrl?.let {
            initializeDataSource(it)
        }
    }

    @OptIn(UnstableApi::class)
    private fun initializeDataSource(url: String) {
        networkConnectivity(
            online = {
                binding.textView.hideView()
                val defaultHttpDataSourceFactory = DefaultHttpDataSource.Factory()
                val mediaItem = MediaItem.fromUri(url)
                val mediaSource = ProgressiveMediaSource.Factory(defaultHttpDataSourceFactory)
                    .createMediaSource(mediaItem)
                exoPlayer.apply {
                    setMediaSource(mediaSource)
                    seekTo(0)
                    playWhenReady = playWhenReady
                    playWhenReady = true
                    prepare()
                }
            }, offline = {
                app?.let { applicationInstance ->
                    val cacheDataSourceFactory: DataSource.Factory =
                        CacheDataSource.Factory()
                            .setCache(applicationInstance.appContainer.downloadCache)
                            .setCacheWriteDataSinkFactory(null) // Disable writing.

                    val mediaSource =
                        ProgressiveMediaSource.Factory(cacheDataSourceFactory)
                            .createMediaSource(MediaItem.fromUri(url))
                    exoPlayer.apply {
                        setMediaSource(mediaSource)
                        seekTo(0)
                        playWhenReady = playWhenReady
                        playWhenReady = true
                        prepare()
                    }
                }

            })

        addListener()
    }

    private fun addListener() {
        exoPlayer.addListener(object : Player.Listener {
            @OptIn(UnstableApi::class)
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                when (playbackState) {
                    Player.STATE_IDLE -> {
                        binding.progressBar.showView()
                    }

                    Player.STATE_BUFFERING -> {
                        binding.run {
                            playerView.hideController()
                            progressBar.showView()
                        }
                    }

                    Player.STATE_READY -> {
                        binding.progressBar.hideView()
                    }

                    Player.STATE_ENDED -> {}
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _exoPlayer?.stop()
        _exoPlayer?.release()
        _exoPlayer = null
        _binding = null
    }
}