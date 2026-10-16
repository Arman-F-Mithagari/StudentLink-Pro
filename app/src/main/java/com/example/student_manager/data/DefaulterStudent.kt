package com.example.student_manager.data

data class DefaulterStudent(
    val studentId: Long? = null,
    val name: String = "",
    val email: String = "",
    val course: String = "",
    val attendancePercentage: Double = 0.0
)