package com.nilay.evenger.ui.fragments.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nilay.evenger.core.db.attendance.AttendanceDao
import com.nilay.evenger.core.db.attendance.AttendanceModel
import com.nilay.evenger.core.db.user_pref.DataStoreCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val attendanceDao: AttendanceDao, cases: DataStoreCases
) : ViewModel() {


    val getAllPref = cases.getAll.invoke()
    private val _attendance = attendanceDao.getNonArchiveAttendance()
    val unArchive: Flow<List<AttendanceModel>>
        get() = _attendance

    val archive: Flow<List<AttendanceModel>> = attendanceDao.getAllArchiveAttendance()
    val allAttendance = attendanceDao.getAllAttendance()


    val _attendanceEvent = Channel<AttendanceEvent>()
    val attendanceEvent = _attendanceEvent.receiveAsFlow()

    //    IO function
    fun update(attendanceModel: AttendanceModel) = viewModelScope.launch {
        attendanceDao.update(attendanceModel)
    }

    fun delete(attendanceModel: AttendanceModel) = viewModelScope.launch {
        attendanceDao.delete(attendanceModel)
        _attendanceEvent.send(AttendanceEvent.ShowUndoDeleteMessage(attendanceModel))
    }

    fun add(attendanceModel: AttendanceModel, request: Int) = viewModelScope.launch {
        attendanceDao.insert(attendanceModel)
    }

    fun deleteAllArchive() = viewModelScope.launch {
        attendanceDao.deleteAllArchiveAttendance()
    }

    fun deleteAll() = viewModelScope.launch {
        attendanceDao.deleteAll()
    }

    /**
     *Event when attendance is deleted
     *@author Ayaan
     *@since 4.0.3
     */
    sealed class AttendanceEvent {
        /**
         * Note :- syllabus is not use in FragmentEditSyllabus
         * @param attendance AttendanceModel
         * @since 4.0.3
         */
        data class ShowUndoDeleteMessage(
            val attendance: AttendanceModel,
        ) : AttendanceEvent()

    }


}