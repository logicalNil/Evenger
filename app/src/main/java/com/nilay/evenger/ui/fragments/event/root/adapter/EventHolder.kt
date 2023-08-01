package com.nilay.evenger.ui.fragments.event.root.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.nilay.evenger.databinding.RowEventBinding
import com.nilay.evenger.databinding.RowTitleBinding
import com.nilay.evenger.utils.convertToFormatDate

sealed class EventHolder(
    binding: ViewBinding
) : RecyclerView.ViewHolder(binding.root) {
    class HeaderViewHolder(
        private val binding: RowTitleBinding
    ) : EventHolder(binding) {
        fun bind(model: EventItems.Header) = with(model) {
            binding.apply {
                root.text = header
            }
        }
    }

    class EventViewHolder(
        private val binding: RowEventBinding,
        private val onEventClick: (Int, Boolean) -> Unit,
        private val onLongClick: (Int) -> Unit
    ) : EventHolder(binding) {
        init {
            binding.root.setOnClickListener {
                binding.eventNameRadioButton.isChecked = !binding.eventNameRadioButton.isChecked
                onEventClick(adapterPosition, binding.eventNameRadioButton.isChecked)
            }
            binding.root.setOnLongClickListener {
                onLongClick(adapterPosition)
                true
            }
        }

        fun bind(model: EventItems.Event) {
            val event = model.event
            binding.apply {
                eventNameRadioButton.apply {
                    text = event.title
                    isChecked = event.isDone
                    isClickable = false
                }
                eventDescription.text = event.description
                eventDate.text = event.created.convertToFormatDate()
            }
        }
    }
}