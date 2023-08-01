package com.nilay.evenger.core.db.event

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Keep
@Entity(
    tableName = "event_table",
)
@Parcelize
data class EventModel(
    val title: String,
    val description: String,
    val location: String = "",
    val isDone: Boolean = false,
    val created: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
) : Parcelable

val data = listOf(
    EventModel(
        title = "Nilay's Birthday",
        description = "Happy Birthday Nilay",
        location = "Chennai",
        isDone = false,
        created = 1690805484000
    ),
)