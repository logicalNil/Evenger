package com.nilay.evenger.ui.fragments.attendance.adapter

import com.nilay.evenger.core.db.attendance.AttendanceModel

sealed class AttendanceItem {
    data class AttendanceData(val data: AttendanceModel) : AttendanceItem()
}