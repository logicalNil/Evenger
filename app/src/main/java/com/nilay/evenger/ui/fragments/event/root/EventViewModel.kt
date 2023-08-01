package com.nilay.evenger.ui.fragments.event.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nilay.evenger.core.db.event.EventModel
import com.nilay.evenger.core.db.event.EventUseCases
import com.nilay.evenger.ui.fragments.event.root.adapter.EventItems
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class EventViewModel @Inject constructor(
    private val cases: EventUseCases
) : ViewModel() {

    val monthStateFlow = MutableStateFlow(System.currentTimeMillis())

    val query = MutableStateFlow("_DEFAULT_")

    fun getData() = combine(
        cases.getEvents.invoke(false), cases.getEvents.invoke(true), monthStateFlow
    ) { incomplete, complete, month ->
        Triple(incomplete, complete, month)
    }.flatMapLatest { (incomplete, complete, month) ->
        val items = mutableListOf<EventItems>()
        incomplete.mapToEventItems(month).let {
            if (it.isNotEmpty()) {
                items.add(EventItems.Header("Incomplete"))
                items.addAll(it)
            }
        }
        complete.mapToEventItems(month).let {
            if (it.isNotEmpty()) {
                items.add(EventItems.Header("Complete"))
                items.addAll(it)
            }
        }
        flowOf(items)
    }


    private fun List<EventModel>.mapToEventItems(month: Long) = filter {
        Calendar.getInstance().apply {
            time = Date(it.created)
        }.get(Calendar.MONTH) == Calendar.getInstance().apply {
            time = Date(month)
        }.get(Calendar.MONTH)
    }.map { EventItems.Event(it) }

    fun setEventIsDone(event: EventModel, isDone: Boolean) = viewModelScope.launch {
        cases.updateIsDone.invoke(event, isDone)
    }

    fun deleteEvent(event: EventModel) = viewModelScope.launch {
        cases.deleteEvent.invoke(event)
    }

    fun getSearchItem() = query.flatMapLatest {
        cases.searchEvent.invoke(it).map { searchEvents ->
            val list = mutableListOf<EventItems>()
            if (searchEvents.isNotEmpty()) {
                searchEvents.filterNot { it.isDone }.map { EventItems.Event(it) }.let {
                    if (it.isNotEmpty()) {
                        list.add(EventItems.Header("Incomplete"))
                        list.addAll(it)
                    }
                }
                searchEvents.filter {
                    list.any { item ->
                        if (item is EventItems.Event) {
                            item.event.id == it.id
                        } else {
                            false
                        }
                    }.not()
                }.filter { it.isDone }.map { EventItems.Event(it) }
                    .let {
                        if (it.isNotEmpty()) {
                            list.add(EventItems.Header("Complete"))
                            list.addAll(it)
                        }
                    }
            }
            list
        }
    }

}