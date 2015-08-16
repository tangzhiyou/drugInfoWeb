package com.skysoft.domain;

public class Druggds {
    private Long id;

    private String approvalnumber;

    private String productname;

    private String englishname;

    private String brandname;

    private String preparation;

    private String dimension;

    private String productionunit;

    private String productionaddress;

    private String productcategory;

    private String originalapproval;

    private String approvaldate;

    private String drugcode;

    private String drugcoderemark;

    private String associateddata;

    private byte[] comments;

    public Druggds(Long id, String approvalnumber, String productname, String englishname, String brandname, String preparation, String dimension, String productionunit, String productionaddress, String productcategory, String originalapproval, String approvaldate, String drugcode, String drugcoderemark, String associateddata, byte[] comments) {
        this.id = id;
        this.approvalnumber = approvalnumber;
        this.productname = productname;
        this.englishname = englishname;
        this.brandname = brandname;
        this.preparation = preparation;
        this.dimension = dimension;
        this.productionunit = productionunit;
        this.productionaddress = productionaddress;
        this.productcategory = productcategory;
        this.originalapproval = originalapproval;
        this.approvaldate = approvaldate;
        this.drugcode = drugcode;
        this.drugcoderemark = drugcoderemark;
        this.associateddata = associateddata;
        this.comments = comments;
    }

    public Druggds() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApprovalnumber() {
        return approvalnumber;
    }

    public void setApprovalnumber(String approvalnumber) {
        this.approvalnumber = approvalnumber == null ? null : approvalnumber.trim();
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname == null ? null : productname.trim();
    }

    public String getEnglishname() {
        return englishname;
    }

    public void setEnglishname(String englishname) {
        this.englishname = englishname == null ? null : englishname.trim();
    }

    public String getBrandname() {
        return brandname;
    }

    public void setBrandname(String brandname) {
        this.brandname = brandname == null ? null : brandname.trim();
    }

    public String getPreparation() {
        return preparation;
    }

    public void setPreparation(String preparation) {
        this.preparation = preparation == null ? null : preparation.trim();
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension == null ? null : dimension.trim();
    }

    public String getProductionunit() {
        return productionunit;
    }

    public void setProductionunit(String productionunit) {
        this.productionunit = productionunit == null ? null : productionunit.trim();
    }

    public String getProductionaddress() {
        return productionaddress;
    }

    public void setProductionaddress(String productionaddress) {
        this.productionaddress = productionaddress == null ? null : productionaddress.trim();
    }

    public String getProductcategory() {
        return productcategory;
    }

    public void setProductcategory(String productcategory) {
        this.productcategory = productcategory == null ? null : productcategory.trim();
    }

    public String getOriginalapproval() {
        return originalapproval;
    }

    public void setOriginalapproval(String originalapproval) {
        this.originalapproval = originalapproval == null ? null : originalapproval.trim();
    }

    public String getApprovaldate() {
        return approvaldate;
    }

    public void setApprovaldate(String approvaldate) {
        this.approvaldate = approvaldate == null ? null : approvaldate.trim();
    }

    public String getDrugcode() {
        return drugcode;
    }

    public void setDrugcode(String drugcode) {
        this.drugcode = drugcode == null ? null : drugcode.trim();
    }

    public String getDrugcoderemark() {
        return drugcoderemark;
    }

    public void setDrugcoderemark(String drugcoderemark) {
        this.drugcoderemark = drugcoderemark == null ? null : drugcoderemark.trim();
    }

    public String getAssociateddata() {
        return associateddata;
    }

    public void setAssociateddata(String associateddata) {
        this.associateddata = associateddata == null ? null : associateddata.trim();
    }

    public byte[] getComments() {
        return comments;
    }

    public void setComments(byte[] comments) {
        this.comments = comments;
    }
}