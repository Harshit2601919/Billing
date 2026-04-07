package com.billflow.backend.service;

import com.billflow.backend.domain.Bill;
import com.billflow.backend.domain.BillItem;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    public byte[] generateInvoicePdf(Bill bill) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Styling elements
        DeviceRgb primaryColor = new DeviceRgb(108, 92, 231); 
        
        // Header using User's Company Name
        String companyHeader = (bill.getUser() != null && bill.getUser().getCompanyName() != null) 
                ? bill.getUser().getCompanyName().toUpperCase() 
                : "BILLFLOW INVOICE";

        document.add(new Paragraph(companyHeader)
                .setFontSize(24)
                .setBold()
                .setFontColor(primaryColor)
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("\n"));

        // Date and Time formatting
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm");
        String formattedDate = bill.getBillDate().format(formatter);

        // Bill Meta
        Table metaTable = new Table(UnitValue.createPercentArray(new float[]{1, 1})).useAllAvailableWidth();
        metaTable.addCell(new Cell().add(new Paragraph("Bill To:\n" + bill.getPartner().getName() + "\n" + bill.getPartner().getLocation())).setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));
        metaTable.addCell(new Cell().add(new Paragraph("Bill Number: " + bill.getBillNumber() + "\nDate: " + formattedDate)).setTextAlignment(TextAlignment.RIGHT).setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));
        document.add(metaTable);
        document.add(new Paragraph("\n"));

        // Items Table
        Table table = new Table(UnitValue.createPercentArray(new float[]{4, 1, 2, 2})).useAllAvailableWidth();
        
        // Headers
        table.addHeaderCell(new Cell().add(new Paragraph("Description").setBold().setFontColor(primaryColor)));
        table.addHeaderCell(new Cell().add(new Paragraph("Qty").setBold().setFontColor(primaryColor)));
        table.addHeaderCell(new Cell().add(new Paragraph("Unit Price").setBold().setFontColor(primaryColor)));
        table.addHeaderCell(new Cell().add(new Paragraph("Total").setBold().setFontColor(primaryColor)));

        // Items
        for (BillItem item : bill.getItems()) {
            table.addCell(new Cell().add(new Paragraph(item.getDescription())));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(item.getQuantity()))));
            table.addCell(new Cell().add(new Paragraph("₹" + item.getUnitPrice())));
            table.addCell(new Cell().add(new Paragraph("₹" + item.getItemTotal())));
        }

        document.add(table);
        
        // Total
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Total Amount: ₹" + bill.getTotalAmount())
                .setFontSize(16)
                .setBold()
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontColor(primaryColor));

        document.close();
        
        return baos.toByteArray();
    }
}
