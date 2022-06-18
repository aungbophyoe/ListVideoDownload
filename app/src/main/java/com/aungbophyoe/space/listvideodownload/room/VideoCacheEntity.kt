package com.aungbophyoe.space.listvideodownload.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "videos")
data class VideoCacheEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id :Int,
    @ColumnInfo(name = "name")
    val name : String,
    @ColumnInfo(name = "url")
    val url : String,
    @ColumnInfo(name = "time")
    val time : String,
    @ColumnInfo(name = "downloaded")
    val downloaded : Boolean
)