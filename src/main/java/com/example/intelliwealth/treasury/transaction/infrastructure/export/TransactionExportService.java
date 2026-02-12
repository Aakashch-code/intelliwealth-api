package com.example.intelliwealth.treasury.transaction.infrastructure.export;

import com.example.intelliwealth.treasury.transaction.application.dto.TransactionResponse;
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
import java.util.List;
import java.util.Locale;
@Service
public class TransactionExportService {

    private static final Locale INDIA = new Locale("en", "IN");
    private static final NumberFormat INR =
            NumberFormat.getCurrencyInstance(INDIA);

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd MMM yyyy");

    private static final Font AMOUNT_FONT =
            FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);

    // ----------------------------------------------------
    // Public API
    // ----------------------------------------------------
    public void generate(HttpServletResponse response,
                         Page<TransactionResponse> transactions)
            throws IOException {

        Document document =
                new Document(PageSize.A4.rotate(),
                        20, 20, 20, 20);

        PdfWriter writer =
                PdfWriter.getInstance(document,
                        response.getOutputStream());

        document.open();

        addTitle(document);
        addTable(document, transactions);
        addFooterStamp(document);

        document.close();

        // Page Number Footer
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
                new Paragraph("Transaction History Report", titleFont);

        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(18);

        document.add(title);
    }

    // ----------------------------------------------------
    // Table
    // ----------------------------------------------------
    private void addTable(Document document,
                          Page<TransactionResponse> transactions)
            throws DocumentException {

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setSpacingBefore(8);

        table.setWidths(new float[]{
                1f, 2f, 4f, 3f, 3f, 2f, 2f
        });

        addHeader(table);
        addRows(table, transactions);

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
        headerCell(table, "Type", headerFont, headerBg);
        headerCell(table, "Description", headerFont, headerBg);
        headerCell(table, "Category", headerFont, headerBg);
        headerCell(table, "Source", headerFont, headerBg);
        headerCell(table, "Amount", headerFont, headerBg);
        headerCell(table, "Date", headerFont, headerBg);
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
                         Page<TransactionResponse> transactions) {

        Font bodyFont =
                FontFactory.getFont(FontFactory.HELVETICA, 10);

        boolean alternate = false;
        int srNo = 1;

        for (TransactionResponse t : transactions) {

            Color rowBg =
                    alternate
                            ? new Color(245, 247, 250)
                            : Color.WHITE;

            alternate = !alternate;

            table.addCell(dataCell(
                    String.valueOf(srNo++),
                    bodyFont, rowBg));

            table.addCell(dataCell(
                    value(t.getType()),
                    bodyFont, rowBg));

            table.addCell(dataCell(
                    value(t.getDescription()),
                    bodyFont, rowBg));

            table.addCell(dataCell(
                    value(t.getCategory()),
                    bodyFont, rowBg));

            table.addCell(dataCell(
                    value(t.getSource()),
                    bodyFont, rowBg));

            table.addCell(amountCell(t, rowBg));

            table.addCell(dataCell(
                    formatDate(t),
                    bodyFont, rowBg));
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

    // ----------------------------------------------------
    // Amount Cell (Colored + Background)
    // ----------------------------------------------------
    private PdfPCell amountCell(TransactionResponse t,
                                Color bg) {

        Font font =
                FontFactory.getFont(
                        FontFactory.HELVETICA_BOLD, 10);

        Color cellBg = bg;

        if ("EXPENSE".equalsIgnoreCase(t.getType())) {

            font.setColor(Color.RED);
            cellBg = new Color(255, 205, 210); // Light Red

        } else if ("INCOME".equalsIgnoreCase(t.getType())) {

            font.setColor(new Color(0, 153, 0));
            cellBg = new Color(200, 230, 201); // Light Green
        }

        PdfPCell cell =
                new PdfPCell(new Phrase(
                        formatAmount(t.getAmount()), font));

        cell.setPadding(6);
        cell.setBackgroundColor(cellBg);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(24);

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

    private String formatDate(TransactionResponse t) {
        return t.getTransactionDate() == null
                ? "-"
                : t.getTransactionDate().format(DATE_FORMAT);
    }

    private String value(Object obj) {
        return obj == null ? "-" : obj.toString();
    }
}
