package com.civil31dot5.fitnessdiary.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.civil31dot5.fitnessdiary.GlideApp
import com.civil31dot5.fitnessdiary.databinding.ItemSelectPhotoBinding
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

class SelectedPhotosAdapter(
    private val onDeleteListener: (RecordImage) -> Unit
): ListAdapter<RecordImage, SelectedPhotosAdapter.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSelectPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemSelectPhotoBinding
    ): RecyclerView.ViewHolder(binding.root){

        init {
            binding.btDelete.setOnClickListener {
                onDeleteListener.invoke(getItem(bindingAdapterPosition))
            }
        }

        fun bind(item: RecordImage) {
            GlideApp.with(binding.photo)
                .load(File(item.filePath))
                .into(binding.photo)
        }
    }
}