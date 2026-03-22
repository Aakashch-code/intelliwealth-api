package com.example.intelliwealth.treasury.goal.infrastructure.export;

import com.example.intelliwealth.treasury.goal.application.dto.GoalResponse;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class GoalExportService {

    private static final Locale INDIA = new Locale("en", "IN");
    private static final NumberFormat INR = NumberFormat.getCurrencyInstance(INDIA);
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd MMM yyyy");

    private static final Font STATUS_FONT =
            FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);

    // ----------------------------------------------------
    // Public API
    // ----------------------------------------------------
    public void generate(HttpServletResponse response,
                         Page<GoalResponse> goals) throws IOException {

        Document document = new Document(PageSize.A4.rotate(), 20, 20, 20, 20);
        PdfWriter writer = PdfWriter.getInstance(document,
                response.getOutputStream());

        document.open();

        addTitle(document);
        addTable(document, goals);
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

    // ----------------------------------------------------
    // Title
    // ----------------------------------------------------
    private void addTitle(Document document)
            throws DocumentException {

        Font titleFont =
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15);

        Paragraph title =
                new Paragraph("Goal Report", titleFont);

        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(18);

        document.add(title);
    }

    // ----------------------------------------------------
    // Table
    // ----------------------------------------------------
    private void addTable(Document document,
                          Page<GoalResponse> goals)
            throws DocumentException {

        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);
        table.setSpacingBefore(8);

        table.setWidths(new float[]{
                1f, 3f, 2f, 2.5f,
                2.5f, 1.5f, 2.5f, 1.5f
        });

        addHeader(table);
        addRows(table, goals);

        document.add(table);
    }

    // ----------------------------------------------------
    // Header
    // ----------------------------------------------------
    private void addHeader(PdfPTable table) {

        Font headerFont =
                FontFactory.getFont(FontFactory.HELVETICA_BOLD,
                        11, Color.WHITE);

        Color headerBg = new Color(33, 150, 243);

        headerCell(table, "#", headerFont, headerBg);
        headerCell(table, "Goal", headerFont, headerBg);
        headerCell(table, "Priority", headerFont, headerBg);
        headerCell(table, "Target", headerFont, headerBg);
        headerCell(table, "Saved", headerFont, headerBg);
        headerCell(table, "Progress", headerFont, headerBg);
        headerCell(table, "Target Date", headerFont, headerBg);
        headerCell(table, "Status", headerFont, headerBg);
    }

    private void headerCell(PdfPTable table,
                            String text,
                            Font font,
                            Color bg) {

        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bg);
        cell.setPadding(7);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(28);

        table.addCell(cell);
    }

    // ----------------------------------------------------
    // Rows
    // ----------------------------------------------------
    private void addRows(PdfPTable table,
                         Page<GoalResponse> goals) {

        Font bodyFont =
                FontFactory.getFont(FontFactory.HELVETICA, 10);

        boolean alternate = false;
        int srNo = 1;

        for (GoalResponse g : goals) {

            Color rowBg =
                    alternate ? new Color(245, 247, 250)
                            : Color.WHITE;

            alternate = !alternate;

            table.addCell(dataCell(
                    String.valueOf(srNo++),
                    bodyFont, rowBg));

            table.addCell(dataCell(
                    value(g.getName()),
                    bodyFont, rowBg));

            table.addCell(priorityCell(
                    g.getPriority(), rowBg));

            table.addCell(amountCell(
                    g.getTargetAmount(),
                    bodyFont, rowBg));

            table.addCell(amountCell(
                    g.getCurrentAmount(),
                    bodyFont, rowBg));

            table.addCell(progressCell(
                    g, bodyFont, rowBg));

            table.addCell(dataCell(
                    formatDate(g),
                    bodyFont, rowBg));

            table.addCell(statusCell(
                    g.isStatus(), rowBg));
        }
    }

    // ----------------------------------------------------
    // Cell Helpers
    // ----------------------------------------------------

    private PdfPCell dataCell(String value,
                              Font font,
                              Color bg) {

        PdfPCell cell =
                new PdfPCell(new Phrase(value, font));

        cell.setPadding(6);
        cell.setBackgroundColor(bg);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(24);

        return cell;
    }

    private PdfPCell amountCell(BigDecimal amount,
                                Font font,
                                Color bg) {

        PdfPCell cell =
                new PdfPCell(new Phrase(
                        formatAmount(amount), font));

        cell.setPadding(6);
        cell.setBackgroundColor(bg);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(24);

        return cell;
    }

    private PdfPCell priorityCell(String priority,
                                  Color bg) {

        Font font =
                FontFactory.getFont(
                        FontFactory.HELVETICA_BOLD, 10);

        if ("HIGH".equalsIgnoreCase(priority)) {
            font.setColor(Color.RED);
        } else if ("MEDIUM".equalsIgnoreCase(priority)) {
            font.setColor(Color.ORANGE);
        } else if ("LOW".equalsIgnoreCase(priority)) {
            font.setColor(new Color(0, 153, 0));
        }

        PdfPCell cell =
                new PdfPCell(new Phrase(
                        value(priority), font));

        cell.setPadding(6);
        cell.setBackgroundColor(bg);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(24);

        return cell;
    }

    private PdfPCell progressCell(GoalResponse g,
                                  Font font,
                                  Color bg) {

        BigDecimal target = g.getTargetAmount();
        BigDecimal current = g.getCurrentAmount();

        String percentStr = "0%";

        if (target != null &&
                target.compareTo(BigDecimal.ZERO) > 0 &&
                current != null) {

            BigDecimal percent =
                    current.divide(target, 2,
                                    RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100));

            percentStr = percent.intValue() + "%";
        }

        PdfPCell cell =
                new PdfPCell(new Phrase(percentStr, font));

        cell.setPadding(6);
        cell.setBackgroundColor(bg);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(24);

        return cell;
    }

    private PdfPCell statusCell(boolean active,
                                Color bg) {

        String text = active ? "Active" : "Completed";

        PdfPCell cell =
                new PdfPCell(new Phrase(text, STATUS_FONT));

        cell.setPadding(6);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(24);

        if (active) {
            cell.setBackgroundColor(new Color(200, 230, 201));
        } else {
            cell.setBackgroundColor(new Color(220, 220, 220));
        }

        return cell;
    }

    // ----------------------------------------------------
    // Footer
    // ----------------------------------------------------
    private void addFooterStamp(Document document)
            throws DocumentException {

        Font footerFont =
                FontFactory.getFont(
                        FontFactory.HELVETICA_BOLD,
                        9,
                        new Color(120, 120, 120));

        Paragraph footer =
                new Paragraph(
                        "Generated by Intelli_Wealth • Smart Finance, Simplified",
                        footerFont);

        footer.setAlignment(Element.ALIGN_RIGHT);
        footer.setSpacingBefore(12);

        document.add(footer);
    }

    // ----------------------------------------------------
    // Utils
    // ----------------------------------------------------
    private String formatAmount(BigDecimal amount) {
        return amount == null ? "₹ 0.00" : INR.format(amount);
    }

    private String formatDate(GoalResponse g) {
        return g.getTargetDate() == null ?
                "-" :
                g.getTargetDate().format(DATE_FORMAT);
    }

    private String value(Object obj) {
        return obj == null ? "-" : obj.toString();
    }
}
