package com.example.student_manager.data

data class Attendance(
    val id: Long? = null,
    val studentId: Long,
    val attendanceDate: String,
    val status: String
)