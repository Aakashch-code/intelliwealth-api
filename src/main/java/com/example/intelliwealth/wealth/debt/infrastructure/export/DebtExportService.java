package com.example.intelliwealth.wealth.debt.infrastructure.export;

import com.example.intelliwealth.wealth.debt.application.dto.DebtResponseDTO;
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
public class DebtExportService {

    // ─── Locale / Formats ──────────────────────────────────────────────────────
    private static final Locale INDIA      = new Locale("en", "IN");
    private static final NumberFormat INR  = NumberFormat.getCurrencyInstance(INDIA);
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    // ─── Palette (Clean White Executive - Intelli Wealth Brand) ───────────────
    // Charcoal — used only for text, never as bg
    private static final Color C_NAVY        = new Color(45,  45,  55);
    // Accent gold (kept as brand accent)
    private static final Color C_GOLD        = new Color(185, 148, 73);
    // Very light warm tint for alternating rows
    private static final Color C_GOLD_TINT   = new Color(253, 250, 244);
    // Soft off-white page background
    private static final Color C_PAGE_BG     = new Color(250, 250, 252);
    // Pure white
    private static final Color C_WHITE       = Color.WHITE;
    // Mid-gray text
    private static final Color C_GRAY        = new Color(120, 120, 130);
    // Near-black body text
    private static final Color C_DARK        = new Color(25,  25,  35);
    // Soft border
    private static final Color C_BORDER      = new Color(225, 225, 232);
    // Debt specific accent (Deep Crimson for liabilities)
    private static final Color C_CRIMSON     = new Color(178, 58, 58);

    // ─── Fonts ─────────────────────────────────────────────────────────────────
    // Cover / page header
    private static final Font F_COVER_SUB     = FontFactory.getFont(FontFactory.HELVETICA,      11, C_GRAY);
    private static final Font F_COVER_DATE    = FontFactory.getFont(FontFactory.HELVETICA,       9, C_GRAY);

