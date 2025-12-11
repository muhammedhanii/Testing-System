package com.aiu.trips.service;

import com.aiu.trips.enums.ExportFormat;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Service for exporting reports to PDF and CSV formats
 */
@Service
public class ReportExportService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Export report data to the specified format
     * @param reportData Map containing report data
     * @param format Export format (PDF or CSV)
     * @param reportTitle Title of the report
     * @return Byte array of the exported report
     */
    public byte[] exportReport(Map<String, Object> reportData, ExportFormat format, String reportTitle) {
        if (format == ExportFormat.PDF) {
            return exportToPdf(reportData, reportTitle);
        } else if (format == ExportFormat.CSV) {
            return exportToCsv(reportData, reportTitle);
        } else if (format == ExportFormat.JSON) {
            return exportToJson(reportData);
        } else {
            throw new IllegalArgumentException("Unsupported export format: " + format);
        }
    }

    /**
     * Export report data to PDF format
     */
    private byte[] exportToPdf(Map<String, Object> reportData, String reportTitle) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Add title
            Paragraph title = new Paragraph(reportTitle)
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            // Add generation timestamp
            Paragraph timestamp = new Paragraph("Generated: " + LocalDateTime.now().format(DATE_FORMATTER))
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.RIGHT);
            document.add(timestamp);

            // Add space
            document.add(new Paragraph("\n"));

            // Create table with 2 columns (Key, Value)
            float[] columnWidths = {2, 3};
            Table table = new Table(columnWidths);
            table.setWidth(500);

            // Add header row
            table.addHeaderCell(new Cell().add(new Paragraph("Metric").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Value").setBold()));

            // Add data rows
            reportData.forEach((key, value) -> {
                table.addCell(new Cell().add(new Paragraph(formatKey(key))));
                table.addCell(new Cell().add(new Paragraph(formatValue(value))));
            });

            document.add(table);

            // Add footer
            document.add(new Paragraph("\n"));
            Paragraph footer = new Paragraph("AIU Trips & Events Management System")
                    .setFontSize(8)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(footer);

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF report: " + e.getMessage(), e);
        }
    }

    /**
     * Export report data to CSV format
     */
    private byte[] exportToCsv(Map<String, Object> reportData, String reportTitle) {
        try (StringWriter sw = new StringWriter();
             CSVPrinter csvPrinter = new CSVPrinter(sw, CSVFormat.DEFAULT
                     .withHeader("Metric", "Value"))) {

            // Add title as a comment
            csvPrinter.printComment(reportTitle);
            csvPrinter.printComment("Generated: " + LocalDateTime.now().format(DATE_FORMATTER));
            csvPrinter.println();

            // Add data rows
            for (Map.Entry<String, Object> entry : reportData.entrySet()) {
                csvPrinter.printRecord(formatKey(entry.getKey()), formatValue(entry.getValue()));
            }

            csvPrinter.flush();
            return sw.toString().getBytes();
        } catch (Exception e) {
            throw new RuntimeException("Error generating CSV report: " + e.getMessage(), e);
        }
    }

    /**
     * Export report data to JSON format (simple implementation)
     */
    private byte[] exportToJson(Map<String, Object> reportData) {
        try {
            StringBuilder json = new StringBuilder();
            json.append("{\n");
            json.append("  \"generatedAt\": \"").append(LocalDateTime.now().format(DATE_FORMATTER)).append("\",\n");
            json.append("  \"data\": {\n");

            int count = 0;
            int total = reportData.size();
            for (Map.Entry<String, Object> entry : reportData.entrySet()) {
                json.append("    \"").append(entry.getKey()).append("\": ");
                if (entry.getValue() instanceof String) {
                    json.append("\"").append(entry.getValue()).append("\"");
                } else {
                    json.append(entry.getValue());
                }
                if (++count < total) {
                    json.append(",");
                }
                json.append("\n");
            }

            json.append("  }\n");
            json.append("}\n");
            return json.toString().getBytes();
        } catch (Exception e) {
            throw new RuntimeException("Error generating JSON report: " + e.getMessage(), e);
        }
    }

    /**
     * Format key for display (convert camelCase to Title Case)
     */
    private String formatKey(String key) {
        // Convert camelCase to Title Case with spaces
        return key.replaceAll("([A-Z])", " $1")
                .trim()
                .substring(0, 1).toUpperCase() + key.replaceAll("([A-Z])", " $1").trim().substring(1);
    }

    /**
     * Format value for display
     */
    private String formatValue(Object value) {
        if (value == null) {
            return "N/A";
        }
        if (value instanceof Double || value instanceof Float) {
            return String.format("%.2f", value);
        }
        return value.toString();
    }
}
