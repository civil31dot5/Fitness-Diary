package com.civil31dot5.fitnessdiary.ui.report

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.civil31dot5.fitnessdiary.GlideApp
import com.civil31dot5.fitnessdiary.databinding.ItemGridImageBinding
import com.civil31dot5.fitnessdiary.domain.model.RecordImage
import com.civil31dot5.fitnessdiary.extraFile

private val DiffUtil = object : ItemCallback<RecordImage>(){

    override fun areItemsTheSame(oldItem: RecordImage, newItem: RecordImage): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RecordImage, newItem: RecordImage): Boolean {
        return oldItem == newItem
    }
}
class ImageGridViewAdapter: ListAdapter<RecordImage, ImageGridViewAdapter.ViewHolder>(DiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemGridImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemGridImageBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: RecordImage) {
            binding.tvNote.text = item.note

            GlideApp.with(binding.ivImage)
                .load(item.extraFile(binding.root.context))
                .into(binding.ivImage)
        }
    }

}