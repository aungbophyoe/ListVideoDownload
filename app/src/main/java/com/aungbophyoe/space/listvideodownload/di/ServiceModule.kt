package com.aungbophyoe.space.listvideodownload.di

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.aungbophyoe.space.listvideodownload.R
import com.aungbophyoe.space.listvideodownload.mapper.CacheMapper
import com.aungbophyoe.space.listvideodownload.room.VideoDAO
import com.aungbophyoe.space.listvideodownload.utility.Constants
import com.aungbophyoe.space.listvideodownload.view.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped


@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {
    @ServiceScoped
    @Provides
    fun provideMainActivityPendingIntent(@ApplicationContext context: Context) : PendingIntent {
        return PendingIntent.getActivity(
            context,
            110,
            Intent(context, MainActivity::class.java).apply {
                this.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    @ServiceScoped
    @Provides
    fun provideNotificationBuilder(@ApplicationContext context: Context,pendingIntent: PendingIntent): NotificationCompat.Builder{
        return NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
            .setOngoing(true)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Download in progress")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setProgress(100,0,false)
            .setContentIntent(pendingIntent)
    }

    @ServiceScoped
    @Provides
    fun provideNotificationManager(@ApplicationContext context: Context) : NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
}