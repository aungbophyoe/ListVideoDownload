package com.aungbophyoe.space.listvideodownload.di

import android.content.Context
import androidx.room.Room
import com.aungbophyoe.space.listvideodownload.mapper.CacheMapper
import com.aungbophyoe.space.listvideodownload.room.VideoCacheEntity
import com.aungbophyoe.space.listvideodownload.room.VideoDAO
import com.aungbophyoe.space.listvideodownload.room.VideoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {
    @Provides
    fun provideVideoDatabase(@ApplicationContext context : Context):VideoDatabase{
        return Room.databaseBuilder(context,VideoDatabase::class.java,VideoDatabase.DatabaseName)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideVideoDao(videoDatabase: VideoDatabase): VideoDAO{
        return videoDatabase.videoDao()
    }
}