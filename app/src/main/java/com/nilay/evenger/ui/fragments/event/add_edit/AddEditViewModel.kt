package com.nilay.evenger.ui.fragments.event.add_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nilay.evenger.core.db.event.EventModel
import com.nilay.evenger.core.db.event.EventUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val state: SavedStateHandle, private val eventUseCases: EventUseCases
) : ViewModel() {
    val event = state.get<EventModel>("event")

    var date: Long
        set(value) = state.set("date", value)
        get() = state.get<Long>("date") ?: System.currentTimeMillis()


    fun saveEvent(
        eventModel: EventModel,
        isUpdate: Boolean,
        action: () -> Unit = {}
    ) = viewModelScope.launch {
        if (isUpdate) eventUseCases.updateEvent.invoke(eventModel)
        else eventUseCases.insert.invoke(eventModel)
        action()
    }

}