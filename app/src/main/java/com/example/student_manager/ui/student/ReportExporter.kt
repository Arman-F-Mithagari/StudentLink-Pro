package com.example.student_manager.ui.student

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import com.example.student_manager.data.ExportReport
import java.io.BufferedOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object ReportExporter {

    fun writePdf(
        context: Context,
        uri: Uri,
        report: ExportReport
    ) {
        val document = PdfDocument()
        val pageWidth = 595
        val pageHeight = 842
        val margin = 40f
        val lineHeight = 24f

        val titlePaint = Paint().apply {
            textSize = 20f
            isFakeBoldText = true
        }

        val textPaint = Paint().apply {
            textSize = 10f
        }

        val headerPaint = Paint().apply {
            textSize = 10f
            isFakeBoldText = true
        }

        var page: PdfDocument.Page? = null
        var canvas: android.graphics.Canvas? = null
        var pageNumber = 0
        var y = 0f

        fun newPage() {
            if (page != null) document.finishPage(page!!)

            pageNumber++
            val info = PdfDocument.PageInfo.Builder(
                pageWidth, pageHeight, pageNumber
            ).create()

            page = document.startPage(info)
            canvas = page!!.canvas
            y = 45f

            canvas!!.drawText(report.title, margin, y, titlePaint)
            y += 30f

            canvas!!.drawText(
                "Generated: ${java.text.SimpleDateFormat(
                    "dd MMM yyyy, HH:mm",
                    java.util.Locale.getDefault()
                ).format(java.util.Date())}",
                margin, y, textPaint
            )
            y += 30f

            canvas!!.drawLine(
                margin, y, pageWidth - margin, y, textPaint
            )
            y += 20f

            drawHeaders()
        }

        fun drawHeaders() {
            val currentCanvas = canvas ?: return
            val columnWidth =
                (pageWidth - margin * 2) / report.headers.size.coerceAtLeast(1)

            report.headers.forEachIndexed { index, header ->
                currentCanvas.drawText(
                    header.take(22),
                    margin + index * columnWidth,
                    y,
                    headerPaint
                )
            }

            y += lineHeight
            currentCanvas.drawLine(
                margin, y - 12, pageWidth - margin, y - 12, textPaint
            )
        }

        try {
            newPage()

            report.rows.forEach { row ->
                if (y > pageHeight - 45f) newPage()

                val columnWidth =
                    (pageWidth - margin * 2) /
                            report.headers.size.coerceAtLeast(1)

                row.take(report.headers.size).forEachIndexed { index, value ->
                    canvas?.drawText(
                        value.take(22),
                        margin + index * columnWidth,
                        y,
                        textPaint
                    )
                }

                y += lineHeight
            }

            if (report.rows.isEmpty()) {
                canvas?.drawText(
                    "No records available.",
                    margin,
                    y,
                    textPaint
                )
            }

            if (page != null) document.finishPage(page!!)

            context.contentResolver.openOutputStream(uri)?.use {
                document.writeTo(it)
            } ?: error("Unable to open the selected file.")

        } finally {
            document.close()
        }
    }

    private fun drawHeaders() {
        TODO("Not yet implemented")
    }

    fun writeXlsx(
        context: Context,
        uri: Uri,
        report: ExportReport
    ) {
        context.contentResolver.openOutputStream(uri)?.use { output ->
            ZipOutputStream(BufferedOutputStream(output)).use { zip ->

                fun addFile(path: String, content: String) {
                    zip.putNextEntry(ZipEntry(path))
                    zip.write(content.toByteArray(Charsets.UTF_8))
                    zip.closeEntry()
                }

                addFile(
                    "[Content_Types].xml",
                    """<?xml version="1.0" encoding="UTF-8"?>
                    <Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">
                      <Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/>
                      <Default Extension="xml" ContentType="application/xml"/>
                      <Override PartName="/xl/workbook.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml"/>
                      <Override PartName="/xl/worksheets/sheet1.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml"/>
                    </Types>""".trimIndent()
                )

                addFile(
                    "_rels/.rels",
                    """<?xml version="1.0" encoding="UTF-8"?>
                    <Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
                      <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="xl/workbook.xml"/>
                    </Relationships>""".trimIndent()
                )

                addFile(
                    "xl/workbook.xml",
                    """<?xml version="1.0" encoding="UTF-8"?>
                    <workbook xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main"
                      xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships">
                      <sheets><sheet name="Report" sheetId="1" r:id="rId1"/></sheets>
                    </workbook>""".trimIndent()
                )

                addFile(
                    "xl/_rels/workbook.xml.rels",
                    """<?xml version="1.0" encoding="UTF-8"?>
                    <Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
                      <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet" Target="worksheets/sheet1.xml"/>
                    </Relationships>""".trimIndent()
                )

                val allRows = listOf(report.headers) + report.rows
                val sheet = buildString {
                    append(
                        """<?xml version="1.0" encoding="UTF-8"?>
                        <worksheet xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main">
                        <sheetData>"""
                    )

                    allRows.forEachIndexed { rowIndex, row ->
                        append("""<row r="${rowIndex + 1}">""")

                        row.forEachIndexed { columnIndex, value ->
                            val cellRef =
                                columnName(columnIndex + 1) + (rowIndex + 1)

                            append(
                                """<c r="$cellRef" t="inlineStr"><is><t xml:space="preserve">${xmlEscape(value)}</t></is></c>"""
                            )
                        }

                        append("</row>")
                    }

                    append("</sheetData></worksheet>")
                }

                addFile("xl/worksheets/sheet1.xml", sheet)
            }
        } ?: error("Unable to open the selected file.")
    }

    private fun columnName(number: Int): String {
        var n = number
        var result = ""

        while (n > 0) {
            val remainder = (n - 1) % 26
            result = ('A' + remainder) + result
            n = (n - 1) / 26
        }

        return result
    }

    private fun xmlEscape(value: String): String =
        value.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;")
            .filter { it == '\t' || it == '\n' || it == '\r' || it >= ' ' }
}