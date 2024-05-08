package com.phntechnology.exoplayerdownloader.model


data class ExoDownloadInfo(
    val id: String? = null,
    val mimeType: String? = null,
    val uri: String? = null,
    val streamKeys: String? = null,
    val customCacheKey: String? = null,
    val data: ByteArray? = byteArrayOf(),
    val state: Int? = null,
    val startTimeMs: Int? = null,
    val updateTimeMs: Int? = null,
    val contentLength: Int? = null,
    val stopReason: Int? = null,
    val failureReason: Int? = null,
    val percentDownloaded: Double? = null,
    val bytesDownloaded: Double? = null,
    val keySetId: ByteArray? = byteArrayOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExoDownloadInfo

        if (id != other.id) return false
        if (mimeType != other.mimeType) return false
        if (uri != other.uri) return false
        if (streamKeys != other.streamKeys) return false
        if (customCacheKey != other.customCacheKey) return false
        if (data != null) {
            if (other.data == null) return false
            if (!data.contentEquals(other.data)) return false
        } else if (other.data != null) return false
        if (state != other.state) return false
        if (startTimeMs != other.startTimeMs) return false
        if (updateTimeMs != other.updateTimeMs) return false
        if (contentLength != other.contentLength) return false
        if (stopReason != other.stopReason) return false
        if (failureReason != other.failureReason) return false
        if (percentDownloaded != other.percentDownloaded) return false
        if (bytesDownloaded != other.bytesDownloaded) return false
        return keySetId.contentEquals(other.keySetId)
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (mimeType?.hashCode() ?: 0)
        result = 31 * result + (uri?.hashCode() ?: 0)
        result = 31 * result + (streamKeys?.hashCode() ?: 0)
        result = 31 * result + (customCacheKey?.hashCode() ?: 0)
        result = 31 * result + (data?.contentHashCode() ?: 0)
        result = 31 * result + (state ?: 0)
        result = 31 * result + (startTimeMs ?: 0)
        result = 31 * result + (updateTimeMs ?: 0)
        result = 31 * result + (contentLength ?: 0)
        result = 31 * result + (stopReason ?: 0)
        result = 31 * result + (failureReason ?: 0)
        result = 31 * result + (percentDownloaded?.hashCode() ?: 0)
        result = 31 * result + (bytesDownloaded?.hashCode() ?: 0)
        result = 31 * result + keySetId.contentHashCode()
        return result
    }
}