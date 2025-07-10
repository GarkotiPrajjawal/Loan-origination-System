package com.example.Loan.origination.System.Repository;

import com.example.Loan.origination.System.Entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AgentRepository  extends JpaRepository<Agent,Integer> {
    @Query(value = "SELECT * FROM Agent WHERE agent_Status = 'Active' ORDER BY pending_Applications_Count LIMIT 1", nativeQuery = true)
    Optional<Agent> findAvailableAgent();
}
