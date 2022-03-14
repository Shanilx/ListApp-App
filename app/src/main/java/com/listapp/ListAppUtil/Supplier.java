package com.listapp.ListAppUtil;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "suppliers",indices = {@Index(value={"supplier"},unique = true)})
public class Supplier {
    private String AuthorisedStatus;
    private String CityName;
    private String SupplierAddress;
    private String SupplierId;
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "supplier")
    private String SupplierName;
    private String ShopName;

    public String getAuthorisedStatus() {
        return AuthorisedStatus;
    }

    public void setAuthorisedStatus(String authorisedStatus) {
        AuthorisedStatus = authorisedStatus;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getSupplierAddress() {
        return SupplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        SupplierAddress = supplierAddress;
    }

    public String getSupplierId() {
        return SupplierId;
    }

    public void setSupplierId(String supplierId) {
        SupplierId = supplierId;
    }

    public String getSupplierName() {
        return SupplierName;
    }

    public void setSupplierName(String supplierName) {
        SupplierName = supplierName;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }
}
