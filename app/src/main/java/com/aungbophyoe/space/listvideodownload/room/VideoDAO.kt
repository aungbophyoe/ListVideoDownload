package com.aungbophyoe.space.listvideodownload.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VideoDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(videoCacheEntity: VideoCacheEntity)

    @Query("SELECT * from videos")
    suspend fun getOfflineVideoList() : List<VideoCacheEntity>
}