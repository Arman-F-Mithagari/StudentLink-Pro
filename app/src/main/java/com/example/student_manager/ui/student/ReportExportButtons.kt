package com.example.student_manager.ui.student


import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.student_manager.data.ExportReport
import com.example.student_manager.ui.student.ReportExporter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ReportExportButtons(report: ExportReport) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    fun export(uri: android.net.Uri?, isPdf: Boolean) {
        if (uri == null) return

        scope.launch {
            val result = withContext(Dispatchers.IO) {
                runCatching {
                    if (isPdf) {
                        ReportExporter.writePdf(context, uri, report)
                    } else {
                        ReportExporter.writeXlsx(context, uri, report)
                    }
                }
            }

            Toast.makeText(
                context,
                if (result.isSuccess) {
                    "Report saved successfully"
                } else {
                    "Export failed: ${result.exceptionOrNull()?.message ?: "Unknown error"}"
                },
                Toast.LENGTH_LONG
            ).show()
        }
    }

    val pdfLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/pdf")
    ) { uri ->
        export(uri, true)
    }

    val excelLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        )
    ) { uri ->
        export(uri, false)
    }

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
            onClick = {
                pdfLauncher.launch(
                    "${report.title.replace(" ", "_")}.pdf"
                )
            }
        ) {
            Text("Export PDF")
        }

        OutlinedButton(
            onClick = {
                excelLauncher.launch(
                    "${report.title.replace(" ", "_")}.xlsx"
                )
            }
        ) {
            Text("Export Excel")
        }
    }
}