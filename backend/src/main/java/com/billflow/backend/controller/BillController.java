package com.billflow.backend.controller;

import com.billflow.backend.domain.Bill;
import com.billflow.backend.domain.BillItem;
import com.billflow.backend.service.BillService;
import com.billflow.backend.service.PdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.billflow.backend.domain.User;
import com.billflow.backend.service.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    private final BillService billService;
    private final PdfService pdfService;
    private final AuthService authService;

    public BillController(BillService billService, PdfService pdfService, AuthService authService) {
        this.billService = billService;
        this.pdfService = pdfService;
        this.authService = authService;
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return authService.getUserByUsername(auth.getName());
    }

    @GetMapping("/")
    public ResponseEntity<List<Bill>> getAllBills() {
        return ResponseEntity.ok(billService.getAllBills(getCurrentUser()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bill> getBillById(@PathVariable String id) {
        return ResponseEntity.ok(billService.getBillById(id));
    }

    @PostMapping("/")
    public ResponseEntity<?> createBill(@RequestBody BillRequest request) {
        try {
            List<BillItem> items = request.items().stream().map(dto -> {
                BillItem item = new BillItem();
                item.setDescription(dto.description());
                item.setQuantity(dto.quantity());
                item.setUnitPrice(dto.unitPrice());
                return item;
            }).collect(Collectors.toList());

            Bill bill = billService.createBill(request.partnerId(), request.billDate(), items, getCurrentUser());
            return ResponseEntity.ok(bill);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/{id}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generatePdf(@PathVariable String id) {
        Bill bill = billService.getBillById(id);
        byte[] pdfBytes = pdfService.generateInvoicePdf(bill);

        String partnerName = bill.getPartner().getName().replaceAll("\\s+", "_");
        String dateStr = bill.getBillDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String filename = partnerName + "_" + dateStr + ".pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", filename);
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBill(@PathVariable String id) {
        try {
            billService.deleteBill(id, getCurrentUser());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DTOs
    public record BillRequest(String partnerId, java.time.LocalDateTime billDate, List<BillItemRequest> items) {}
    public record BillItemRequest(String description, Integer quantity, java.math.BigDecimal unitPrice) {}
}
