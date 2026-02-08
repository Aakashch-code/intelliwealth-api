package com.example.intelliwealth.treasury.goal.application.service;

import com.example.intelliwealth.treasury.goal.application.dto.GoalResponseDTO;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class GoalExportService {

    private static final Locale INDIA = new Locale("en", "IN");
    private static final NumberFormat INR = NumberFormat.getCurrencyInstance(INDIA);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    public void generate(HttpServletResponse response, List<GoalResponseDTO> goals) throws IOException {

        // Setup Document (A4 Landscape)
        Document document = new Document(PageSize.A4.rotate(), 20, 20, 20, 20);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        addTitle(document);
        addTable(document, goals);

        document.close();
    }

    // ----------------------------------------------------
    // Title
    // ----------------------------------------------------
    private void addTitle(Document document) throws DocumentException {
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);

        Paragraph title = new Paragraph("Financial Goals Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(15);

        document.add(title);
    }

    // ----------------------------------------------------
    // Table Setup
    // ----------------------------------------------------
    private void addTable(Document document, List<GoalResponseDTO> goals) throws DocumentException {

        // 8 Columns
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);

        // Widths: Sr.No, Name, Priority, Target, Saved, Progress, Date, Status
        table.setWidths(new float[]{1f, 3f, 2f, 2.5f, 2.5f, 1.5f, 2.5f, 1.5f});
        table.setSpacingBefore(10);

        addHeader(table);
        addRows(table, goals);

        document.add(table);
    }

    // ----------------------------------------------------
    // Header
    // ----------------------------------------------------
    private void addHeader(PdfPTable table) {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
        headerFont.setColor(Color.WHITE);

        Color headerBg = new Color(33, 150, 243); // Fintech Blue

        headerCell(table, "Sr. No", headerFont, headerBg);
        headerCell(table, "Goal Name", headerFont, headerBg);
        headerCell(table, "Priority", headerFont, headerBg);
        headerCell(table, "Target (₹)", headerFont, headerBg);
        headerCell(table, "Saved (₹)", headerFont, headerBg);
        headerCell(table, "Progress", headerFont, headerBg);
        headerCell(table, "Target Date", headerFont, headerBg);
        headerCell(table, "Status", headerFont, headerBg);
    }

    private void headerCell(PdfPTable table, String text, Font font, Color bg) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bg);
        cell.setPadding(6);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
    }

    // ----------------------------------------------------
    // Rows
    // ----------------------------------------------------
    private void addRows(PdfPTable table, List<GoalResponseDTO> goals) {
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

        int srNo = 1;

        for (GoalResponseDTO g : goals) {
            // 1. Sr No (Generated)
            table.addCell(dataCell(String.valueOf(srNo++), normalFont));

            // 2. Name
            table.addCell(dataCell(value(g.getName()), normalFont));

            // 3. Priority (Color Coded)
            table.addCell(priorityCell(g.getPriority()));

            // 4. Target Amount
            table.addCell(amountCell(g.getTargetAmount()));

            // 5. Current (Saved) Amount
            table.addCell(amountCell(g.getCurrentAmount()));

            // 6. Progress %
            table.addCell(progressCell(g));

            // 7. Target Date
            table.addCell(dataCell(formatDate(g), normalFont));

            // 8. Status
            table.addCell(statusCell(g.isStatus()));
        }
    }

    // ----------------------------------------------------
    // Cell Builders
    // ----------------------------------------------------

    private PdfPCell dataCell(String value, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(value, font));
        cell.setPadding(5);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        // Center text slightly for better readability
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    private PdfPCell amountCell(BigDecimal amount) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        PdfPCell cell = new PdfPCell(new Phrase(formatAmount(amount), font));
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    private PdfPCell priorityCell(String priority) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);

        if (priority != null) {
            if ("HIGH".equalsIgnoreCase(priority)) {
                font.setColor(Color.RED);
            } else if ("MEDIUM".equalsIgnoreCase(priority)) {
                font.setColor(Color.ORANGE);
            } else if ("LOW".equalsIgnoreCase(priority)) {
                font.setColor(new Color(0, 153, 0)); // Dark Green
            } else {
                font.setColor(Color.BLACK);
            }
        }

        PdfPCell cell = new PdfPCell(new Phrase(value(priority), font));
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    private PdfPCell progressCell(GoalResponseDTO g) {
        BigDecimal target = g.getTargetAmount();
        BigDecimal current = g.getCurrentAmount();
        String percentStr = "0%";

        if (target != null && target.compareTo(BigDecimal.ZERO) > 0 && current != null) {
            BigDecimal percent = current.divide(target, 2, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(100));
            percentStr = percent.intValue() + "%";
        }

        PdfPCell cell = new PdfPCell(new Phrase(percentStr, FontFactory.getFont(FontFactory.HELVETICA, 10)));
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    private PdfPCell statusCell(boolean isActive) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);

        String text = isActive ? "Active" : "Completed";

        if (!isActive) {
            font.setColor(Color.GRAY);
        } else {
            font.setColor(new Color(0, 102, 204)); // Darker Blue
        }

        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    // ----------------------------------------------------
    // Formatting Helpers
    // ----------------------------------------------------
    private String formatAmount(BigDecimal amount) {
        return amount == null ? "-" : INR.format(amount);
    }

    private String formatDate(GoalResponseDTO g) {
        return g.getTargetDate() == null ? "-" : g.getTargetDate().format(DATE_FORMAT);
    }

    private String value(Object o) {
        return o == null ? "-" : o.toString();
    }
}