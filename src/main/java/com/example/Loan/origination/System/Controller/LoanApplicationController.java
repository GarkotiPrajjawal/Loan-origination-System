package com.example.Loan.origination.System.Controller;


import com.example.Loan.origination.System.Entity.LoanApplication;
import com.example.Loan.origination.System.Service.LoanApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class LoanApplicationController {
    @Autowired
    private LoanApplicationService loanApplicationService;

    @PostMapping("/loans")
    public String createLoanApplication(@RequestBody LoanApplication loanApplication) throws InterruptedException {
        return loanApplicationService.saveLoanApplication(loanApplication);
    }
    @PutMapping("/agents/{agent_id}/loans/{loan_id}/{decision}")
    public String updateLoanApplication(@PathVariable int agent_id, @PathVariable int loan_id,@PathVariable String decision) {
        return loanApplicationService.updateLoanApplication(loanApplicationService.findLoanApplicationById(loan_id),agent_id,decision);
    }

    @GetMapping("/loans/status-count")
    public List<String> getLoanStatusCount() {
        return loanApplicationService.getLoanStatusCount();
    }

    @GetMapping("/customers/top")
    public List<String> getTopCustomers() {
        return loanApplicationService.getTopCustomers();
    }

    @GetMapping("/loans")
    public Page<LoanApplication> getLoansByStatus(@RequestParam String status, Pageable pageable) {
        return loanApplicationService.findByStatus( status, pageable);
    }

}
