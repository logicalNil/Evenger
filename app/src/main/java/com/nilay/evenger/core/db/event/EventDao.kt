package com.nilay.evenger.core.db.event

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertEvent(eventModel: EventModel): Long

    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertAllEvents(eventModel: List<EventModel>)

    @Query(value = "SELECT * FROM event_table ORDER BY created DESC")
    fun getAllEvents(): Flow<List<EventModel>>

    @Query(value = "SELECT * FROM event_table WHERE isDone = :isDone ORDER BY created DESC")
    fun getAllEvents(
        isDone: Boolean = false
    ): Flow<List<EventModel>>

    @Query(value = "SELECT * FROM event_table WHERE title like '%' || :title || '%' or description like '%' || :title || '%' ORDER BY created DESC")
    fun getEventByTitle(title: String): Flow<List<EventModel>>

    @androidx.room.Delete()
    suspend fun deleteEvent(eventModel: EventModel)

    @androidx.room.Update()
    suspend fun updateEvent(eventModel: EventModel)

    @Query("DELETE FROM event_table")
    suspend fun deleteAllEvents()
}