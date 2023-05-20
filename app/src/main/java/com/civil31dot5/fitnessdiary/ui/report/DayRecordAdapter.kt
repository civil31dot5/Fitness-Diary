package com.civil31dot5.fitnessdiary.ui.report

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.civil31dot5.fitnessdiary.R
import com.civil31dot5.fitnessdiary.databinding.ItemDayDietBinding
import com.civil31dot5.fitnessdiary.databinding.ItemDaySportBinding
import com.civil31dot5.fitnessdiary.domain.model.DietRecord
import com.civil31dot5.fitnessdiary.domain.model.Record
import com.civil31dot5.fitnessdiary.domain.model.StravaSport
import java.time.format.DateTimeFormatter


private val DiffUtil = object : ItemCallback<Record>(){
    override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean {
        return oldItem == newItem
    }
}

class DayRecordAdapter: ListAdapter<Record, RecyclerView.ViewHolder>(DiffUtil) {

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is DietRecord -> R.layout.item_day_diet
            is StravaSport -> R.layout.item_day_sport
            else -> throw IllegalStateException("wrong type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            R.layout.item_day_diet -> DayDietViewHolder(ItemDayDietBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            R.layout.item_day_sport -> DaySportViewHolder(ItemDaySportBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw IllegalStateException("wrong type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is DayDietViewHolder -> holder.bind(getItem(position) as DietRecord)
            is DaySportViewHolder -> holder.bind(getItem(position) as StravaSport)
        }
    }

    inner class DayDietViewHolder(val binding: ItemDayDietBinding): RecyclerView.ViewHolder(binding.root){

        init {
            binding.rvImages.adapter = ImageGridViewAdapter()
        }

        fun bind(item: DietRecord) {
            with(binding){
                tvDateTime.text = item.dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                tvNote.text = item.note
                tvNote.visibility = if (item.note.isEmpty()) View.GONE else View.VISIBLE
                (rvImages.adapter as? ImageGridViewAdapter)?.submitList(item.images)
            }
        }
    }

    inner class DaySportViewHolder(val binding: ItemDaySportBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: StravaSport) {
            with(binding){
                tvDateTime.text = item.dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                tvName.text = item.name
                tvCalories.text = "消耗 %.0f 卡路里".format(item.calories)
            }
        }

    }
}