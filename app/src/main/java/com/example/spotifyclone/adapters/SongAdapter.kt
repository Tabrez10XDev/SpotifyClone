package com.example.spotifyclone.adapters

import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.spotifyclone.R
import com.example.spotifyclone.data.entities.Songs
import kotlinx.android.synthetic.main.list_view.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SongAdapter @Inject constructor(
        private val glide : RequestManager
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    class SongViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

    private val diffCallback = object : DiffUtil.ItemCallback<Songs>(){
        override fun areItemsTheSame(oldItem: Songs, newItem: Songs): Boolean {
            return oldItem.mediaID == newItem.mediaID
        }

        override fun areContentsTheSame(oldItem: Songs, newItem: Songs): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var songs : List<Songs>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        return SongViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.list_view,
                        parent,
                        false
                )
        )
    }

    private var onItemClickListener: ((Songs) -> Unit) ?= null

    fun setOnItemClickListener(listener : (Songs) -> Unit){
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.itemView.apply {
            tvPrimary.text = song.title
            tvSecondary.text = song.imageUrl
//            if(position == 0){
//                val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, Uri.parse(song.imageUrl))
//                ivItemImage.setImageBitmap(bitmap)
//            }
                glide.load(Uri.parse(song.songUrl)).into(ivItemImage)
            setOnClickListener{
                onItemClickListener?.let{ click ->
                    click(song)
                }
            }
        }
    }
}