package com.example.student_manager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.student_manager.data.DailyAttendance
import com.example.student_manager.data.MonthlyAttendance
import com.example.student_manager.data.ReportData
import com.example.student_manager.data.Student
import com.example.student_manager.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StudentViewModel : ViewModel() {
    private val _reportData = MutableLiveData<ReportData>()
    val reportData: LiveData<ReportData> = _reportData
    val studentList = MutableLiveData<List<Student>>(emptyList())
    val message = MutableLiveData<String>()
    val addMessage = MutableLiveData<String>()
    val updateMessage = MutableLiveData<String>()
    val deleteMessage = MutableLiveData<String>()
    private val _monthlyAttendance =
        MutableStateFlow<List<MonthlyAttendance>>(emptyList())
    val monthlyAttendance = _monthlyAttendance.asStateFlow()

    private val _dailyAttendance =
        MutableStateFlow<List<DailyAttendance>>(emptyList())
    val dailyAttendance = _dailyAttendance.asStateFlow()

    fun getStudents() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getStudents()
                studentList.value = response
            } catch (e: Exception) {
                message.value = "Failed to load students: ${e.message}"
            }
        }
    }

    fun addStudent(student: Student) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.addStudent(student)
                addMessage.value = "Student added successfully: ${response.name}"
                getStudents()
            } catch (e: Exception) {
                addMessage.value = "Failed to add student: ${e.message}"
            }
        }
    }

    fun updateStudent(id: Long, student: Student) {
        viewModelScope.launch {
            try {
                RetrofitInstance.api.updateStudent(id, student)
                message.value = "Student updated successfully"
                getStudents()
            } catch (e: Exception) {
                message.value = "Failed to update student: ${e.message}"
            }
        }
    }

    fun getReportSummary() {
        viewModelScope.launch {
            try {
                _reportData.value =
                    RetrofitInstance.api.getReportSummary()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getMonthlyAttendance() {
        viewModelScope.launch {
            try {
                _monthlyAttendance.value =
                    RetrofitInstance.api.getMonthlyAttendance()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getDailyAttendance() {
        viewModelScope.launch {
            try {
                _dailyAttendance.value =
                    RetrofitInstance.api.getDailyAttendance()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteStudent(id: Long) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.deleteStudent(id)

                if (response.isSuccessful) {
                    deleteMessage.value = "Student deleted successfully"
                    getStudents()
                } else {
                    deleteMessage.value = "Failed to delete student"
                }

            } catch (e: Exception) {
                deleteMessage.value = "Failed to delete student: ${e.message}"
            }
        }
    }
}

