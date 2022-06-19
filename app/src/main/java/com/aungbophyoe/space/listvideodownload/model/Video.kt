package com.aungbophyoe.space.listvideodownload.model

import java.io.Serializable

data class Video(
    val id : Int,
    val name : String,
    val url : String,
    val time : String,
    var status : DownloadStatus,
    var progress : Int
) : Serializable {
    enum class DownloadStatus{
        Downloading,
        Downloaded,
        Download
    }
}