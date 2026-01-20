package com.isentric.smsserver.object;

import java.io.Serializable;

public class HLRResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private String msisdn;
    private String imsi;
    private String msc;
    private String vlr;
    private String hlr;
    private String status;
    private String errorCode;
    private String errorMessage;
    private String networkCode;
    private String countryCode;
    private String originalNetwork;
    private String ported;
    private String roaming;
    private String roamingNetwork;
    private String roamingCountry;

    public HLRResponse() {}

    public String getMsisdn() { return msisdn; }
    public void setMsisdn(String msisdn) { this.msisdn = msisdn; }

    public String getImsi() { return imsi; }
    public void setImsi(String imsi) { this.imsi = imsi; }

    public String getMsc() { return msc; }
    public void setMsc(String msc) { this.msc = msc; }

    public String getVlr() { return vlr; }
    public void setVlr(String vlr) { this.vlr = vlr; }

    public String getHlr() { return hlr; }
    public void setHlr(String hlr) { this.hlr = hlr; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public String getNetworkCode() { return networkCode; }
    public void setNetworkCode(String networkCode) { this.networkCode = networkCode; }

    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }

    public String getOriginalNetwork() { return originalNetwork; }
    public void setOriginalNetwork(String originalNetwork) { this.originalNetwork = originalNetwork; }

    public String getPorted() { return ported; }
    public void setPorted(String ported) { this.ported = ported; }

    public String getRoaming() { return roaming; }
    public void setRoaming(String roaming) { this.roaming = roaming; }

    public String getRoamingNetwork() { return roamingNetwork; }
    public void setRoamingNetwork(String roamingNetwork) { this.roamingNetwork = roamingNetwork; }

    public String getRoamingCountry() { return roamingCountry; }
    public void setRoamingCountry(String roamingCountry) { this.roamingCountry = roamingCountry; }
}
