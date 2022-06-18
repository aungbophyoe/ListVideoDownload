package com.aungbophyoe.space.listvideodownload.model

data class Video(
    val id : Int,
    val name : String,
    val url : String,
    val time : String,
    val status : DownloadStatus,
    val progress : Int
) {
    enum class DownloadStatus{
        Downloading,
        Downloaded,
        Download
    }
}