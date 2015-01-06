package com.skysoft.domain;

/**
 * User: pinaster
 * Date: 14-1-10
 * Time: 上午10:15
 */
public class SFDARule {
    private String gatherName;
    private String siteDomain;
    private String describe;
    private String characterset;
    private int tableId;
    private int startID;
    private int EndID;
    private int batchUpdate;
    private String mappingxml;

    public String getGatherName() {
        return gatherName;
    }

    public void setGatherName(String gatherName) {
        this.gatherName = gatherName;
    }

    public String getSiteDomain() {
        return siteDomain;
    }

    public void setSiteDomain(String siteDomain) {
        this.siteDomain = siteDomain;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getCharacterset() {
        return characterset;
    }

    public void setCharacterset(String characterset) {
        this.characterset = characterset;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int getStartID() {
        return startID;
    }

    public void setStartID(int startID) {
        this.startID = startID;
    }

    public int getEndID() {
        return EndID;
    }

    public void setEndID(int endID) {
        EndID = endID;
    }

    public int getBatchUpdate() {
        return batchUpdate;
    }

    public void setBatchUpdate(int batchUpdate) {
        this.batchUpdate = batchUpdate;
    }

    public String getMappingxml() {
        return mappingxml;
    }

    public void setMappingxml(String mappingxml) {
        this.mappingxml = mappingxml;
    }
}
