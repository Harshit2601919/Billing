package com.billflow.backend.service;

import com.billflow.backend.domain.Bill;
import com.billflow.backend.domain.BillItem;
import com.billflow.backend.domain.Partner;
import com.billflow.backend.repository.BillRepository;
import com.billflow.backend.repository.PartnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.billflow.backend.domain.User;

@Service
public class BillService {

    private final BillRepository billRepository;
    private final PartnerRepository partnerRepository;

    public BillService(BillRepository billRepository, PartnerRepository partnerRepository) {
        this.billRepository = billRepository;
        this.partnerRepository = partnerRepository;
    }

    public List<Bill> getAllBills(User user) {
        return billRepository.findByUserOrderByBillDateDesc(user);
    }

    public Bill getBillById(String id) {
        return billRepository.findById(id).orElseThrow(() -> new RuntimeException("Bill not found"));
    }

    @Transactional
    public Bill createBill(String partnerId, LocalDateTime billDate, List<BillItem> items, User user) {
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Partner not found"));

        Bill bill = new Bill();
        bill.setPartner(partner);
        bill.setUser(user);
        bill.setBillDate(billDate); // Use provided date and time

        String generatedNumber = generateSequentialNumber(user);
        bill.setBillNumber(generatedNumber);

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (BillItem item : items) {
            bill.addItem(item);
            totalAmount = totalAmount.add(item.getItemTotal());
        }
        bill.setTotalAmount(totalAmount);

        return billRepository.save(bill);
    }

    @Transactional
    public void deleteBill(String id, User user) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
        
        // Verify ownership
        if (!bill.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to delete this bill");
        }
        
        billRepository.delete(bill);
    }

    private synchronized String generateSequentialNumber(User user) {
        Long maxNumber = billRepository.findMaxBillNumberByUserId(user.getId());
        return String.valueOf(maxNumber + 1); // Sequential max + 1
    }
}
