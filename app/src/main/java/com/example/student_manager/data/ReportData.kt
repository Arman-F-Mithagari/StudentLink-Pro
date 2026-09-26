package com.example.student_manager.data

data class ReportData(
    val totalStudents: Long,
    val totalAttendance: Long,
    val presentCount: Long,
    val absentCount: Long,
    val attendancePercentage: Double
)