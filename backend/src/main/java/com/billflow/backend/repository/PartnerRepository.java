package com.billflow.backend.repository;

import com.billflow.backend.domain.Partner;
import com.billflow.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, String> {
    List<Partner> findByUserOrderByCreatedAtDesc(User user);
}
