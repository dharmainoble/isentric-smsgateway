package com.isentric.smsserver.object;

import java.io.Serializable;

public class UserObject implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private String username;
    private String password;
    private String custid;
    private String status;
    private String email;
    private String phone;
    private String companyName;
    private String apiKey;
    private String createdDate;
    private String lastLoginDate;
    private int maxSmsPerDay;
    private int sentSmsToday;

    public UserObject() {}

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getCustid() { return custid; }
    public void setCustid(String custid) { this.custid = custid; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }

    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }

    public String getLastLoginDate() { return lastLoginDate; }
    public void setLastLoginDate(String lastLoginDate) { this.lastLoginDate = lastLoginDate; }

    public int getMaxSmsPerDay() { return maxSmsPerDay; }
    public void setMaxSmsPerDay(int maxSmsPerDay) { this.maxSmsPerDay = maxSmsPerDay; }

    public int getSentSmsToday() { return sentSmsToday; }
    public void setSentSmsToday(int sentSmsToday) { this.sentSmsToday = sentSmsToday; }
}
