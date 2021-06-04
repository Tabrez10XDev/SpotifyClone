package com.example.spotifyclone.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifyclone.exoplayer.MusicService
import com.example.spotifyclone.exoplayer.MusicServiceConnection
import com.example.spotifyclone.exoplayer.currentPlaybackPosition
import com.example.spotifyclone.util.CONSTANTS.UPDATE_PLAYER_POSITION_INTERVAL
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SongViewModel @ViewModelInject constructor(
    musicServiceConnection: MusicServiceConnection
): ViewModel() {

    private val playbackState = musicServiceConnection.playbackState

    private val _currSongDuration = MutableLiveData<Long>()
    val currSongDuration = _currSongDuration

    private val _currPlayerPosition = MutableLiveData<Long>()
    val currPlayerPosition = _currPlayerPosition

    init {
        updateCurrPlayerPosition()
    }

    private fun updateCurrPlayerPosition(){
        viewModelScope.launch {
            val pos = playbackState.value?.currentPlaybackPosition
            if(currPlayerPosition.value != pos){
                _currPlayerPosition.postValue(pos)
                _currSongDuration.postValue(MusicService.currSongDuration)
            }
            delay(UPDATE_PLAYER_POSITION_INTERVAL)
        }
    }

}