package com.civil31dot5.fitnessdiary.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.civil31dot5.fitnessdiary.GlideApp
import com.civil31dot5.fitnessdiary.databinding.ItemSelectPhotoBinding
import com.civil31dot5.fitnessdiary.domain.model.RecordImage
import com.civil31dot5.fitnessdiary.extraFile

private val diffUtil = object : DiffUtil.ItemCallback<RecordImage>() {
    override fun areItemsTheSame(oldItem: RecordImage, newItem: RecordImage): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RecordImage, newItem: RecordImage): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: RecordImage, newItem: RecordImage): Any {
        val payload = Bundle()
        payload.putString("note", newItem.note)
        return payload
    }
}

class SelectedPhotosAdapter(
    private val onDeleteListener: (RecordImage) -> Unit,
    private val onNoteChangedListener: (RecordImage, String) -> Unit
) : ListAdapter<RecordImage, SelectedPhotosAdapter.ViewHolder>(diffUtil) {

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

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
            return
        }
        val payload = payloads.getOrNull(0) ?: return
        val bundle = (payload as? Bundle) ?: return
        val newNote = bundle.getString("note") ?: return
        holder.updateNote(newNote)
    }

    inner class ViewHolder(
        private val binding: ItemSelectPhotoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btDelete.setOnClickListener {
                onDeleteListener.invoke(getItem(bindingAdapterPosition))
            }


            binding.etNote.doAfterTextChanged {
                it ?: return@doAfterTextChanged
                if (getItem(bindingAdapterPosition).note != it.toString()) {
                    onNoteChangedListener.invoke(getItem(bindingAdapterPosition), it.toString())
                }
            }
        }

        fun bind(item: RecordImage) {
            binding.etNote.setText(item.note)

            GlideApp.with(binding.photo)
                .load(item.extraFile(binding.root.context.applicationContext))
                .into(binding.photo)
        }

        fun updateNote(newNote: String) {
            binding.etNote.setText(newNote)
            binding.etNote.setSelection(binding.etNote.editableText.length)
        }
    }
}