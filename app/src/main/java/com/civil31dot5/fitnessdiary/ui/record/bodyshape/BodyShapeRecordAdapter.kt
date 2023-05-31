package com.civil31dot5.fitnessdiary.ui.record.bodyshape

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.civil31dot5.fitnessdiary.R
import com.civil31dot5.fitnessdiary.databinding.ItemBodyShapeRecordBinding
import com.civil31dot5.fitnessdiary.domain.model.BodyShapeRecord
import com.civil31dot5.fitnessdiary.ui.record.diet.RecordImageAdapter
import com.civil31dot5.fitnessdiary.ui.utility.GenericRecordAdapterListener
import java.time.format.DateTimeFormatter

private val diffUtil: ItemCallback<BodyShapeRecord> = object : ItemCallback<BodyShapeRecord>() {
    override fun areItemsTheSame(oldItem: BodyShapeRecord, newItem: BodyShapeRecord): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BodyShapeRecord, newItem: BodyShapeRecord): Boolean {
        return oldItem == newItem
    }
}

class BodyShapeRecordAdapter(private val listener: GenericRecordAdapterListener<BodyShapeRecord>) :
    ListAdapter<BodyShapeRecord, BodyShapeRecordAdapter.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBodyShapeRecordBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemBodyShapeRecordBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private var isImageExpend = false
            set(value) {
                field = value
                refreshImageViewPager()
            }

        init {
            binding.vpImages.adapter = RecordImageAdapter()

            binding.btEdit.setOnClickListener {
                listener.onEditClick(getItem(bindingAdapterPosition))
            }

            binding.btDelete.setOnClickListener {
                listener.onDeleteClick(getItem(bindingAdapterPosition))
            }

            binding.btCollapseExtend.setOnClickListener {
                isImageExpend = !isImageExpend
            }
        }

        fun bind(item: BodyShapeRecord) {
            with(binding) {
                tvName.text = item.name
                tvDatetime.text = item.dateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
                tvWeight.text = "體重：%.2fkg".format(item.weight)
                if (item.bodyFatPercentage != null) {
                    tvFatRate.text = "體脂率：%.2f%%".format(item.bodyFatPercentage)
                } else {
                    tvFatRate.text = "體脂率：--%"
                }
                tvNote.text = item.note
                isImageExpend = false
                btCollapseExtend.visibility = if (item.images.isEmpty()) View.GONE else View.VISIBLE
                (vpImages.adapter as? RecordImageAdapter)?.submitList(item.images)
            }
        }

        private fun refreshImageViewPager() {
            binding.vpImages.visibility = if (isImageExpend) View.VISIBLE else View.GONE
            if (isImageExpend) {
                binding.btCollapseExtend.setImageResource(R.drawable.ic_arrow_down)
            } else {
                binding.btCollapseExtend.setImageResource(R.drawable.ic_arrow_left)
            }
        }

    }

}