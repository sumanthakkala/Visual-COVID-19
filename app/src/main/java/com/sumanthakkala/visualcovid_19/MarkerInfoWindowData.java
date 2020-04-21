package com.sumanthakkala.visualcovid_19;

public class MarkerInfoWindowData {
    private String totalCases;
    private String activeCases;
    private String recoveredCases;
    private String fatalCases;
    private String countryName;

    public String getTotalCases() {
        return totalCases;
    }

    public void setTotalCases(String total) {
        this.totalCases = total;
    }

    public String getActiveCases() {
        return activeCases;
    }

    public void setActiveCases(String active) {
        this.activeCases = active;
    }

    public String getRecoveredCases() {
        return recoveredCases;
    }

    public void setRecoveredCases(String recovered) {
        this.recoveredCases = recovered;
    }

    public String getFatalCases() {
        return fatalCases;
    }

    public void setFatalCases(String fatal) {
        this.fatalCases = fatal;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String name) {
        this.countryName = name;
    }
}
