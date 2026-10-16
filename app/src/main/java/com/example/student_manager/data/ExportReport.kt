package com.example.student_manager.data

data class ExportReport(
    val title: String,
    val headers: List<String>,
    val rows: List<List<String>>
)