    // Card header band
    private static final Font F_DEBT_NAME     = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, C_DARK);
    private static final Font F_DEBT_VALUE    = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, C_CRIMSON);
    private static final Font F_DEBT_LABEL    = FontFactory.getFont(FontFactory.HELVETICA,       9, C_GRAY);

    // Section / body
    private static final Font F_SECTION_HDR   = FontFactory.getFont(FontFactory.HELVETICA_BOLD,  8, C_GOLD);
    private static final Font F_KV_LABEL      = FontFactory.getFont(FontFactory.HELVETICA,       8, C_GRAY);
    private static final Font F_KV_VALUE      = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, C_DARK);

    // ──────────────────────────────────────────────────────────────────────────

    public void generate(HttpServletResponse response, List<DebtResponseDTO> debts) throws IOException {
        // Full A4, narrow margins so cards have maximum real-estate
        Document doc = new Document(PageSize.A4, 40, 40, 40, 50);
        PdfWriter writer = PdfWriter.getInstance(doc, response.getOutputStream());

        // Attach footer event
        writer.setPageEvent(new FooterPageEvent());

        doc.open();

        // Cover page
        addCoverPage(doc, debts.size());

        // One debt record per page
        for (DebtResponseDTO debt : debts) {
            doc.newPage();
            addDebtPage(doc, writer, debt);
        }

        doc.close();
    }

    // ─── Cover Page ────────────────────────────────────────────────────────────

    private void addCoverPage(Document doc, long total) throws DocumentException {
        // Top accent bar (simulated with a 1-row table)
        PdfPTable accentBar = new PdfPTable(1);
        accentBar.setWidthPercentage(100);
        PdfPCell bar = new PdfPCell(new Phrase(" "));
        bar.setBackgroundColor(C_GOLD);
        bar.setBorder(Rectangle.NO_BORDER);
        bar.setFixedHeight(6f);
        accentBar.addCell(bar);
        doc.add(accentBar);

        doc.add(spacer(60));

        // Firm / product name
        Paragraph firm = new Paragraph("INTELLI WEALTH", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, C_GOLD));
        firm.setAlignment(Element.ALIGN_CENTER);
        doc.add(firm);

        doc.add(spacer(12));

        Paragraph title = new Paragraph("Liabilities & Debt Report",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 28, C_DARK));
        title.setAlignment(Element.ALIGN_CENTER);
        doc.add(title);

        doc.add(spacer(14));

        Paragraph sub = new Paragraph(
                "Comprehensive Debt Statement  ·  " + total + " Record" + (total == 1 ? "" : "s") + " on File",
                F_COVER_SUB
        );
        sub.setAlignment(Element.ALIGN_CENTER);
        doc.add(sub);

        doc.add(spacer(8));

        // Gold divider
        doc.add(goldDivider());

        doc.add(spacer(8));

        Paragraph date = new Paragraph(
                "Generated on " + LocalDate.now().format(DATE_FMT) + "  ·  CONFIDENTIAL",
                F_COVER_DATE
        );
        date.setAlignment(Element.ALIGN_CENTER);
        doc.add(date);

        doc.add(spacer(60));

        // Large navy rectangle as decorative block (table trick)
        PdfPTable deco = new PdfPTable(1);
        deco.setWidthPercentage(60);
        deco.setHorizontalAlignment(Element.ALIGN_CENTER);
        PdfPCell decoCell = new PdfPCell();
        decoCell.setBackgroundColor(C_NAVY);
        decoCell.setBorder(Rectangle.NO_BORDER);
        decoCell.setFixedHeight(4f);
        deco.addCell(decoCell);
        doc.add(deco);

        doc.add(spacer(20));

        Paragraph disclaimer = new Paragraph(
                "This report is prepared exclusively for the named client and contains proprietary financial information.\n"
                        + "Unauthorized disclosure, reproduction or distribution is strictly prohibited.",
                FontFactory.getFont(FontFactory.HELVETICA, 8, C_GRAY)
        );
        disclaimer.setAlignment(Element.ALIGN_CENTER);
        doc.add(disclaimer);
    }

    // ─── Per-Debt Full Page ────────────────────────────────────────────────────

    private void addDebtPage(Document doc, PdfWriter writer, DebtResponseDTO debt) throws DocumentException {
        // ── 1. Header Band ───────────────────────────────────────────────────
        PdfPTable headerBand = new PdfPTable(2);
        headerBand.setWidthPercentage(100);
        headerBand.setWidths(new float[]{3f, 2f});

        // Left: Debt name + category pill
        PdfPCell leftHdr = new PdfPCell();
        leftHdr.setBackgroundColor(C_WHITE);
        leftHdr.setBorder(Rectangle.BOTTOM);
        leftHdr.setBorderColor(C_BORDER);
        leftHdr.setBorderWidth(1f);
        leftHdr.setPadding(20);
        leftHdr.setPaddingLeft(24);

        Paragraph catLabel = new Paragraph(
                value(debt.getMainCategory()) + "  ›  " + value(debt.getCategory()),
                F_DEBT_LABEL
        );
        leftHdr.addElement(catLabel);

        Paragraph nameP = new Paragraph(value(debt.getName()).toUpperCase(), F_DEBT_NAME);
        nameP.setSpacingBefore(4);
        leftHdr.addElement(nameP);

        Paragraph creditorLabel = new Paragraph(
                "Creditor / Lender:  " + value(debt.getCreditor()),
                F_DEBT_LABEL
        );
        creditorLabel.setSpacingBefore(6);
        leftHdr.addElement(creditorLabel);

        headerBand.addCell(leftHdr);

        // Right: Outstanding value
        PdfPCell rightHdr = new PdfPCell();
        rightHdr.setBackgroundColor(C_WHITE);
        rightHdr.setBorder(Rectangle.BOTTOM);
        rightHdr.setBorderColor(C_BORDER);
        rightHdr.setBorderWidth(1f);
        rightHdr.setPadding(20);
        rightHdr.setPaddingRight(24);
        rightHdr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightHdr.setVerticalAlignment(Element.ALIGN_MIDDLE);

        Paragraph valLabel = new Paragraph("OUTSTANDING BALANCE", F_DEBT_LABEL);
        valLabel.setAlignment(Element.ALIGN_RIGHT);
        rightHdr.addElement(valLabel);

        Paragraph valP = new Paragraph(formatAmount(debt.getOutstandingAmount()), F_DEBT_VALUE);
        valP.setAlignment(Element.ALIGN_RIGHT);
        valP.setSpacingBefore(6);
        rightHdr.addElement(valP);

        // Status text below value
        Paragraph statusP = new Paragraph("● ACTIVE LIABILITY", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, C_CRIMSON));
        statusP.setAlignment(Element.ALIGN_RIGHT);
        statusP.setSpacingBefore(8);
        rightHdr.addElement(statusP);

        headerBand.addCell(rightHdr);
        doc.add(headerBand);

        // Gold accent stripe below header
        doc.add(goldStripe());

        doc.add(spacer(18));

        // ── 2. Core Info Section ─────────────────────────────────────────────
        addSectionTitle(doc, "LIABILITY OVERVIEW");
        doc.add(spacer(6));
        doc.add(buildOverviewTable(debt));

        doc.add(spacer(20));

        // ── 3. Loan Terms & Specifications ───────────────────────────────────
        if (debt.getAttributes() != null && !debt.getAttributes().isEmpty()) {
            addSectionTitle(doc, "LOAN DETAILS & TERMS");
            doc.add(spacer(6));
            doc.add(buildAttributesTable(debt.getAttributes()));
        }

        doc.add(spacer(20));

        // ── 4. Key Metrics Strip ─────────────────────────────────────────────
        doc.add(buildMetricsStrip(debt));
    }

    // ─── Section Title Row ─────────────────────────────────────────────────────

    private void addSectionTitle(Document doc, String title) throws DocumentException {
        PdfPTable t = new PdfPTable(1);
        t.setWidthPercentage(100);

        PdfPCell c = new PdfPCell();
        c.setBorder(Rectangle.BOTTOM);
        c.setBorderColor(C_GOLD);
        c.setBorderWidth(1.5f);
        c.setPaddingBottom(4);
        c.setPaddingLeft(0);
        c.setBackgroundColor(C_PAGE_BG);

        Phrase phrase = new Phrase(title, F_SECTION_HDR);
        c.setPhrase(phrase);
        t.addCell(c);
        doc.add(t);
    }

    // ─── Debt Overview Table ───────────────────────────────────────────────────

    private PdfPTable buildOverviewTable(DebtResponseDTO debt) {
        // 4 columns: label | value | label | value
        PdfPTable t = new PdfPTable(4);
        t.setWidthPercentage(100);
        t.setWidths(new float[]{1.4f, 2f, 1.4f, 2f});

        String[][] rows = {
                { "Category",        value(debt.getMainCategory()) },
                { "Liability Type",  value(debt.getCategory())     },
                { "Creditor",        value(debt.getCreditor())     },
                { "Record Status",   "Active"                      }
        };

        for (int i = 0; i < rows.length; i += 2) {
            boolean shade = (i / 2) % 2 == 0;
            addKvRow(t, rows[i][0], rows[i][1], rows[i+1][0], rows[i+1][1], shade);
        }
        return t;
    }

    // ─── Dynamic Attributes Table ──────────────────────────────────────────────

    private PdfPTable buildAttributesTable(Map<String, Object> attrs) {
        PdfPTable t = new PdfPTable(4);
        t.setWidthPercentage(100);
        t.setWidths(new float[]{1.4f, 2f, 1.4f, 2f});

        List<Map.Entry<String, Object>> entries = new java.util.ArrayList<>(attrs.entrySet());
        // Pad to even
        if (entries.size() % 2 != 0) {
            entries.add(new java.util.AbstractMap.SimpleEntry<>("", ""));
        }

        for (int i = 0; i < entries.size(); i += 2) {
            boolean shade = (i / 2) % 2 == 0;
            Map.Entry<String, Object> a = entries.get(i);
            Map.Entry<String, Object> b = entries.get(i + 1);
            addKvRow(t,
                    beautify(a.getKey()), formatAttributeValue(a.getKey(), a.getValue()),
                    beautify(b.getKey()), formatAttributeValue(b.getKey(), b.getValue()),
                    shade
            );
        }
        return t;
    }

    // ─── Key Metrics Bottom Strip ──────────────────────────────────────────────

    private PdfPTable buildMetricsStrip(DebtResponseDTO debt) {
        PdfPTable t = new PdfPTable(3);
        t.setWidthPercentage(100);
        t.setWidths(new float[]{1f, 1f, 1f});

        // Metric 1: Outstanding Amount
        addMetricCell(t, "Outstanding Balance", formatAmount(debt.getOutstandingAmount()), C_CRIMSON);

        // Metric 2: Original Principal
        addMetricCell(t, "Original Principal", formatAmount(debt.getTotalAmount()), C_GOLD);

        // Metric 3: Due Date
        addMetricCell(t, "Next Due Date", formatDate(debt.getDueDate()), C_NAVY);

        return t;
    }

    private void addMetricCell(PdfPTable t, String label, String value, Color accentColor) {
        PdfPCell c = new PdfPCell();
        c.setBackgroundColor(C_WHITE);
        c.setBorder(Rectangle.BOX);
        c.setBorderColor(C_BORDER);
        c.setBorderWidth(0.75f);
        c.setPadding(14);

        // Top accent line
        PdfPTable topLine = new PdfPTable(1);
        topLine.setWidthPercentage(100);
        PdfPCell line = new PdfPCell(new Phrase(" "));
        line.setFixedHeight(3f);
        line.setBackgroundColor(accentColor);
        line.setBorder(Rectangle.NO_BORDER);
        topLine.addCell(line);
        c.addElement(topLine);

        Paragraph lbl = new Paragraph(label.toUpperCase(),
                FontFactory.getFont(FontFactory.HELVETICA, 7, C_GRAY));
        lbl.setSpacingBefore(6);
        c.addElement(lbl);

        Paragraph val = new Paragraph(value,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, C_DARK));
        val.setSpacingBefore(3);
        c.addElement(val);

        t.addCell(c);
    }

    // ─── KV Row Helpers ────────────────────────────────────────────────────────

    private void addKvRow(PdfPTable t,
                          String k1, String v1,
                          String k2, String v2,
                          boolean shaded) {
        Color bg = shaded ? C_GOLD_TINT : C_WHITE;
        t.addCell(labelCell(k1, bg));
        t.addCell(valueCell(v1, bg));
        t.addCell(labelCell(k2, bg));
        t.addCell(valueCell(v2, bg));
    }

    private PdfPCell labelCell(String text, Color bg) {
        PdfPCell c = new PdfPCell(new Phrase(text, F_KV_LABEL));
        c.setBackgroundColor(bg);
        c.setBorderColor(C_BORDER);
        c.setBorder(Rectangle.BOTTOM);
        c.setPadding(9);
        c.setPaddingLeft(12);
        return c;
    }

    private PdfPCell valueCell(String text, Color bg) {
        PdfPCell c = new PdfPCell(new Phrase(text, F_KV_VALUE));
        c.setBackgroundColor(bg);
        c.setBorderColor(C_BORDER);
        c.setBorder(Rectangle.BOTTOM);
        c.setPadding(9);
        return c;
    }

    // ─── Decorative Helpers ────────────────────────────────────────────────────

    private PdfPTable goldStripe() {
        PdfPTable t = new PdfPTable(1);
        t.setWidthPercentage(100);
        PdfPCell c = new PdfPCell(new Phrase(" "));
        c.setBackgroundColor(C_GOLD);
        c.setBorder(Rectangle.NO_BORDER);
        c.setFixedHeight(3f);
        t.addCell(c);
        return t;
    }

    private PdfPTable goldDivider() {
        PdfPTable t = new PdfPTable(1);
        t.setWidthPercentage(40);
        t.setHorizontalAlignment(Element.ALIGN_CENTER);
        PdfPCell c = new PdfPCell(new Phrase(" "));
        c.setBackgroundColor(C_GOLD);
        c.setBorder(Rectangle.NO_BORDER);
        c.setFixedHeight(1.5f);
        t.addCell(c);
        return t;
    }

    private Paragraph spacer(float pts) {
        Paragraph p = new Paragraph(" ");
        p.setLeading(pts);
        return p;
    }

    // ─── Footer Event ──────────────────────────────────────────────────────────

    static class FooterPageEvent extends PdfPageEventHelper {
        private static final Font F = FontFactory.getFont(FontFactory.HELVETICA, 7, new Color(150, 150, 160));

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            ColumnText.showTextAligned(
                    cb,
                    Element.ALIGN_LEFT,
                    new Phrase("INTELLI WEALTH  ·  Liability & Debt Report  ·  CONFIDENTIAL", F),
                    document.left(), document.bottom() - 18, 0
            );
            ColumnText.showTextAligned(
                    cb,
                    Element.ALIGN_RIGHT,
                    new Phrase("Page " + writer.getPageNumber(), F),
                    document.right(), document.bottom() - 18, 0
            );
            // Footer line
            cb.setLineWidth(0.5f);
            cb.setColorStroke(new Color(185, 148, 73));
            cb.moveTo(document.left(), document.bottom() - 10);
            cb.lineTo(document.right(), document.bottom() - 10);
            cb.stroke();
        }
    }

    // ─── Formatting Utilities ──────────────────────────────────────────────────

    private String formatAttributeValue(String key, Object val) {
        if (val == null || val.toString().isBlank()) return "-";
        String k = key.toLowerCase();

        if (k.contains("rate") || k.contains("interest")) return val + "%";
        if (k.contains("tenure") || k.contains("term")) return val + " Months";
        if (k.contains("emi") || k.contains("amount") || k.contains("principal")) {
            try { return INR.format(new BigDecimal(val.toString())); }
            catch (Exception ignored) {}
        }
        return val.toString();
    }

    private String formatAmount(BigDecimal amount) {
        return amount == null ? "-" : INR.format(amount);
    }

    private String formatDate(LocalDate d) {
        return d == null ? "-" : d.format(DATE_FMT);
    }

    private String value(Object o) {
        return o == null ? "-" : o.toString();
    }

    private String beautify(String key) {
        if (key == null || key.isBlank()) return "";
        return key.substring(0, 1).toUpperCase()
                + key.substring(1).replace("_", " ").toLowerCase();
    }
}