package com.isentric.smsserver.object;

import java.io.Serializable;

public class CreditObject implements Serializable {
    private static final long serialVersionUID = 1L;

    private String custid;
    private String userId;
    private double credit;
    private double usedCredit;
    private double remainingCredit;
    private int creditType;
    private String status;
    private String lastUpdated;
    private String createdDate;

    public CreditObject() {}

    public String getCustid() { return custid; }
    public void setCustid(String custid) { this.custid = custid; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public double getCredit() { return credit; }
    public void setCredit(double credit) { this.credit = credit; }

    public double getUsedCredit() { return usedCredit; }
    public void setUsedCredit(double usedCredit) { this.usedCredit = usedCredit; }

    public double getRemainingCredit() { return remainingCredit; }
    public void setRemainingCredit(double remainingCredit) { this.remainingCredit = remainingCredit; }

    public int getCreditType() { return creditType; }
    public void setCreditType(int creditType) { this.creditType = creditType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; }

    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }
}
