package com.nilay.evenger.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nilay.evenger.core.db.attendance.AttendanceDao
import com.nilay.evenger.core.db.attendance.AttendanceModel
import com.nilay.evenger.core.db.attendance.DaysTypeConvector
import com.nilay.evenger.core.db.attendance.IsPresentTypeConvector
import com.nilay.evenger.core.db.attendance.StackTypeConvector
import com.nilay.evenger.core.db.event.EventDao
import com.nilay.evenger.core.db.event.EventModel
import com.nilay.evenger.core.db.event.data
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(
    entities = [
        EventModel::class,
        AttendanceModel::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    DaysTypeConvector::class,
    IsPresentTypeConvector::class,
    StackTypeConvector::class
)
abstract class EvengerDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun attendanceDao(): AttendanceDao

    companion object {
        const val DATABASE_NAME = "evenger_database"
    }

    //Adding Syllabus
    class EventCallback @Inject constructor(
        private val database: Provider<EvengerDatabase>,
    ) : Callback() {

        @OptIn(DelicateCoroutinesApi::class)
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val dao = database.get().eventDao()
            GlobalScope.launch(Dispatchers.IO) {
                val syllabus = data
                dao.insertAllEvents(syllabus)
            }
        }
    }
}