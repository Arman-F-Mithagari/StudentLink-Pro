package com.example.student_manager.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.student_manager.data.Student
import com.example.student_manager.network.RetrofitInstance
import kotlinx.coroutines.launch

class StudentViewModel : ViewModel() {

    val studentList = MutableLiveData<List<Student>>(emptyList())
    val message = MutableLiveData<String>()
    val addMessage = MutableLiveData<String>()
    val updateMessage = MutableLiveData<String>()
    val deleteMessage = MutableLiveData<String>()

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

