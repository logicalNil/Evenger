package com.nilay.evenger.core.db.event

import javax.inject.Inject

data class EventUseCases @Inject constructor(
    val insert: InsertEventUseCase,
    val getAllEvents: GetAllEventsUseCase,
    val getEvents: GetEventsUseCase,
    val deleteEvent: DeleteEventUseCase,
    val updateEvent: UpdateEventUseCase,
    val deleteAllEvents: DeleteAllEventsUseCase,
    val updateIsDone: UpdateIsDone,
    val searchEvent: SearchEvent
)

class InsertEventUseCase @Inject constructor(
    private val eventDao: EventDao
) {
    suspend operator fun invoke(eventModel: EventModel): Long {
        return eventDao.insertEvent(eventModel)
    }
}

class GetAllEventsUseCase @Inject constructor(
    private val eventDao: EventDao
) {
    operator fun invoke() = eventDao.getAllEvents()
}

class GetEventsUseCase @Inject constructor(
    private val eventDao: EventDao
) {
    operator fun invoke(isDone: Boolean) = eventDao.getAllEvents(isDone)
}

class DeleteEventUseCase @Inject constructor(
    private val eventDao: EventDao
) {
    suspend operator fun invoke(eventModel: EventModel) {
        eventDao.deleteEvent(eventModel)
    }
}

class UpdateEventUseCase @Inject constructor(
    private val eventDao: EventDao
) {
    suspend operator fun invoke(eventModel: EventModel) {
        eventDao.updateEvent(eventModel)
    }
}

class DeleteAllEventsUseCase @Inject constructor(
    private val eventDao: EventDao
) {
    suspend operator fun invoke() {
        eventDao.deleteAllEvents()
    }
}

class UpdateIsDone @Inject constructor(
    private val eventDao: EventDao
) {
    suspend operator fun invoke(eventModel: EventModel, isDone: Boolean) {
        eventDao.updateEvent(eventModel.copy(isDone = isDone))
    }
}

class SearchEvent @Inject constructor(
    private val eventDao: EventDao
) {
    operator fun invoke(title: String) = eventDao.getEventByTitle(title)
}