package com.civil31dot5.fitnessdiary.ui.record.diet

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.civil31dot5.fitnessdiary.GlideApp
import com.civil31dot5.fitnessdiary.domain.model.RecordImage
import java.io.File

private val diffUtil = object : DiffUtil.ItemCallback<RecordImage>(){
    override fun areItemsTheSame(oldItem: RecordImage, newItem: RecordImage): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RecordImage, newItem: RecordImage): Boolean {
        return oldItem.id == newItem.id
    }
}

class RecordImageAdapter: ListAdapter<RecordImage, RecordImageAdapter.ViewHolder>(diffUtil){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val imageView = ImageView(parent.context)
        imageView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return ViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val imageView: ImageView
    ): RecyclerView.ViewHolder(imageView){

        fun bind(recordImage: RecordImage){
            GlideApp.with(imageView)
                .load(File(imageView.context.filesDir, recordImage.filePath))
                .into(imageView)
        }
    }

}