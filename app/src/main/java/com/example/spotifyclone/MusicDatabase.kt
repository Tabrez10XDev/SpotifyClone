package com.example.spotifyclone

import com.example.spotifyclone.CONSTANTS.SONG_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class MusicDatabase {

    private val firestore = FirebaseFirestore.getInstance()
    private val songCollection = firestore.collection(SONG_COLLECTION)

    suspend fun getAllSongs() : List<Songs>{
        return try{
            songCollection.get().await().toObjects(Songs::class.java)
        }
        catch (e : Exception){
            emptyList()
        }
    }
}