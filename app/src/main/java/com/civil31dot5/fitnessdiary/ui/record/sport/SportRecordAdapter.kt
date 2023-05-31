package com.civil31dot5.fitnessdiary.ui.record.sport

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.civil31dot5.fitnessdiary.databinding.ItemSportHistoryBinding
import com.civil31dot5.fitnessdiary.domain.model.StravaSportRecord
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val diffUtil = object : DiffUtil.ItemCallback<StravaSportRecord>(){
    override fun areItemsTheSame(oldItem: StravaSportRecord, newItem: StravaSportRecord): Boolean {
        return oldItem.stravaId == newItem.stravaId
    }

    override fun areContentsTheSame(oldItem: StravaSportRecord, newItem: StravaSportRecord): Boolean {
        return oldItem == newItem
    }
}
class SportRecordAdapter: ListAdapter<StravaSportRecord, SportRecordAdapter.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSportHistoryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemSportHistoryBinding): RecyclerView.ViewHolder(binding.root){


        fun bind(item: StravaSportRecord) {
            with(binding){
                tvDatetime.text = item.dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                tvName.text = item.name
                tvCalories.text = "%.0f".format(item.calories)
                tvTpye.text = item.type
                tvElapsedTime.text = LocalTime.MIN.plusSeconds(item.elapsedTimeSec).format(DateTimeFormatter.ISO_LOCAL_TIME)
//                tvElapsedTime.text = DateUtils.formatElapsedTime(item.elapsedTimeSec)

            }
        }


    }
}