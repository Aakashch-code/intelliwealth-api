package com.example.intelliwealth.wealth.asset.infrastructure.export;

import com.example.intelliwealth.wealth.asset.application.dto.AssetsResponseDTO;
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
public class AssetExportService {

    // --- Configuration ---
    private static final Locale INDIA = new Locale("en", "IN");
    private static final NumberFormat INR = NumberFormat.getCurrencyInstance(INDIA);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    // --- Colors ---
    private static final Color COL_PRIMARY_BG = new Color(0, 51, 102);    // Navy Blue
    private static final Color COL_HEADER_TXT = Color.WHITE;
    private static final Color COL_LABEL_TXT = new Color(100, 100, 100); // Dark Gray
    private static final Color COL_VALUE_TXT = Color.BLACK;
    private static final Color COL_CARD_BG = Color.WHITE;
    private static final Color COL_BORDER = new Color(220, 220, 220);

    // --- Fonts ---
    private static final Font FONT_TITLE = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, COL_PRIMARY_BG);
    private static final Font FONT_CARD_HEADER = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, COL_HEADER_TXT);
    private static final Font FONT_CARD_AMOUNT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15, new Color(144, 238, 144)); // Light Green
    private static final Font FONT_LABEL = FontFactory.getFont(FontFactory.HELVETICA, 9, COL_LABEL_TXT);
    private static final Font FONT_VALUE = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, COL_VALUE_TXT);
    private static final Font FONT_SECTION_HEADER = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, COL_PRIMARY_BG);


    public void generate(HttpServletResponse response, List<AssetsResponseDTO> assets) throws IOException {
        // A4 with moderate margins
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        addReportTitle(document);

        int count = 0;
        for (AssetsResponseDTO asset : assets) {

            if (count > 0 && count % 2 == 0) {
                document.newPage();

            }

            addAssetCard(document, asset);
            count++;
        }

        document.close();
    }

    private void addReportTitle(Document document) throws DocumentException {
        Paragraph p = new Paragraph("Portfolio Valuation Report", FONT_TITLE);
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingAfter(30);
        document.add(p);
    }

    private void addAssetCard(Document document, AssetsResponseDTO asset) throws DocumentException {
        // Container Table (The Card)
        PdfPTable card = new PdfPTable(1);
        card.setWidthPercentage(100);
        card.setKeepTogether(true); // Try to prevent page breaks inside a card
        card.setSpacingAfter(40); // Large gap between the two cards

        // 1. Header Section (Name + Value)
        PdfPCell headerCell = new PdfPCell();
        headerCell.setBackgroundColor(COL_PRIMARY_BG);
        headerCell.setPadding(12);
        headerCell.setBorderColor(COL_PRIMARY_BG);

        // Header Inner Layout: Left (Name) - Right (Price)
        PdfPTable headerContent = new PdfPTable(2);
        headerContent.setWidthPercentage(100);
        headerContent.setWidths(new float[]{3f, 1f}); // Name gets more space

        // Name
        PdfPCell nameCell = new PdfPCell(new Phrase(value(asset.getName()).toUpperCase(), FONT_CARD_HEADER));
        nameCell.setBorder(Rectangle.NO_BORDER);
        nameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        headerContent.addCell(nameCell);

        // Price
        PdfPCell priceCell = new PdfPCell(new Phrase(formatAmount(asset.getCurrentValue()), FONT_CARD_AMOUNT));
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

        // Core Details (Category / Date)
        bodyCell.addElement(createCoreDetailsTable(asset));

        // Separator Line
        bodyCell.addElement(createSeparator());

        // Dynamic Attributes (Grid Layout)
        bodyCell.addElement(createAttributesGrid(asset.getAttributes()));

        card.addCell(bodyCell);

        document.add(card);
    }

    // --- Sub-components ---

    private PdfPTable createCoreDetailsTable(AssetsResponseDTO asset) {
        PdfPTable table = new PdfPTable(4); // 4 Columns: Label Val Label Val
        table.setWidthPercentage(100);
        table.setSpacingAfter(5);

        // Row 1: Main Category | Sub Category
        addKvCell(table, "Main Category", value(asset.getMainCategory()));
        addKvCell(table, "Asset Class", value(asset.getCategory()));

        // Row 2: Acquired Date | ROI (Example placeholder or calculated)
        addKvCell(table, "Date Acquired", formatDate(asset.getDateAcquired()));
        addKvCell(table, "Status", "Active"); // Example static field

        return table;
    }

    private PdfPTable createAttributesGrid(Map<String, Object> attributes) {
        PdfPTable table = new PdfPTable(4); // 2 sets of Key-Value pairs per row
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1.5f, 2f, 1.5f, 2f}); // Adjust column ratios
        table.setSpacingBefore(10);

        if (attributes == null || attributes.isEmpty()) {
            return table;
        }

        // Add "Technical Details" Header inside the grid area
        PdfPCell titleCell = new PdfPCell(new Phrase("Technical Specifications", FONT_SECTION_HEADER));
        titleCell.setColspan(4);
        titleCell.setBorder(Rectangle.NO_BORDER);
        titleCell.setPaddingBottom(8);
        table.addCell(titleCell);

        int count = 0;
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            addKvCell(table, beautify(entry.getKey()), formatAttributeValue(entry.getKey(), entry.getValue()));
            count++;
        }

        // Fill empty cells if odd number of attributes to keep borders clean
        if (count % 2 != 0) {
            addKvCell(table, "", "");
        }

        return table;
    }

    private void addKvCell(PdfPTable table, String key, String val) {
        // Label Cell
        PdfPCell label = new PdfPCell(new Phrase(key, FONT_LABEL));
        label.setBorder(Rectangle.NO_BORDER);
        label.setPaddingTop(5);
        label.setPaddingBottom(5);
        table.addCell(label);

        // Value Cell
        PdfPCell value = new PdfPCell(new Phrase(val, FONT_VALUE));
        value.setBorder(Rectangle.NO_BORDER);
        value.setPaddingTop(5);
        value.setPaddingBottom(5);
        table.addCell(value);
    }

    private Paragraph createSeparator() {
        Paragraph p = new Paragraph(" ");
        p.setLeading(5); // Small height
        // Drawing a line using a table or border is harder in pure Element,
        // so we use a thin empty table with a bottom border
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
        if (k.contains("rate") || k.contains("yield") || k.contains("tax")) return value + "%";
        if (k.contains("price") || k.contains("value") || k.contains("amount")) {
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