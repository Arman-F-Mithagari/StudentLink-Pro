package com.example.student_manager.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.student_manager.data.Attendance
import com.example.student_manager.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AttendanceViewModel : ViewModel() {

    private val _attendanceList =
        MutableStateFlow<List<Attendance>>(emptyList())
    val attendanceList: StateFlow<List<Attendance>>
            = _attendanceList

    fun getAllAttendance() {
        viewModelScope.launch {
            try {
                _attendanceList.value =
                    RetrofitInstance.attendanceApi
                        .getAllAttendance()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getAttendanceByStudentId(studentId: Int) {
        viewModelScope.launch {
            try {
                _attendanceList.value =
                    RetrofitInstance.attendanceApi
                        .getAttendanceByStudentId(studentId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun saveAttendance(attendance: Attendance) {
        viewModelScope.launch {
            try {

                Log.d("ATTENDANCE", "Sending to API: $attendance")

                val response = RetrofitInstance.attendanceApi
                    .saveAttendance(attendance)

                Log.d("ATTENDANCE", "Code = ${response.code()}")

                getAllAttendance()

            } catch (e: Exception) {

                Log.e(
                    "ATTENDANCE",
                    "ERROR: ${e.message}",
                    e
                )
            }
        }
    }

    fun updateAttendance(
        id: Long,
        attendance: Attendance
    ) {
        viewModelScope.launch {
            try {

                RetrofitInstance.attendanceApi
                    .updateAttendance(
                        id,
                        attendance
                    )

                getAllAttendance()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun getAttendanceByDate(date: String) {
        viewModelScope.launch {
            try {

                _attendanceList.value =
                    RetrofitInstance.attendanceApi
                        .getAttendanceByDate(date)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteAttendance(id: Long) {
        viewModelScope.launch {
            try {
                RetrofitInstance.attendanceApi
                    .deleteAttendance(id)

                getAllAttendance()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}