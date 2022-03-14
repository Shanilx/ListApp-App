
package com.listapp.API_Utility.Ratrofit_Implementation.Model.Search_Product;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductDetails {

    @SerializedName("data")
    private Data mData;
    @SerializedName("error")
    private String mError;
    @SerializedName("message")
    private String mMessage;

    public Data getData() {
        return mData;
    }

    public void setData(Data data) {
        mData = data;
    }

    public String getError() {
        return mError;
    }

    public void setError(String error) {
        mError = error;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public class Data {

        @SerializedName("product")
        private Product mProduct;
        @SerializedName("suppliers")
        private List<Supplier> mSuppliers;

        public Product getProduct() {
            return mProduct;
        }

        public void setProduct(Product product) {
            mProduct = product;
        }

        public List<Supplier> getSuppliers() {
            return mSuppliers;
        }

        public void setSuppliers(List<Supplier> suppliers) {
            mSuppliers = suppliers;
        }


        public class Supplier {

            @SerializedName("authorised_status")
            private String mAuthorisedStatus;
            @SerializedName("city_name")
            private String mCityName;
            @SerializedName("supplier_address")
            private String mSupplierAddress;
            @SerializedName("supplier_id")
            private String mSupplierId;
            @SerializedName("supplier_name")
            private String mSupplierName;
            @SerializedName("shop_name")
            private String mShopName;

            public String getAuthorisedStatus() {
                return mAuthorisedStatus;
            }

            public void setAuthorisedStatus(String authorisedStatus) {
                mAuthorisedStatus = authorisedStatus;
            }

            public String getCityName() {
                return mCityName;
            }

            public void setCityName(String cityName) {
                mCityName = cityName;
            }

            public String getSupplierAddress() {
                return mSupplierAddress;
            }

            public void setSupplierAddress(String supplierAddress) {
                mSupplierAddress = supplierAddress;
            }

            public String getSupplierId() {
                return mSupplierId;
            }

            public void setSupplierId(String supplierId) {
                mSupplierId = supplierId;
            }

            public String getSupplierName() {
                return mSupplierName;
            }

            public void setSupplierName(String supplierName) {
                mSupplierName = supplierName;
            }

            public String getShopName() {
                return mShopName;
            }

            public void setShopName(String shopName) {
                mShopName = shopName;
            }
        }

        public class Product {

            @SerializedName("city_name")
            private String mCityName;
            @SerializedName("composition")
            private String mComposition;
            @SerializedName("favourite_status")
            private String mFavouriteStatus;
            @SerializedName("menufecture_by")
            private String mMenufectureBy;
            @SerializedName("packing_detail")
            private String mPackingDetail;
            @SerializedName("price")
            private String mPrice;
            @SerializedName("product_id")
            private String mProductId;
            @SerializedName("product_name")
            private String mProductName;
            @SerializedName("used_for_treatment")
            private String mUsedForTreatment;

            public String getCityName() {
                return mCityName;
            }

            public void setCityName(String cityName) {
                mCityName = cityName;
            }

            public String getComposition() {
                return mComposition;
            }

            public void setComposition(String composition) {
                mComposition = composition;
            }

            public String getFavouriteStatus() {
                return mFavouriteStatus;
            }

            public void setFavouriteStatus(String favouriteStatus) {
                mFavouriteStatus = favouriteStatus;
            }

            public String getMenufectureBy() {
                return mMenufectureBy;
            }

            public void setMenufectureBy(String menufectureBy) {
                mMenufectureBy = menufectureBy;
            }

            public String getPackingDetail() {
                return mPackingDetail;
            }

            public void setPackingDetail(String packingDetail) {
                mPackingDetail = packingDetail;
            }

            public String getPrice() {
                return mPrice;
            }

            public void setPrice(String price) {
                mPrice = price;
            }

            public String getProductId() {
                return mProductId;
            }

            public void setProductId(String productId) {
                mProductId = productId;
            }

            public String getProductName() {
                return mProductName;
            }

            public void setProductName(String productName) {
                mProductName = productName;
            }

            public String getUsedForTreatment() {
                return mUsedForTreatment;
            }

            public void setUsedForTreatment(String usedForTreatment) {
                mUsedForTreatment = usedForTreatment;
            }

        }

    }
}
