package com.example.Loan.origination.System.Repository;

import com.example.Loan.origination.System.Entity.LoanApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication,Integer> {

    @Query(value = "SELECT customer_name FROM loan_application WHERE loan_status IN ('APPROVED_BY_AGENT','APPROVED_BY_SYSTEM') GROUP BY customer_name ORDER BY COUNT(customer_name) DESC LIMIT 3", nativeQuery = true)
    List<String> findCustomersWithMostLoanApproved();

    @Query(value = "SELECT * FROM loan_application WHERE application_status = :status",
            countQuery = "SELECT count(*) FROM loan_application WHERE application_status = :status", // Good practice for performance
            nativeQuery = true)
    Page<LoanApplication> findByStatus(@Param("status") String status, Pageable pageable);
}
