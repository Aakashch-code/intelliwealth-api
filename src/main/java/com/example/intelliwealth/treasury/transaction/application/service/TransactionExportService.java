package com.example.intelliwealth.treasury.transaction.application.service;

import com.example.intelliwealth.treasury.transaction.application.dto.TransactionResponse;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;
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

    public void generate(HttpServletResponse response,
                         List<TransactionResponse> transactions)
            throws IOException {

        Document document = new Document(PageSize.A4.rotate(), 20, 20, 20, 20);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        addTitle(document);
        addTable(document, transactions);

        document.close();
    }

    // ----------------------------------------------------
    // Title
    // ----------------------------------------------------
    private void addTitle(Document document) throws DocumentException {
        Font titleFont =
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);

        Paragraph title =
                new Paragraph("Transaction History Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(15);

        document.add(title);
    }

    // ----------------------------------------------------
    // Table
    // ----------------------------------------------------
    private void addTable(Document document,
                          List<TransactionResponse> transactions)
            throws DocumentException {

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1f, 2f, 4f, 3f, 3f, 2f, 2f});
        table.setSpacingBefore(10);

        addHeader(table);
        addRows(table, transactions);

        document.add(table);
    }

    // ----------------------------------------------------
    // Header
    // ----------------------------------------------------
    private void addHeader(PdfPTable table) {
        Font headerFont =
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
        headerFont.setColor(Color.WHITE);

        Color headerBg = new Color(33, 150, 243); // fintech blue

        headerCell(table, "ID", headerFont, headerBg);
        headerCell(table, "Type", headerFont, headerBg);
        headerCell(table, "Description", headerFont, headerBg);
        headerCell(table, "Category", headerFont, headerBg);
        headerCell(table, "Source", headerFont, headerBg);
        headerCell(table, "Amount (₹)", headerFont, headerBg);
        headerCell(table, "Date", headerFont, headerBg);
    }

    private void headerCell(PdfPTable table,
                            String text,
                            Font font,
                            Color bg) {

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
    private void addRows(PdfPTable table,
                         List<TransactionResponse> transactions) {

        Font normalFont =
                FontFactory.getFont(FontFactory.HELVETICA, 10);

        for (TransactionResponse t : transactions) {

            table.addCell(dataCell(value(t.getId()), normalFont));
            table.addCell(dataCell(value(t.getType()), normalFont));
            table.addCell(dataCell(value(t.getDescription()), normalFont));
            table.addCell(dataCell(value(t.getCategory()), normalFont));
            table.addCell(dataCell(value(t.getSource()), normalFont));

            table.addCell(amountCell(t));

            table.addCell(dataCell(formatDate(t), normalFont));
        }
    }

    private PdfPCell dataCell(String value, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(value, font));
        cell.setPadding(5);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    // ----------------------------------------------------
    // Amount (Red / Green)
    // ----------------------------------------------------
    private PdfPCell amountCell(TransactionResponse t) {

        Font amountFont =
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);

        if ("EXPENSE".equalsIgnoreCase(t.getType())) {
            amountFont.setColor(Color.RED);
        } else if ("INCOME".equalsIgnoreCase(t.getType())) {
            amountFont.setColor(new Color(0, 153, 0));
        } else {
            amountFont.setColor(Color.BLACK);
        }

        PdfPCell cell =
                new PdfPCell(new Phrase(formatAmount(t.getAmount()), amountFont));

        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        return cell;
    }

    // ----------------------------------------------------
    // Formatting Helpers
    // ----------------------------------------------------
    private String formatAmount(BigDecimal amount) {
        return amount == null ? "-" : INR.format(amount);
    }

    private String formatDate(TransactionResponse t) {
        return t.getTransactionDate() == null
                ? "-"
                : t.getTransactionDate().format(DATE_FORMAT);
    }

    private String value(Object o) {
        return o == null ? "-" : o.toString();
    }
}
