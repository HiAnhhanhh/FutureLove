package com.thinkdiffai.futurelove.modelfor4gdomain;


import com.google.gson.annotations.SerializedName;

public class NetworkModel {
    @SerializedName("as")
    private String asX;
    @SerializedName("city")
    private String city;
    @SerializedName("country")
    private String country;
    @SerializedName("countryCode")
    private String countryCode;
    @SerializedName("isp")
    private String isp;
    @SerializedName("lat")
    private double lat;
    @SerializedName("lon")
    private double lon;
    @SerializedName("org")
    private String org;
    @SerializedName("query")
    private String query;
    @SerializedName("region")
    private String region;
    @SerializedName("regionName")
    private String regionName;
    @SerializedName("status")
    private String status;
    @SerializedName("timezone")
    private String timezone;
    @SerializedName("zip")
    private String zip;

    public NetworkModel() {
    }

    public NetworkModel(String asX, String city, String country, String countryCode, String isp,
                        double lat, double lon, String org, String query, String region,
                        String regionName, String status, String timezone, String zip) {
        this.asX = asX;
        this.city = city;
        this.country = country;
        this.countryCode = countryCode;
        this.isp = isp;
        this.lat = lat;
        this.lon = lon;
        this.org = org;
        this.query = query;
        this.region = region;
        this.regionName = regionName;
        this.status = status;
        this.timezone = timezone;
        this.zip = zip;
    }

    // Getters and Setters (or use Lombok annotations)

    public String getAsX() {
        return asX;
    }

    public void setAsX(String asX) {
        this.asX = asX;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}