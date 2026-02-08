package com.example.intelliwealth.protection.insurance.application.service;

import com.example.intelliwealth.protection.insurance.application.dto.InsuranceResponseDTO;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class InsuranceExportService {

    // --- Configuration ---
    private static final Locale INDIA = new Locale("en", "IN");
    private static final NumberFormat INR = NumberFormat.getCurrencyInstance(INDIA);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    // --- Colors (Teal / Protection Theme) ---
    private static final Color COL_PRIMARY_BG = new Color(0, 128, 128);   // Teal (Distinct from Asset Blue)
    private static final Color COL_HEADER_TXT = Color.WHITE;
    private static final Color COL_LABEL_TXT = new Color(100, 100, 100);  // Dark Gray
    private static final Color COL_VALUE_TXT = Color.BLACK;
    private static final Color COL_CARD_BG = Color.WHITE;
    private static final Color COL_BORDER = new Color(220, 220, 220);

    // --- Fonts ---
    private static final Font FONT_TITLE = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, COL_PRIMARY_BG);
    private static final Font FONT_CARD_HEADER = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, COL_HEADER_TXT);
    private static final Font FONT_CARD_AMOUNT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15, new Color(255, 255, 224)); // Light Yellow for contrast
    private static final Font FONT_LABEL = FontFactory.getFont(FontFactory.HELVETICA, 9, COL_LABEL_TXT);
    private static final Font FONT_VALUE = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, COL_VALUE_TXT);
    private static final Font FONT_SECTION_HEADER = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, COL_PRIMARY_BG);


    public void generate(HttpServletResponse response, List<InsuranceResponseDTO> policies) throws IOException {
        // A4 with moderate margins
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        addReportTitle(document);

        int count = 0;
        for (InsuranceResponseDTO policy : policies) {
            // New Page Logic: Trigger every 2 items
            if (count > 0 && count % 2 == 0) {
                document.newPage();
            }

            addInsuranceCard(document, policy);
            count++;
        }

        document.close();
    }

    private void addReportTitle(Document document) throws DocumentException {
        Paragraph p = new Paragraph("Insurance Coverage Report", FONT_TITLE);
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingAfter(30);
        document.add(p);
    }

    private void addInsuranceCard(Document document, InsuranceResponseDTO policy) throws DocumentException {
        // Container Table (The Card)
        PdfPTable card = new PdfPTable(1);
        card.setWidthPercentage(100);
        card.setKeepTogether(true);
        card.setSpacingAfter(40); // Large gap

        // 1. Header Section (Provider/Name + Coverage Amount)
        PdfPCell headerCell = new PdfPCell();
        headerCell.setBackgroundColor(COL_PRIMARY_BG);
        headerCell.setPadding(12);
        headerCell.setBorderColor(COL_PRIMARY_BG);

        PdfPTable headerContent = new PdfPTable(2);
        headerContent.setWidthPercentage(100);
        headerContent.setWidths(new float[]{3f, 1.5f}); // Name gets more space

        // Name & Provider
        String titleText = value(policy.getProvider()) + " - " + value(policy.getName());
        PdfPCell nameCell = new PdfPCell(new Phrase(titleText.toUpperCase(), FONT_CARD_HEADER));
        nameCell.setBorder(Rectangle.NO_BORDER);
        nameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        headerContent.addCell(nameCell);

        // Coverage Amount (Sum Assured)
        String coverText = "Cover: " + formatAmount(policy.getCoverageAmount());
        PdfPCell priceCell = new PdfPCell(new Phrase(coverText, FONT_CARD_AMOUNT));
        priceCell.setBorder(Rectangle.NO_BORDER);
        priceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        priceCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        headerContent.addCell(priceCell);

        headerCell.addElement(headerContent);
        card.addCell(headerCell);

        // 2. Body Section
        PdfPCell bodyCell = new PdfPCell();
        bodyCell.setBackgroundColor(COL_CARD_BG);
        bodyCell.setBorderColor(COL_BORDER);
        bodyCell.setPadding(15);
        bodyCell.setPaddingBottom(20);

        // Core Details (Premium, Dates)
        bodyCell.addElement(createCoreDetailsTable(policy));

        // Separator Line
        bodyCell.addElement(createSeparator());

        // Dynamic Attributes
        bodyCell.addElement(createAttributesGrid(policy.getAttributes()));

        card.addCell(bodyCell);
        document.add(card);
    }

    // --- Sub-components ---

    private PdfPTable createCoreDetailsTable(InsuranceResponseDTO policy) {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingAfter(5);

        // Row 1: Category | Premium
        addKvCell(table, "Category", value(policy.getMainCategory()) + " (" + value(policy.getCategory()) + ")");
        addKvCell(table, "Premium Amount", formatAmount(policy.getPremiumAmount()));

        // Row 2: Start Date | End Date
        addKvCell(table, "Policy Start Date", formatDate(policy.getStartDate()));
        addKvCell(table, "Policy End Date", formatDate(policy.getEndDate()));

        return table;
    }

    private PdfPTable createAttributesGrid(Map<String, Object> attributes) {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1.5f, 2f, 1.5f, 2f});
        table.setSpacingBefore(10);

        if (attributes == null || attributes.isEmpty()) {
            return table;
        }

        // Section Header
        PdfPCell titleCell = new PdfPCell(new Phrase("Policy Details & Benefits", FONT_SECTION_HEADER));
        titleCell.setColspan(4);
        titleCell.setBorder(Rectangle.NO_BORDER);
        titleCell.setPaddingBottom(8);
        table.addCell(titleCell);

        int count = 0;
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            addKvCell(table, beautify(entry.getKey()), formatAttributeValue(entry.getKey(), entry.getValue()));
            count++;
        }

        if (count % 2 != 0) {
            addKvCell(table, "", "");
        }

        return table;
    }

    private void addKvCell(PdfPTable table, String key, String val) {
        PdfPCell label = new PdfPCell(new Phrase(key, FONT_LABEL));
        label.setBorder(Rectangle.NO_BORDER);
        label.setPaddingTop(5);
        label.setPaddingBottom(5);
        table.addCell(label);

        PdfPCell value = new PdfPCell(new Phrase(val, FONT_VALUE));
        value.setBorder(Rectangle.NO_BORDER);
        value.setPaddingTop(5);
        value.setPaddingBottom(5);
        table.addCell(value);
    }

    private Paragraph createSeparator() {
        Paragraph p = new Paragraph(" ");
        p.setLeading(5);
        PdfPTable line = new PdfPTable(1);
        line.setWidthPercentage(100);
        PdfPCell c = new PdfPCell(new Phrase(" "));
        c.setBorder(Rectangle.BOTTOM);
        c.setBorderColor(new Color(240,240,240));
        c.setBorderWidth(1f);
        line.addCell(c);
        return p;
    }

    // --- Utilities ---

    private String formatAttributeValue(String key, Object value) {
        if (value == null) return "-";
        String k = key.toLowerCase();
        if (k.contains("rate") || k.contains("tax") || k.contains("bonus")) return value + "%";
        if (k.contains("amount") || k.contains("cost") || k.contains("premium")) {
            try {
                return INR.format(new BigDecimal(value.toString()));
            } catch (Exception e) { return value.toString(); }
        }
        return value.toString();
    }

    private String formatAmount(BigDecimal amount) {
        return amount == null ? "-" : INR.format(amount);
    }

    private String formatDate(LocalDate d) {
        return d == null ? "-" : d.format(DATE_FORMAT);
    }

    private String value(Object o) {
        return o == null ? "-" : o.toString();
    }

    private String beautify(String key) {
        if (key == null || key.isEmpty()) return "";
        return key.substring(0, 1).toUpperCase() + key.substring(1).replace("_", " ").toLowerCase();
    }
}