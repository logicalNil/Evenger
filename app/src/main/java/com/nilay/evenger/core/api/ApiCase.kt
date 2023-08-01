package com.nilay.evenger.core.api

import com.nilay.evenger.core.db.attendance.AttendanceDao
import com.nilay.evenger.core.db.attendance.AttendanceModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class SubjectUIModel(
    val attendanceModel: AttendanceModel,
    val isSelected: Boolean = false
)

data class ApiCase @Inject constructor(
    val getAllData: GetAllData, val getAppSubjects: GetAppSubjects,
    val addOrRemove: AddOrRemove
)

data class GetAppSubjects @Inject constructor(
    private val api: ApiInterface,
    private val dao: AttendanceDao,
) {
    suspend operator fun invoke(): List<SubjectUIModel> = withContext(Dispatchers.IO) {
        val list = mutableListOf<SubjectUIModel>()
        val offline = dao.getAllAttendanceList()
        val subject = api.getSubjects().data.toAttendance()
        subject.forEach { subject1 ->
            list.add(
                SubjectUIModel(
                    attendanceModel = subject1,
                    isSelected = offline.find { it.subject == subject1.subject } != null
                )
            )
        }
        list
    }
}

data class AddOrRemove @Inject constructor(
    private val dao: AttendanceDao
) {
    suspend operator fun invoke(
        attendanceModel: AttendanceModel,
        isAdd: Boolean
    ) {
        if (isAdd) {
            dao.insert(attendanceModel)
        } else {
            dao.getAttendance(attendanceModel.subject).let {
                dao.delete(it)
            }
        }
    }
}

class GetAllData @Inject constructor(
    private val api: ApiInterface, private val dao: AttendanceDao
) {
    suspend operator fun invoke(
        listener: (Pair<List<Subject>?, Exception?>) -> Unit
    ) {
        try {
            val data = api.getSubjects().data
            dao.insertAll(data.toAttendance())
            listener(Pair(api.getSubjects().data, null))
        } catch (e: Exception) {
            listener(Pair(null, e))
        }
    }
}

