package com.aungbophyoe.space.listvideodownload.recycleradapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aungbophyoe.space.listvideodownload.R
import com.aungbophyoe.space.listvideodownload.model.Video

class VideoListRecyclerAdapter(private val context: Context,private val downloadClickListener: DownloadClickListener) : ListAdapter<Video,VideoListRecyclerAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<Video>(){
        override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
            return oldItem==newItem
        }

    }
){

    private val layoutInflater = LayoutInflater.from(context)
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val tvVideoName : TextView = view.findViewById(R.id.tvVideoName)
        val tvVideoTime : TextView = view.findViewById(R.id.tvVideoTime)
        val ivDownload : ImageView = view.findViewById(R.id.ivDownload)
        val progressBar : ProgressBar = view.findViewById(R.id.progressBar)
    }

    interface DownloadClickListener{
        fun onDownload(video: Video)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterLayout = layoutInflater.inflate(R.layout.rv_video_item, parent, false)
        val holder = ViewHolder(adapterLayout)
        holder.ivDownload.setOnClickListener{
            val position = holder.adapterPosition
            if(position!=RecyclerView.NO_POSITION){
                val itemIndex = currentList[position]
                downloadClickListener.onDownload(itemIndex)
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val video = currentList[position]
            holder.tvVideoName.text = video.name
            holder.tvVideoTime.text = video.time
        }catch (e:Exception){
            Log.d("onBind","${e.message}")
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }
}