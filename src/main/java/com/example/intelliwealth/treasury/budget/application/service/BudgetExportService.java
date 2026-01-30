package com.example.intelliwealth.treasury.budget.application.service;

import com.example.intelliwealth.treasury.budget.application.dto.BudgetResponseDTO;
import com.example.intelliwealth.treasury.budget.domain.model.BudgetStatus;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.util.List;

@Service
public class BudgetExportService {

    // =========================
    // Public API
    // =========================
    public void generate(HttpServletResponse response, List<BudgetResponseDTO> budgets)
            throws IOException {

        Document document = new Document(PageSize.A4.rotate(), 20, 20, 20, 20);
        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        addWatermark(writer);
        addTitle(document);
        addTable(document, budgets);
        addFooterStamp(document);
        document.close();
    }

    // =========================
    // Title
    // =========================
    private void addTitle(Document document) throws DocumentException {
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15);

        Paragraph title = new Paragraph("Budget History", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(18);

        document.add(title);
    }

    // =========================
    // Table
    // =========================
    private void addTable(Document document, List<BudgetResponseDTO> budgets)
            throws DocumentException {

        PdfPTable table = new PdfPTable(11);
        table.setWidthPercentage(100);
        table.setSpacingBefore(8);
        table.setWidths(new float[]{
                1.2f, 3f, 2.4f, 2.4f, 2.4f,
                3f, 3f, 3f, 2f, 2f, 4f
        });

        addHeader(table);
        addRows(table, budgets);

        document.add(table);
    }

    // =========================
    // Header
    // =========================
    private void addHeader(PdfPTable table) {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.WHITE);
        Color headerBg = new Color(33, 150, 243);

        headerCell(table, "ID", headerFont, headerBg);
        headerCell(table, "Title", headerFont, headerBg);
        headerCell(table, "Category", headerFont, headerBg);
        headerCell(table, "Start Date", headerFont, headerBg);
        headerCell(table, "End Date", headerFont, headerBg);
        headerCell(table, "Allocated (₹)", headerFont, headerBg);
        headerCell(table, "Spent (₹)", headerFont, headerBg);
        headerCell(table, "Remaining (₹)", headerFont, headerBg);
        headerCell(table, "Status", headerFont, headerBg);
        headerCell(table, "Recurring", headerFont, headerBg);
        headerCell(table, "Note", headerFont, headerBg);
    }

    private void headerCell(PdfPTable table, String text, Font font, Color bg) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bg);
        cell.setPadding(7);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(28);
        table.addCell(cell);
    }

    // =========================
    // Rows
    // =========================
    private void addRows(PdfPTable table, List<BudgetResponseDTO> budgets) {
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        boolean alternate = false;

        for (BudgetResponseDTO b : budgets) {
            Color rowBg = alternate ? new Color(245, 247, 250) : Color.WHITE;
            alternate = !alternate;

            table.addCell(dataCell(value(b.getId()), bodyFont, rowBg));
            table.addCell(dataCell(value(b.getTitle()), bodyFont, rowBg));
            table.addCell(dataCell(value(b.getCategory()), bodyFont, rowBg));

            table.addCell(dataCell(value(b.getStartDate()), bodyFont, rowBg));
            table.addCell(dataCell(value(b.getEndDate()), bodyFont, rowBg));

            table.addCell(amountCell(b.getAmountAllocated(), bodyFont, rowBg));
            table.addCell(amountCell(b.getAmountSpent(), bodyFont, rowBg));
            table.addCell(amountCell(b.getRemainingAmount(), bodyFont, rowBg));

            table.addCell(statusCell(b.getStatus()));
            table.addCell(dataCell(b.isRecurring() ? "Yes" : "No", bodyFont, rowBg));
            table.addCell(dataCell(value(b.getNote()), bodyFont, rowBg));
        }
    }

    // =========================
    // Cell Helpers
    // =========================
    private PdfPCell dataCell(String value, Font font, Color bg) {
        PdfPCell cell = new PdfPCell(new Phrase(value, font));
        cell.setPadding(6);
        cell.setBackgroundColor(bg);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(24);
        return cell;
    }

    private PdfPCell amountCell(Object amount, Font font, Color bg) {
        PdfPCell cell = new PdfPCell(
                new Phrase(amount == null ? "₹ 0.00" : "₹ " + amount, font));
        cell.setPadding(6);
        cell.setBackgroundColor(bg);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(24);
        return cell;
    }

    private PdfPCell statusCell(BudgetStatus status) {
        Font statusFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        PdfPCell cell = new PdfPCell(new Phrase(
                status == null ? "-" : status.name(), statusFont));

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(6);
        cell.setMinimumHeight(24);

        if (status == null) return cell;

        switch (status) {
            case SAFE ->
                    cell.setBackgroundColor(new Color(200, 230, 201));
            case WARNING ->
                    cell.setBackgroundColor(new Color(255, 224, 178));
            case EXCEEDED ->
                    cell.setBackgroundColor(new Color(255, 205, 210));
        }

        return cell;
    }

    // =========================
    // Branding
    // =========================
    private void addWatermark(PdfWriter writer) {
        PdfContentByte canvas = writer.getDirectContentUnder();
        Font font = FontFactory.getFont(
                FontFactory.HELVETICA_BOLD, 60, new Color(235, 235, 235));

        Phrase watermark = new Phrase("INTELLI_WEALTH", font);

        ColumnText.showTextAligned(
                canvas,
                Element.ALIGN_CENTER,
                watermark,
                420, 300, 45
        );
    }

    private void addFooterStamp(Document document) throws DocumentException {
        Font footerFont = FontFactory.getFont(
                FontFactory.HELVETICA_BOLD, 9, new Color(120, 120, 120));

        Paragraph footer = new Paragraph(
                "Generated by Intelli_Wealth • Smart Finance, Simplified",
                footerFont
        );
        footer.setAlignment(Element.ALIGN_RIGHT);
        footer.setSpacingBefore(12);

        document.add(footer);
    }

    // =========================
    // Utils
    // =========================
    private String value(Object obj) {
        return obj == null ? "-" : obj.toString();
    }
}
