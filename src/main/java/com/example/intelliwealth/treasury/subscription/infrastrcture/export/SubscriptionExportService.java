package com.example.intelliwealth.treasury.subscription.infrastrcture.export;

import com.example.intelliwealth.treasury.subscription.application.dto.SubscriptionResponse;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
@Service
public class SubscriptionExportService {

    private static final Locale INDIA = new Locale("en", "IN");
    private static final NumberFormat INR =
            NumberFormat.getCurrencyInstance(INDIA);

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd MMM yyyy");

    private static final Font STATUS_FONT =
            FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);

    // ----------------------------------------------------
    // Public API
    // ----------------------------------------------------
    public void generate(HttpServletResponse response,
                         Page<SubscriptionResponse> subscriptions)
            throws IOException {

        Document document =
                new Document(PageSize.A4.rotate(),
                        20, 20, 20, 20);

        PdfWriter writer =
                PdfWriter.getInstance(document,
                        response.getOutputStream());

        document.open();

        addTitle(document);
        addTable(document, subscriptions);
        addFooterStamp(document);

        document.close();

        // Page Numbers
        writer.setPageEvent(new PdfPageEventHelper() {
            @Override
            public void onEndPage(PdfWriter writer,
                                  Document document) {

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
                FontFactory.getFont(
                        FontFactory.HELVETICA_BOLD, 15);

        Paragraph title =
                new Paragraph("Subscription Report", titleFont);

        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(18);

        document.add(title);
    }

    // ----------------------------------------------------
    // Table
    // ----------------------------------------------------
    private void addTable(Document document,
                          Page<SubscriptionResponse> subscriptions)
            throws DocumentException {

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setSpacingBefore(8);

        table.setWidths(new float[]{
                1f, 3.5f, 2.5f, 2f,
                2.5f, 2.5f, 1.5f
        });

        addHeader(table);
        addRows(table, subscriptions);

        document.add(table);
    }

    // ----------------------------------------------------
    // Header
    // ----------------------------------------------------
    private void addHeader(PdfPTable table) {

        Font headerFont =
                FontFactory.getFont(
                        FontFactory.HELVETICA_BOLD,
                        11, Color.WHITE);

        Color headerBg = new Color(33, 150, 243);

        headerCell(table, "#", headerFont, headerBg);
        headerCell(table, "Title", headerFont, headerBg);
        headerCell(table, "Category", headerFont, headerBg);
        headerCell(table, "Cycle", headerFont, headerBg);
        headerCell(table, "Amount", headerFont, headerBg);
        headerCell(table, "Next Due", headerFont, headerBg);
        headerCell(table, "Status", headerFont, headerBg);
    }

    private void headerCell(PdfPTable table,
                            String text,
                            Font font,
                            Color bg) {

        PdfPCell cell =
                new PdfPCell(new Phrase(text, font));

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
                         Page<SubscriptionResponse> subscriptions) {

        Font bodyFont =
                FontFactory.getFont(FontFactory.HELVETICA, 10);

        boolean alternate = false;
        int srNo = 1;

        for (SubscriptionResponse sub : subscriptions) {

            Color rowBg =
                    alternate
                            ? new Color(245, 247, 250)
                            : Color.WHITE;

            alternate = !alternate;

            table.addCell(dataCell(
                    String.valueOf(srNo++),
                    bodyFont, rowBg));

            table.addCell(dataCell(
                    value(sub.getTitle()),
                    bodyFont, rowBg));

            table.addCell(dataCell(
                    value(sub.getCategory()),
                    bodyFont, rowBg));

            table.addCell(dataCell(
                    formatCycle(sub.getBillingCycle()),
                    bodyFont, rowBg));

            table.addCell(amountCell(
                    sub.getAmount(),
                    bodyFont, rowBg));

            table.addCell(dataCell(
                    formatDate(sub),
                    bodyFont, rowBg));

            table.addCell(statusCell(
                    sub.isActive()));
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

    private PdfPCell statusCell(boolean active) {

        String text = active ? "Active" : "Inactive";

        PdfPCell cell =
                new PdfPCell(new Phrase(text, STATUS_FONT));

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(6);
        cell.setMinimumHeight(24);

        if (active) {
            cell.setBackgroundColor(
                    new Color(200, 230, 201)); // Green
        } else {
            cell.setBackgroundColor(
                    new Color(224, 224, 224)); // Grey
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

    private String formatDate(SubscriptionResponse sub) {
        return sub.getNextRecurrence() == null
                ? "-"
                : sub.getNextRecurrence().format(DATE_FORMAT);
    }

    private String formatCycle(String cycle) {

        if (cycle == null || cycle.isEmpty()) {
            return "-";
        }

        return cycle.substring(0, 1).toUpperCase()
                + cycle.substring(1).toLowerCase();
    }

    private String value(Object obj) {
        return obj == null ? "-" : obj.toString();
    }
}
