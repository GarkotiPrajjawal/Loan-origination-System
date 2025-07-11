package com.example.Loan.origination.System.Controller;

import com.example.Loan.origination.System.Entity.Agent;
import com.example.Loan.origination.System.Repository.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agent")
public class AgentController {
    @Autowired
    private AgentRepository agentRepository;

    @GetMapping()
    public List<Agent> getAllAgents(){
        return agentRepository.findAll();
    }
    @PostMapping()
    public String addAgent(@RequestBody Agent agent) {
        agentRepository.save(agent);
        return "Agent added successfully";
    }
}
