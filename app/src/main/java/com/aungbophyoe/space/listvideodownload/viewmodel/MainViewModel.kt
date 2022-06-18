package com.aungbophyoe.space.listvideodownload.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aungbophyoe.space.listvideodownload.mapper.CacheMapper
import com.aungbophyoe.space.listvideodownload.model.Video
import com.aungbophyoe.space.listvideodownload.utility.Utility
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val cacheMapper: CacheMapper) : ViewModel() {
    private var _videoList : MutableSharedFlow<List<Video>> = MutableSharedFlow()
    val videoList = _videoList.asSharedFlow()

    fun getVideoList(){
        viewModelScope.launch {
            delay(1000)
            _videoList.emit(Utility.getVideoList())
        }
    }
}