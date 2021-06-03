package com.example.spotifyclone.ui.viewmodels

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_ID
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spotifyclone.data.entities.Song
import com.example.spotifyclone.exoplayer.*
import com.example.spotifyclone.util.CONSTANTS.MEDIA_ROOT_ID
import com.example.spotifyclone.util.Resource

class MainViewModel @ViewModelInject constructor(
    private val musicServiceConnection: MusicServiceConnection
) : ViewModel() {


    var mediaItems = MutableLiveData<Resource<List<Song>>>()
        private set

    var isConnected = musicServiceConnection.isConnected
    var networkError = musicServiceConnection.networkError
    var currPlayingSong = musicServiceConnection.currPlayingSong
    var playbackState = musicServiceConnection.playbackState


    init {
        mediaItems.postValue(Resource.loading(null))
        musicServiceConnection.subscribe(MEDIA_ROOT_ID, object : MediaBrowserCompat.SubscriptionCallback(){
            override fun onChildrenLoaded(parentId: String, children: MutableList<MediaBrowserCompat.MediaItem>) {
                super.onChildrenLoaded(parentId, children)
                val items = children.map {
                    Song(
                            it.mediaId!!,
                            it.description.title.toString(),
                            it.description.subtitle.toString(),
                            it.description.mediaUri.toString(),
                            it.description.iconUri.toString()
                    )
                }
                Log.d("Fragss",items.toString())
                mediaItems.postValue(Resource.success(items))
            }
        })
    }

    fun skipToNextSong(){
        musicServiceConnection.transportControls.skipToNext()
    }


    fun skipToPreviousSong(){
        musicServiceConnection.transportControls.skipToPrevious()
    }


    fun seekTo(pos : Long){
        musicServiceConnection.transportControls.seekTo(pos)
    }

    fun playOrToggleSong(mediaItem : Song, toggle : Boolean) {
        val isPrepared = playbackState.value?.isPrepared ?: false
        Log.d("Lowj",toggle.toString())

        if(isPrepared && mediaItem.mediaID ==
                currPlayingSong?.value?.getString(METADATA_KEY_MEDIA_ID)){
            playbackState?.value?.let { playbackState->
                when{
                    playbackState.isPlaying-> {
                        Log.d("Lowj", "isPlaying")

                        if (toggle) {
                            musicServiceConnection.transportControls.pause()

                        } else Unit
                    }
                    playbackState.isPlayEnabled -> {
                        musicServiceConnection.transportControls.play()

                    }
                    else -> Unit
                }
            }
        } else{
            musicServiceConnection.transportControls.playFromMediaId(mediaItem.mediaID, null)
        }
    }

    override fun onCleared() {
        super.onCleared()
        musicServiceConnection.unsubscribe(MEDIA_ROOT_ID, object : MediaBrowserCompat.SubscriptionCallback(){})
    }


}