package com.nilay.evenger.core.db.attendance

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(attendance: AttendanceModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(attendance: List<AttendanceModel>)

    @Update
    suspend fun update(attendance: AttendanceModel)

    @Delete
    suspend fun delete(attendance: AttendanceModel)

    //select * from attendance_table where  isArchive is NULL or isArchive = 0 ;
    @Query("SELECT * FROM attendance_table WHERE isArchive is NULL or isArchive = 0 ORDER BY id ASC")
    fun getNonArchiveAttendance(): Flow<List<AttendanceModel>>

    @Query("SELECT * FROM attendance_table ORDER BY id ASC")
    fun getAllAttendance(): Flow<List<AttendanceModel>>


    @Query("SELECT * FROM attendance_table ORDER BY id ASC")
    fun getAllAttendanceList(): List<AttendanceModel>

    @Query("SELECT * FROM attendance_table WHERE isArchive = 1 ORDER BY id ASC")
    fun getAllArchiveAttendance(): Flow<List<AttendanceModel>>

    @Query("SELECT * FROM attendance_table WHERE subject_name LIKE '%'||:query||'%'")
    suspend fun getAttendance(query: String): AttendanceModel

    @Query("DELETE FROM attendance_table")
    suspend fun deleteAll()


    /**
     * @since 4.0.3
     * @author Ayaan
     */
    @Query("SELECT * FROM attendance_table WHERE subject_name = :query")
    suspend fun findSyllabus(query: String): AttendanceModel?


    @Query("DELETE FROM attendance_table WHERE isArchive = 1")
    suspend fun deleteAllArchiveAttendance()
}