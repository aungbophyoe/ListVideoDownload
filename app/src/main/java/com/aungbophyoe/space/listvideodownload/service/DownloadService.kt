package com.aungbophyoe.space.listvideodownload.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.aungbophyoe.space.listvideodownload.mapper.CacheMapper
import com.aungbophyoe.space.listvideodownload.model.Video
import com.aungbophyoe.space.listvideodownload.room.VideoDAO
import com.aungbophyoe.space.listvideodownload.utility.Constants
import com.aungbophyoe.space.listvideodownload.utility.Constants.START_DOWNLOAD
import com.aungbophyoe.space.listvideodownload.utility.DownloadEvent
import com.aungbophyoe.space.listvideodownload.utility.Utility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.io.*
import javax.inject.Inject
import java.net.URL

@AndroidEntryPoint
class DownloadService : LifecycleService()  {
    @Inject
    lateinit var notificationManager: NotificationManager
    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder
    @Inject
    lateinit var videoDAO: VideoDAO
    @Inject
    lateinit var cacheMapper: CacheMapper
    private val DOWNLOAD_NOTI_EVERY_PERCENT = 5
    private val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    companion object{
        val downloadEvent = MutableLiveData<DownloadEvent>()
        val downloadVideoList = MutableLiveData<Video>()
        val downloadProgress = MutableLiveData<Int>()
    }

    private fun initValue(){

    }

    override fun onCreate() {
        initValue()
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val video = it?.getSerializableExtra("video") as Video
            downloadVideoList.value = video
            when(it?.action){
                START_DOWNLOAD -> startForegroundService()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
    private fun startForegroundService() {
        Log.d("Service", "start service")
        val currentVideo = downloadVideoList.value
        currentVideo?.status = Video.DownloadStatus.Downloading
        downloadVideoList.value = currentVideo!!
        val videoEntity = cacheMapper.mapToEntity(currentVideo)
        CoroutineScope(Dispatchers.IO).launch{
            videoDAO.insertAll(videoEntity)
        }
        startDownload()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel()
        }
        startForeground(Constants.NOTIFICATION_ID,notificationBuilder.build())
        downloadVideoList.observe(this){
            Log.d("service", "${it.progress}%")
            /*currentVideo = it*/
            if(it.progress==100){
                Log.d("Service","download complete")
                notificationBuilder.setOnlyAlertOnce(false)
                notificationBuilder.setAutoCancel(true)
                notificationBuilder.setContentTitle("Download Complete.")
                notificationBuilder.setContentText("")
                notificationBuilder.setProgress(0,0,false)
                notificationManager.notify(Constants.NOTIFICATION_ID, notificationBuilder.build())
                notificationManager.cancel(Constants.NOTIFICATION_ID)
            }else{
                notificationBuilder.setContentTitle("Downloading")
                notificationBuilder.setContentText("${it.progress} %")
                notificationBuilder.setProgress(100,it.progress,false)
                notificationManager.notify(Constants.NOTIFICATION_ID, notificationBuilder.build())
            }
        }
    }

    private fun startDownload(){
        try {
            val dd: File = File(path.path)
            if (!dd.exists()) {
                dd.mkdir()
            }
            val filename = downloadVideoList.value?.url!!.substring(downloadVideoList.value?.url!!.lastIndexOf('/')+1)
            val file = File("$dd/${Utility.getCurrentTimeInMillis()}_$filename")


            CoroutineScope(Dispatchers.IO).launch {
                val url = URL(downloadVideoList.value?.url)
                val connection = url.openConnection()
                connection.connect()
                val fileLength = connection.contentLength
                val output: OutputStream = FileOutputStream(file.path)
                val input: InputStream = BufferedInputStream(connection.getInputStream())
                val data = ByteArray(1024)
                var total: Long = 0
                var count: Int
                var prevProgress = 0
                var progress: Int

                val currentVideo = downloadVideoList.value

                while ((input.read(data)).also {dd-> count = dd  } > 0){
                    total += count
                    progress = ((total*100)/fileLength).toInt()
                    output.write(data, 0, count)
                    withContext(Dispatchers.Main){
                        if (progress == 0) {
                            currentVideo!!.progress = progress
                            downloadVideoList.value = currentVideo!!
                        }
                        if (progress - prevProgress == DOWNLOAD_NOTI_EVERY_PERCENT) {
                            currentVideo!!.progress = progress
                            downloadVideoList.value = currentVideo!!
                            if(progress==100){
                                currentVideo.status = Video.DownloadStatus.Downloaded
                                val videoEntity = cacheMapper.mapToEntity(currentVideo)
                                CoroutineScope(Dispatchers.IO).launch{
                                    Log.d("service","${videoEntity.downloaded}")
                                    videoDAO.insertAll(videoEntity)
                                }
                            }
                            Log.d("service","${progress}")
                            prevProgress = progress
                        }
                    }
                }
                output.flush()
                output.close()
                input.close()
            }
        }catch (e:Exception){
            Log.d("service","${e.message}")
            val errorVideo = downloadVideoList.value
            errorVideo?.status = Video.DownloadStatus.Download
            downloadVideoList.value = errorVideo!!
        }
    }

    @SuppressLint("WrongConstant")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(){
        val channel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            Constants.NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
    }
}