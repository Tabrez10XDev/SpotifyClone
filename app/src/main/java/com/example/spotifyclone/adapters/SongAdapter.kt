package com.example.spotifyclone.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.spotifyclone.R
import com.example.spotifyclone.data.entities.Song
import kotlinx.android.synthetic.main.list_view.view.*
import javax.inject.Inject

class SongAdapter @Inject constructor(
        private val glide : RequestManager
) : BaseSongAdapter(R.layout.list_view) {

    override var differ = AsyncListDiffer(this, diffCallback)

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.itemView.apply {
            tvPrimary.text = song.subtitle
            tvSecondary.text = song.title
            glide.load(Uri.parse(song.imageUrl)).into(ivItemImage)
            setOnClickListener{
                onItemClickListener?.let{ click ->
                    click(song)
                }
            }
        }
    }
}