package com.billflow.backend.repository;

import com.billflow.backend.domain.BillItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillItemRepository extends JpaRepository<BillItem, String> {
}
