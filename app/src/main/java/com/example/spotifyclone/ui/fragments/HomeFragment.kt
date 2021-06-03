package com.example.spotifyclone.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spotifyclone.R
import com.example.spotifyclone.adapters.SongAdapter
import com.example.spotifyclone.ui.viewmodels.MainViewModel
import com.example.spotifyclone.util.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home){

    lateinit var mainViewModel: MainViewModel
    @Inject
    lateinit var songAdapter: SongAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)


        setupRV()
        subscribeToObservers()


        songAdapter.setItemClickListener {
            mainViewModel.playOrToggleSong(it, false)
        }
    }

    private fun setupRV() {
        rvAllSongs.apply {

            adapter = songAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
    private fun subscribeToObservers(){
        mainViewModel.mediaItems.observe(viewLifecycleOwner){result->
            when(result.status){
                Status.SUCCESS->{
                    Log.d("StateResult","Success")
                    allSongsProgressBar.isVisible = false
                    result.data?.let {songs->
                        Log.d("StateResult",songs.toString())
                        songAdapter.songs = songs
                    }
                }
                Status.ERROR -> Unit
                Status.LOADING->
                    allSongsProgressBar.isVisible = true

            }
            }
        }
    }
