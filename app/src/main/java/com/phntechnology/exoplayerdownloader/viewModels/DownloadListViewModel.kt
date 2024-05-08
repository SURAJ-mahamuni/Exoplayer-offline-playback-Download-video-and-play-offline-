package com.phntechnology.exoplayerdownloader.viewModels


import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.OptIn
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.room.util.getColumnIndex
import com.phntechnology.exoplayerdownloader.database.ExoDownloadDao
import com.phntechnology.exoplayerdownloader.model.ExoDownloadInfo
import com.phntechnology.exoplayerdownloader.model.ExoDownloadingHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class DownloadListViewModel @Inject constructor(
    private val dao: ExoDownloadDao
) : ViewModel() {

    var dataBase: StandaloneDatabaseProvider? = null

    val downloadListLiveData = MutableLiveData<ArrayList<ExoDownloadInfo>>()

    @SuppressLint("RestrictedApi")
    @OptIn(UnstableApi::class)
    fun getDownloadsFromExoDB(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            var flag = ""
            val downloadList = mutableListOf<ExoDownloadInfo>()
            var key_id: String?
            try {
                dataBase?.readableDatabase?.let { db ->
                    val query = "SELECT * FROM ExoPlayerDownloads"
                    val cursor = db.rawQuery(query, null)

                    while (cursor.moveToNext()) {
                        val id = cursor.getString(getColumnIndex(cursor, "id"))
                        key_id = id
                        val mime_type = cursor.getString(getColumnIndex(cursor, "mime_type"))
                        val uri = cursor.getString(getColumnIndex(cursor, "uri"))
                        val stream_keys = cursor.getString(getColumnIndex(cursor, "stream_keys"))
                        val custom_cache_key =
                            cursor.getString(getColumnIndex(cursor, "custom_cache_key"))
                        val data = cursor.getBlob(getColumnIndex(cursor, "data"))
                        val state = cursor.getInt(getColumnIndex(cursor, "state"))
                        val start_time_ms = cursor.getInt(getColumnIndex(cursor, "start_time_ms"))
                        val update_time_ms = cursor.getInt(getColumnIndex(cursor, "update_time_ms"))
                        val content_length = cursor.getInt(getColumnIndex(cursor, "content_length"))
                        val stop_reason = cursor.getInt(getColumnIndex(cursor, "stop_reason"))
                        val failure_reason = cursor.getInt(getColumnIndex(cursor, "failure_reason"))
                        val percent_downloaded =
                            cursor.getDouble(getColumnIndex(cursor, "percent_downloaded"))
                        val bytes_downloaded =
                            cursor.getDouble(getColumnIndex(cursor, "bytes_downloaded"))
                        val key_set_id = cursor.getBlob(getColumnIndex(cursor, "key_set_id"))
                        val exoDownloadInfo = ExoDownloadInfo(
                            id = id,
                            mimeType = mime_type,
                            uri = uri,
                            streamKeys = stream_keys,
                            customCacheKey = custom_cache_key,
                            data = data,
                            state = state,
                            startTimeMs = start_time_ms,
                            updateTimeMs = update_time_ms,
                            contentLength = content_length,
                            stopReason = stop_reason,
                            failureReason = failure_reason,
                            percentDownloaded = percent_downloaded,
                            bytesDownloaded = bytes_downloaded,
                            keySetId = key_set_id
                        )
                        downloadList.add(exoDownloadInfo)
                        downloadListLiveData.postValue(ArrayList(downloadList))
                        if (percent_downloaded == 100.0) {
                            flag = "out"
                        }
                    }
                    cursor.close()
                    db.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            delay(500)
            if (flag == "") {
                getDownloadsFromExoDB(context)
            }
            return@launch
        }
    }


}