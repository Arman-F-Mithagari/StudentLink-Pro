package com.example.student_manager.network

import com.example.student_manager.data.Attendance
import retrofit2.http.*

interface AttendanceApiService {

    @POST("attendance")
    suspend fun saveAttendance(
        @Body attendance: Attendance
    ): retrofit2.Response<Unit>

    @GET("attendance")
    suspend fun getAllAttendance(): List<Attendance>

    @GET("attendance/student/{studentId}")
    suspend fun getAttendanceByStudentId(
        @Path("studentId") studentId: Int
    ): List<Attendance>

    @GET("attendance/date/{date}")
    suspend fun getAttendanceByDate(
        @Path("date") date: String
    ): List<Attendance>

    @PUT("attendance/{id}")
    suspend fun updateAttendance(
        @Path("id") id: Long,
        @Body attendance: Attendance
    ): Attendance

    @DELETE("attendance/{id}")
    suspend fun deleteAttendance(
        @Path("id") id: Long
    )
}