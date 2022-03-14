package com.listapp.ListAppUtil;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "orders",indices = {@Index(value = {"product", "supplier"},
        unique = true),@Index(value={"product"},unique = true)})
public class Order {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "product")
    private String product;

    @ColumnInfo(name = "supplier")
    private String supplier;

    private String company;
    
    private String quantity;

    private  String user;

    private  String scheme;
    
    private  String whatsappnumber;

    private  String othernumber;
    
    private String city;

    private String type;

    private String newproduct;

    private String notes;

    private String supplierid;

    private String isordered;

    public String getIsordered() {
        return isordered;
    }

    public void setIsordered(String isordered) {
        this.isordered = isordered;
    }

    public String getSupplierid() {
        return supplierid;
    }

    public void setSupplierid(String supplierid) {
        this.supplierid = supplierid;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNewproduct() {
        return newproduct;
    }

    public void setNewproduct(String newproduct) {
        this.newproduct = newproduct;
    }

    public String getWhatsappnumber() {
        return whatsappnumber;
    }

    public void setWhatsappnumber(String whatsappnumber) {
        this.whatsappnumber = whatsappnumber;
    }

    public String getOthernumber() {
        return othernumber;
    }

    public void setOthernumber(String othernumber) {
        this.othernumber = othernumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }
    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
