package com.example.student_manager.network

import com.example.student_manager.data.Student
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface StudentApiService {

    @GET("students")
    suspend fun getStudents(): List<Student>

    @POST("students")
    suspend fun addStudent(@Body student: Student): Student

    @PUT("students/{id}")
    suspend fun updateStudent(
        @Path("id") id: Long,
        @Body student: Student
    ): Student

    @DELETE("students/{id}")
    suspend fun deleteStudent(
        @Path("id") id: Long
    ): retrofit2.Response<Unit>
}