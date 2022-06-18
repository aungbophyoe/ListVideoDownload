package com.aungbophyoe.space.listvideodownload.utility

sealed class DownloadEvent {
    object Downloading : DownloadEvent()
    object Downloaded : DownloadEvent()
}
