package com.nilay.evenger.ui.fragments.event.root.adapter

import com.nilay.evenger.core.db.event.EventModel

sealed class EventItems {
    data class Event(val event: EventModel) : EventItems()
    data class Header(val header: String) : EventItems()
}