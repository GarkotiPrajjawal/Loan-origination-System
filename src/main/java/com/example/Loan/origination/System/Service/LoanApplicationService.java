package com.example.Loan.origination.System.Service;


import com.example.Loan.origination.System.Entity.Agent;
import com.example.Loan.origination.System.Entity.LoanApplication;
import com.example.Loan.origination.System.Entity.LoanType;
import com.example.Loan.origination.System.Repository.AgentRepository;
import com.example.Loan.origination.System.Repository.LoanApplicationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class LoanApplicationService {
    @Autowired
    private LoanApplicationRepository loanApplicationRepository;
    @Autowired
    private AgentRepository agentRepository;

    Logger log = LoggerFactory.getLogger(LoanApplicationService.class);

    // Method to save a loan application
    public String saveLoanApplication(LoanApplication loanApplication) throws InterruptedException {
        Thread.sleep(2500); // Simulating a delay of 2.5 seconds
        if(loanApplication.getLoan_type()== LoanType.PERSONAL){
            log.debug("{} APPROVED", loanApplication.getCustomer_name());
            loanApplication.setLoan_status("APPROVED_BY_SYSTEM");
        }
        else if(loanApplication.getLoan_type()== LoanType.AUTO){
            loanApplication.setLoan_status("REJECTED_BY_SYSTEM");
            log.debug("{} REJECTED", loanApplication.getCustomer_name());
        } else if (loanApplication.getLoan_type()==LoanType.HOME) {
            log.debug("{} APPROVED", loanApplication.getCustomer_name());
            loanApplication.setLoan_status("APPROVED_BY_AGENT");
        }else{
            loanApplication.setLoan_status("UNDER_REVIEW");
        }
        Optional<Agent> agent= agentRepository.findAvailableAgent();
        if(agent.isEmpty()) {
            return "No available agents to handle the loan application";
        }
        loanApplication.setAgent_id(agent.get().getAgentId());
        agent.get().setPendingApplicationsCount(agent.get().getPendingApplicationsCount()+1);
        agentRepository.save(agent.get());
        loanApplicationRepository.save(loanApplication);
        log.debug("loan application to be processed by {}", agent.get().getAgentId());
        return "Loan application created successfully";
    }

    public Void createMultipleLoanApplication(List<LoanApplication> loanApplications) throws ExecutionException, InterruptedException {
        Executor executor = Executors.newFixedThreadPool(5);
        CompletableFuture<Void> runAsyncFuture = CompletableFuture.runAsync(
                () -> {
                    try {
                        loanApplications.parallelStream().forEach(loanApplication-> {
                            try {
                                saveAllLoanApplication(loanApplication);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },executor);

        return runAsyncFuture.get();
    }

    public void saveAllLoanApplication(LoanApplication loanApplication) throws InterruptedException {
        ReentrantLock lock=new ReentrantLock();
        try {
            lock.lock();
            Thread.sleep(2500); // Simulating a delay of 2.5 seconds
        } finally {
            lock.unlock();
        }
        if(loanApplication.getLoan_type()== LoanType.PERSONAL){
            log.debug("{} APPROVED", loanApplication.getCustomer_name());
            loanApplication.setLoan_status("APPROVED_BY_SYSTEM");
        }
        else if(loanApplication.getLoan_type()== LoanType.AUTO){
            loanApplication.setLoan_status("REJECTED_BY_SYSTEM");
            log.debug("{} REJECTED", loanApplication.getCustomer_name());
        } else if (loanApplication.getLoan_type()==LoanType.HOME) {
            log.debug("{} APPROVED", loanApplication.getCustomer_name());
            loanApplication.setLoan_status("APPROVED_BY_AGENT");
        }else{
            loanApplication.setLoan_status("UNDER_REVIEW");
        }
        Optional<Agent> agent= agentRepository.findAvailableAgent();
        if(agent.isEmpty()) {
            log.debug("No available agents to handle the loan application");
            return;
        }
        loanApplication.setAgent_id(agent.get().getAgentId());
        agent.get().setPendingApplicationsCount(agent.get().getPendingApplicationsCount()+1);
        agentRepository.save(agent.get());
        loanApplicationRepository.save(loanApplication);
        log.debug("loan application to be processed by {}", agent.get().getAgentId());
    }

    // Method to find a loan application by ID
    public LoanApplication findLoanApplicationById(int id) {
        return loanApplicationRepository.findById(id).orElse(null);
    }
    public String updateLoanApplication(LoanApplication loanApplication,int agent_id,String decision) {
        Optional<Agent> agent=agentRepository.findById(agent_id);
        if(agent.isEmpty()) {
            return "Agent not found";
        }
        if(loanApplication == null) {
            return "Loan application not found";
        }
        if(loanApplication.getAgent_id()!= agent_id) {
            return "Agent ID does not match the loan application";
        }
        if(Objects.equals(decision, "APPROVE")){loanApplication.setLoan_status("APPROVED_BY_AGENT");
            log.debug("{} APPROVED", loanApplication.getCustomer_name());}
        else{loanApplication.setLoan_status("REJECTED_BY_AGENT");}
        agent.get().setPendingApplicationsCount(agent.get().getPendingApplicationsCount()-1);
        agentRepository.save(agent.get());
        loanApplicationRepository.save(loanApplication);
        return "Loan application updated successfully";
    }

    public List<String> getLoanStatusCountstreams() {
          Map<String, Long> statusAndCount=loanApplicationRepository.findAll()
                .parallelStream()
                .collect(Collectors.groupingBy(LoanApplication::getLoan_status,
                        Collectors.counting()));
          List<String> statusList = new ArrayList<>();
          for(Map.Entry<String, Long> entry : statusAndCount.entrySet()) {
              String status = entry.getKey();
              Long count = entry.getValue();
              statusList.add(status + ": " + count);
          }
          return statusList;
    }

    public List<Object[]> getLoanStatusCount() {
        return loanApplicationRepository.findLoanStatusCount();
    }

    public List<String> getTopCustomers() {
        return loanApplicationRepository.findCustomersWithMostLoanApproved();
    }

    public Page<LoanApplication> findByStatus(String status,Pageable pageable) {
        return loanApplicationRepository.findByStatus(status, pageable);
    }
}
