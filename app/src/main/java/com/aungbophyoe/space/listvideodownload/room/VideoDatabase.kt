package com.aungbophyoe.space.listvideodownload.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [VideoCacheEntity::class], version = 1)
abstract class VideoDatabase:RoomDatabase() {
    companion object{
        const val DatabaseName  : String = "photo_database"
    }
    abstract fun videoDao() : VideoDAO
}