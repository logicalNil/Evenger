package com.nilay.evenger.ui.fragments.event.root.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nilay.evenger.R
import com.nilay.evenger.databinding.RowEventBinding
import com.nilay.evenger.databinding.RowTitleBinding
import com.nilay.evenger.core.db.event.EventModel

class EventAdapter(
    private val onLongClick : (EventModel) -> Unit = { _ -> },
    private val onEventClick: (EventModel, Boolean) -> Unit = { _, _ -> }
) : RecyclerView.Adapter<EventHolder>() {
    var items = mutableListOf<EventItems>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder =
        when (viewType) {
            R.layout.row_title -> EventHolder.HeaderViewHolder(
                RowTitleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            R.layout.row_event -> EventHolder.EventViewHolder(
                RowEventBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                onEventClick = { pos, isChecked ->
                    onEventClick((items[pos] as EventItems.Event).event, isChecked)
                },
                onLongClick = { pos ->
                    onLongClick((items[pos] as EventItems.Event).event)
                }
            )

            else -> throw IllegalArgumentException("Invalid view type")
        }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: EventHolder, position: Int) {
        when (holder) {
            is EventHolder.HeaderViewHolder -> holder.bind(items[position] as EventItems.Header)
            is EventHolder.EventViewHolder -> holder.bind(items[position] as EventItems.Event)
        }
    }

    override fun getItemViewType(position: Int): Int = when (items[position]) {
        is EventItems.Header -> R.layout.row_title
        is EventItems.Event -> R.layout.row_event
    }
}