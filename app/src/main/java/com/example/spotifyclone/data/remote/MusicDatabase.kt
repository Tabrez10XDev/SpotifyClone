package com.example.spotifyclone.data.remote

import android.util.Log
import com.example.spotifyclone.util.CONSTANTS.SONG_COLLECTION
import com.example.spotifyclone.data.entities.Songs
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.Collections.emptyList

class MusicDatabase {

    private val firestore = FirebaseFirestore.getInstance()
    private val songCollection = firestore.collection(SONG_COLLECTION)

    suspend fun getAllSongs() : List<Songs>{
        return try{
            songCollection.get().addOnSuccessListener {
                it?.let {
                    for(document in it){
                        Log.d("ALLSONG",document.data.toString())
                    }
                }
            }
            songCollection.get().await().toObjects(Songs::class.java)
        }
        catch (e : Exception){
            Log.d("allSongs",e.message.toString())
            emptyList()
        }

    }
}