package com.aungbophyoe.space.listvideodownload.di

import com.aungbophyoe.space.listvideodownload.mapper.CacheMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    /*@Provides
    fun provideMainRepository(cacheMapper: CacheMapper): MainRepository {
        return MainRepository(cacheMapper)
    }*/

    @Provides
    fun provideCacheMapper(): CacheMapper {
        return CacheMapper()
    }
}