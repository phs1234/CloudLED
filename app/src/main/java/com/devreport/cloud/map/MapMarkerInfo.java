package com.devreport.cloud.map;

public class MapMarkerInfo {
    private int id;
    private String name;

    private double lon;
    private double lat;

    private String desc;

    public MapMarkerInfo(int id, String name, double lat, double lon, String desc) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(int lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
