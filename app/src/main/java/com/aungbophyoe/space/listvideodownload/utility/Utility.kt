package com.aungbophyoe.space.listvideodownload.utility

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Environment
import com.aungbophyoe.space.listvideodownload.model.Video
import pub.devrel.easypermissions.EasyPermissions

object Utility {
    fun hasStoragePermission(context: Context) =
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            Environment.isExternalStorageManager()
        }else{
            EasyPermissions.hasPermissions(context,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

    fun getCurrentTimeInMillis() = "${System.currentTimeMillis()}"

    fun getVideoList() : List<Video>{
        return listOf(
            Video(1,"What is Inequation?","https://concept-xvideos.sgp1.cdn.digitaloceanspaces.com/93828.CBUig.m4v","00:02:03",Video.DownloadStatus.Download,0),
            Video(2,"Theory | Quadratic Functions","https://concept-xvideos.sgp1.cdn.digitaloceanspaces.com/22951.IkgI7.m4v","00:02:50",Video.DownloadStatus.Download,0),
            Video(3,"Theory | Graphs of Quadratic Functions","https://concept-xvideos.sgp1.cdn.digitaloceanspaces.com/71417.EKwrQ.m4v","00:05:47",Video.DownloadStatus.Download,0),
            Video(4,"Theory | Number Lines Approach","https://concept-xvideos.sgp1.cdn.digitaloceanspaces.com/83077.SAuJ4.m4v","00:14:24",Video.DownloadStatus.Download,0),
            Video(5,"Theory | Table Approach","https://concept-xvideos.sgp1.cdn.digitaloceanspaces.com/56535.e0wqX.m4v","00:06:29",Video.DownloadStatus.Download,0)
        )
    }
}