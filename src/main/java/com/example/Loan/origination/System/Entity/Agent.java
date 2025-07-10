package com.example.Loan.origination.System.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Agent {

    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Id
    private int agentId;
    private String agentName;
    private String agentPhoneNumber;
    private String agentAddress;
    private String agentStatus;
    private int agentManagerId;
    private int pendingApplicationsCount;

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentPhoneNumber() {
        return agentPhoneNumber;
    }

    public void setAgentPhoneNumber(String agentPhoneNumber) {
        this.agentPhoneNumber = agentPhoneNumber;
    }

    public String getAgentAddress() {
        return agentAddress;
    }

    public void setAgentAddress(String agentAddress) {
        this.agentAddress = agentAddress;
    }

    public String getAgentStatus() {
        return agentStatus;
    }

    public void setAgentStatus(String agentStatus) {
        this.agentStatus = agentStatus;
    }

    public int getAgentManagerId() {
        return agentManagerId;
    }

    public void setAgentManagerId(int agentManagerId) {
        this.agentManagerId = agentManagerId;
    }

    public int getPendingApplicationsCount() {
        return pendingApplicationsCount;
    }

    public void setPendingApplicationsCount(int pendingApplicationsCount) {
        this.pendingApplicationsCount = pendingApplicationsCount;
    }
}
