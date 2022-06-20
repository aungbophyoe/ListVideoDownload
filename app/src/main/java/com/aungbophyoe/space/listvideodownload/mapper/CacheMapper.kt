package com.aungbophyoe.space.listvideodownload.mapper

import com.aungbophyoe.space.listvideodownload.model.Video
import com.aungbophyoe.space.listvideodownload.room.VideoCacheEntity
import javax.inject.Inject

class CacheMapper @Inject constructor() : EntityMapper<VideoCacheEntity,Video> {
    override fun mapFromEntity(entity: VideoCacheEntity): Video {
        return Video(entity.id,entity.name,entity.url,entity.time,getVideoStatus(entity),0)
    }

    override fun mapToEntity(domainModel: Video): VideoCacheEntity {
        return VideoCacheEntity(domainModel.id,domainModel.name,domainModel.url,domainModel.time,getEntityStatus(domainModel))
    }

    fun mapFromEntityList(entities : List<VideoCacheEntity>) : List<Video>{
        return entities.map { mapFromEntity(it) }
    }

    private fun getEntityStatus(video: Video):Boolean{
        return video.status==Video.DownloadStatus.Downloaded
    }

    private fun getVideoStatus(entity: VideoCacheEntity):Video.DownloadStatus{
        return if(entity.downloaded){
            Video.DownloadStatus.Downloaded
        }else{
            Video.DownloadStatus.Download
        }
    }
}