package com.example.intelliwealth.treasury.budget.infrastructure.export;

import com.example.intelliwealth.treasury.budget.application.dto.BudgetResponseDTO;
import com.example.intelliwealth.treasury.budget.domain.model.BudgetStatus;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class BudgetExportService {

    private static final Font STATUS_FONT =
            FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);

    // Public API
    public void generate(HttpServletResponse response, Page<BudgetResponseDTO> budgets)
            throws IOException {

        Document document = new Document(PageSize.A4.rotate(), 20, 20, 20, 20);
        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        addTitle(document);
        addTable(document, budgets);
        addFooterStamp(document);
        document.close();

        writer.setPageEvent(new PdfPageEventHelper() {
            @Override
            public void onEndPage(PdfWriter writer, Document document) {
                ColumnText.showTextAligned(
                        writer.getDirectContent(),
                        Element.ALIGN_CENTER,
                        new Phrase("Page " + writer.getPageNumber()),
                        420, 20, 0
                );
            }
        });
    }

    // Title
    private void addTitle(Document document) throws DocumentException {
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15);

        Paragraph title = new Paragraph("Budget Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(18);

        document.add(title);
    }

    // Table
    private void addTable(Document document, Page<BudgetResponseDTO> budgets)
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

    // Header
    private void addHeader(PdfPTable table) {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.WHITE);
        Color headerBg = new Color(33, 150, 243);

        headerCell(table, "#", headerFont, headerBg);
        headerCell(table, "Budget", headerFont, headerBg);
        headerCell(table, "Category", headerFont, headerBg);
        headerCell(table, "From", headerFont, headerBg);
        headerCell(table, "To", headerFont, headerBg);
        headerCell(table, "Allocated", headerFont, headerBg);
        headerCell(table, "Spent", headerFont, headerBg);
        headerCell(table, "Balance", headerFont, headerBg);
        headerCell(table, "Status", headerFont, headerBg);
        headerCell(table, "Repeat", headerFont, headerBg);
        headerCell(table, "Notes", headerFont, headerBg);
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
    // Rows
    private void addRows(PdfPTable table, Page<BudgetResponseDTO> budgets) {
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        boolean alternate = false;
        int srNo = 1;
        for (BudgetResponseDTO b : budgets) {
            Color rowBg = alternate ? new Color(245, 247, 250) : Color.WHITE;
            alternate = !alternate;

            table.addCell(dataCell(String.valueOf(srNo++), bodyFont, rowBg));
            table.addCell(dataCell(value(b.getTitle()), bodyFont, rowBg));
            table.addCell(dataCell(value(b.getCategory().getLabel()), bodyFont, rowBg));

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

    // Cell Helpers
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


    private static final Map<BudgetStatus, Color> STATUS_COLORS = Map.of(
            BudgetStatus.SAFE, new Color(200, 230, 201),
            BudgetStatus.WARNING, new Color(255, 224, 178),
            BudgetStatus.EXCEEDED, new Color(255, 205, 210)
    );

    private PdfPCell statusCell(BudgetStatus status) {

        String text = status == null ? "-" : status.getLabel();

        PdfPCell cell = new PdfPCell(
                new Phrase(text, STATUS_FONT)
        );

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(6);
        cell.setMinimumHeight(24);

        Color bg = STATUS_COLORS.get(status);
        if (bg != null) {
            cell.setBackgroundColor(bg);
        }

        return cell;
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

    // Utils
    private String value(Object obj) {
        return obj == null ? "-" : obj.toString();
    }
}
