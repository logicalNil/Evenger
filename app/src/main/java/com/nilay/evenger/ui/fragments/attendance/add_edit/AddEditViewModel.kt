package com.nilay.evenger.ui.fragments.attendance.add_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nilay.evenger.core.db.attendance.AttendanceDao
import com.nilay.evenger.core.db.attendance.AttendanceModel
import com.nilay.evenger.core.db.attendance.Days
import com.nilay.evenger.utils.ERROR_IN_UPDATE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val attendanceDao: AttendanceDao
) : ViewModel() {
    val attendance = state.get<AttendanceModel>("attendance") ?: AttendanceModel(
        subject = "Subject",
        total = 0,
        present = 0,
        days = Days(
            presetDays = arrayListOf(),
            absentDays = arrayListOf(),
            totalDays = arrayListOf()
        ),
        teacher = ""
    )

    val type = state.get<String>("type") ?: "Add Subject"

    val request = state.get<Int>("request") ?: 0

    var subjectName = state.get<String>("subject") ?: attendance.subject
        set(value) {
            field = value
            state["subject"] = value
        }

    var subjectTotal = state.get<String>("total") ?: attendance.total
        set(value) {
            field = value
            state["total"] = value
        }

    var subjectPresent = state.get<String>("present") ?: attendance.present
        set(value) {
            field = value
            state["present"] = value
        }
    var teacher = state.get<String>("teacher") ?: attendance.teacher
        set(value) {
            field = value
            state["teacher"] = value
        }


    /**
     * Add subject in background thread
     *@param attendanceModel current Subject
     */
    fun add(attendanceModel: AttendanceModel) = viewModelScope.launch {
        attendanceDao.insert(attendanceModel)
    }

    /**
     * Update subject in background thread
     * @param attendanceModel current Subject
     */
    suspend fun update(attendanceModel: AttendanceModel): Int = withContext(Dispatchers.IO) {
        try {
            attendanceDao.update(attendanceModel)
            1
        } catch (e: Exception) {
            ERROR_IN_UPDATE
        }
    }
}