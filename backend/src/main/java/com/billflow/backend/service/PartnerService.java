package com.billflow.backend.service;

import com.billflow.backend.domain.Partner;
import com.billflow.backend.repository.BillRepository;
import com.billflow.backend.repository.PartnerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import com.billflow.backend.domain.User;
import org.springframework.data.domain.PageRequest;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PartnerService {

    private final PartnerRepository partnerRepository;
    private final BillRepository billRepository;

    public PartnerService(PartnerRepository partnerRepository, BillRepository billRepository) {
        this.partnerRepository = partnerRepository;
        this.billRepository = billRepository;
    }

    public List<Partner> getAllPartners(User user) {
        return partnerRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public Partner createPartner(String name, String location, String phoneNumber, User user) {
        Partner partner = new Partner();
        partner.setName(name);
        partner.setLocation(location);
        partner.setPhoneNumber(phoneNumber);
        partner.setUser(user);
        return partnerRepository.save(partner);
    }

    public void deletePartner(String id) {
        if (billRepository.existsByPartnerId(id)) {
            throw new RuntimeException("Cannot delete partner. Bills are associated with this partner.");
        }
        partnerRepository.deleteById(id);
    }

    public List<Map<String, Object>> getTopPartners(User user) {
        List<Object[]> results = billRepository.findTopPartnersByUser(user, PageRequest.of(0, 5));
        return results.stream().map(r -> Map.<String, Object>of(
            "name", (String) r[0],
            "totalRevenue", (BigDecimal) r[1]
        )).collect(Collectors.toList());
    }

    public List<Map<String, Object>> getRevenueAnalysis(User user, String period) {
        // period must be 'week', 'month', or 'year' for PostgreSQL DATE_TRUNC
        String dbPeriod = switch(period.toLowerCase()) {
            case "weekly" -> "week";
            case "yearly" -> "year";
            default -> "month";
        };
        
        List<Object[]> results = billRepository.getRevenueAnalysis(user.getId(), dbPeriod);
        return results.stream().map(r -> Map.<String, Object>of(
            "customer", (String) r[0],
            "timeframe", ((Timestamp) r[1]).toLocalDateTime().toString(),
            "amount", (BigDecimal) r[2]
        )).collect(Collectors.toList());
    }
}
