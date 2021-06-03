package com.example.spotifyclone.ui.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.observe
import com.bumptech.glide.RequestManager
import com.example.spotifyclone.R
import com.example.spotifyclone.adapters.SwipeSongAdapter
import com.example.spotifyclone.data.entities.Song
import com.example.spotifyclone.exoplayer.toSong
import com.example.spotifyclone.ui.viewmodels.MainViewModel
import com.example.spotifyclone.util.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class SpotifyClone : AppCompatActivity() {

    private val mainViewModel : MainViewModel by viewModels()

    @Inject
    lateinit var glide : RequestManager

    @Inject
    lateinit var swipeSongAdapter: SwipeSongAdapter

    private var currPlayingSong : Song ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_SpotifyClone)
        setContentView(R.layout.activity_main)

        subscribeToObservers()
        vpSong.adapter = swipeSongAdapter

    }

    fun switchViewPagerToCurrentSong(song: Song){
        val newItemIndex = swipeSongAdapter.songs.indexOf(song)
        if(newItemIndex != -1){
            vpSong.currentItem = newItemIndex
            currPlayingSong = song
        }

    }


    private fun subscribeToObservers(){
        mainViewModel.mediaItems.observe(this){
            it?.let{result->
                when(result.status){
                    Status.SUCCESS->{
                        result.data?.let {songs->
                            swipeSongAdapter.songs = songs
                            if(songs.isNotEmpty()){
                                glide.load((currPlayingSong ?: songs[0]).imageUrl).into(ivCurSongImage)
                            }
                            switchViewPagerToCurrentSong(currPlayingSong ?: return@observe)
                        }
                    }
                    Status.LOADING -> Unit
                    Status.ERROR -> Unit
                }
            }
        }
        mainViewModel.currPlayingSong.observe(this){
            if(it == null) return@observe

            currPlayingSong = it.toSong()
            glide.load(currPlayingSong?.imageUrl).into(ivCurSongImage)
            switchViewPagerToCurrentSong(currPlayingSong ?: return@observe)

        }
    }
}