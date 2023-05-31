package com.civil31dot5.fitnessdiary.ui.record.diet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.civil31dot5.fitnessdiary.databinding.ItemDietRecordBinding
import com.civil31dot5.fitnessdiary.domain.model.DietRecord
import java.time.format.DateTimeFormatter

private val diffUtil = object : DiffUtil.ItemCallback<DietRecord>() {
    override fun areItemsTheSame(oldItem: DietRecord, newItem: DietRecord): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DietRecord, newItem: DietRecord): Boolean {
        return oldItem == newItem
    }
}


class DietRecordAdapter(
    private val onEditClick: (DietRecord) -> Unit,
    private val onDeleteClick: (DietRecord) -> Unit
) : ListAdapter<DietRecord, DietRecordAdapter.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemDietRecordBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemDietRecordBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.vpImages.adapter = RecordImageAdapter()

            binding.btEdit.setOnClickListener {
                onEditClick.invoke(getItem(bindingAdapterPosition))
            }

            binding.btDelete.setOnClickListener {
                onDeleteClick.invoke(getItem(bindingAdapterPosition))
            }
        }

        fun bind(item: DietRecord) {
            with(binding) {
                tvName.text = item.name
                tvDatetime.text = item.dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                tvNote.text = item.note
                binding.vpImages.visibility =
                    if (item.images.isNotEmpty()) View.VISIBLE else View.GONE
                (binding.vpImages.adapter as? RecordImageAdapter)?.submitList(item.images)
            }
        }

    }

}