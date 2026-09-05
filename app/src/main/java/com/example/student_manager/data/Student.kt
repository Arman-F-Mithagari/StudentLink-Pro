package com.example.student_manager.data
//data class Student(
//    val id: Long? = null,
//    val name: String,
//    val email: String,
//    val course: String
//)

data class Student(
    val id: Long? = null,
    val rollNumber: String,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val course: String,
    val semester: Int,
    val division: String
)