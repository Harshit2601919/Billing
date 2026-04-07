package com.billflow.backend.repository;

import com.billflow.backend.domain.Bill;
import com.billflow.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface BillRepository extends JpaRepository<Bill, String> {
    List<Bill> findByUserOrderByBillDateDesc(User user);
    
    long countByUser(User user);
    
    boolean existsByPartnerId(String partnerId);

    @Query("SELECT b.partner.name as name, SUM(b.totalAmount) as totalRevenue FROM Bill b WHERE b.user = :user GROUP BY b.partner.name ORDER BY totalRevenue DESC")
    List<Object[]> findTopPartnersByUser(@Param("user") User user, Pageable pageable);

    @Query(value = "SELECT p.name as name, DATE_TRUNC(:period, b.bill_date) as timeframe, SUM(b.total_amount) as amount " +
                   "FROM bills b JOIN partners p ON b.partner_id = p.id " +
                   "WHERE b.user_id = :userId " +
                   "GROUP BY p.name, timeframe ORDER BY timeframe ASC", nativeQuery = true)
    List<Object[]> getRevenueAnalysis(@Param("userId") String userId, @Param("period") String period);
    @Query(value = "SELECT CAST(COALESCE(MAX(CAST(bill_number AS INTEGER)), 0) AS BIGINT) FROM bills WHERE user_id = :userId", nativeQuery = true)
    Long findMaxBillNumberByUserId(@Param("userId") String userId);
}
