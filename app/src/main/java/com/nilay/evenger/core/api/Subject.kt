package com.nilay.evenger.core.api

import com.google.errorprone.annotations.Keep
import com.google.gson.annotations.SerializedName
import com.nilay.evenger.core.db.attendance.AttendanceModel
import com.nilay.evenger.core.db.attendance.Days


data class SubjectResponse(
    val data: List<Subject>
)

@Keep
data class Subject(
    val subject: String,
    @SerializedName("total_class") val totalClass: Int,
    val present: Int,
    val teacher: String,
    val credits: Int
)

fun List<Subject>.toAttendance() = this.map {
    AttendanceModel(
        subject = "${it.subject} .Cr ${it.credits}",
        total = it.totalClass,
        present = it.present,
        teacher = it.teacher,
        days = Days(
            presetDays = arrayListOf(),
            absentDays = arrayListOf(),
            totalDays = arrayListOf()
        )
    )
}