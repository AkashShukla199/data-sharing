package com.example.datasharing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.sap")
public class SapConfig {
    
    private String destination;
    private String ashost;
    private String sysnr;
    private String client;
    private String user;
    private String passwd;
    private String lang;
    private int poolCapacity;
    private int peakLimit;
    
    // Getters and Setters
    public String getDestination() {
        return destination;
    }
    
    public void setDestination(String destination) {
        this.destination = destination;
    }
    
    public String getAshost() {
        return ashost;
    }
    
    public void setAshost(String ashost) {
        this.ashost = ashost;
    }
    
    public String getSysnr() {
        return sysnr;
    }
    
    public void setSysnr(String sysnr) {
        this.sysnr = sysnr;
    }
    
    public String getClient() {
        return client;
    }
    
    public void setClient(String client) {
        this.client = client;
    }
    
    public String getUser() {
        return user;
    }
    
    public void setUser(String user) {
        this.user = user;
    }
    
    public String getPasswd() {
        return passwd;
    }
    
    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
    
    public String getLang() {
        return lang;
    }
    
    public void setLang(String lang) {
        this.lang = lang;
    }
    
    public int getPoolCapacity() {
        return poolCapacity;
    }
    
    public void setPoolCapacity(int poolCapacity) {
        this.poolCapacity = poolCapacity;
    }
    
    public int getPeakLimit() {
        return peakLimit;
    }
    
    public void setPeakLimit(int peakLimit) {
        this.peakLimit = peakLimit;
    }
    
    /**
     * In a real implementation, this method would create a JCoDestination
     * using the SAP JCo library. For our mock implementation, we'll just
     * return the configuration properties as a string.
     */
    public String getConnectionProperties() {
        return String.format(
            "SAP Connection: Host=%s, System=%s, Client=%s, User=%s, Language=%s",
            ashost, sysnr, client, user, lang
        );
    }
}
