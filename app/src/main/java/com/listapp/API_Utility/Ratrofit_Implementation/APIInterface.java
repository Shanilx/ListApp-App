package com.listapp.API_Utility.Ratrofit_Implementation;

import com.listapp.API_Utility.Ratrofit_Implementation.Model.AboutUs.AboutUsResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.CheckMobileNumber.CheckNumberResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.City.Cities;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.CompanySearch.CompanyDetailResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.CompanySearch.CompanySearchResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.Edit_Profile.ChangeTinDlNumberResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.Edit_Profile.EditProfileResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.Get_Profile.GetProfileResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.Login_Response.LoginResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.MobileNumberChange.NewOTPResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.MobileNumberChange.NewResendOTPResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.Not_Find_Product.NotFindProductResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.Notification.NotificationModel;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.Notification.NotificationRead;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.OtpVerify.OTPResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.Resend_OTP.ResendOTPResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.ResetPassword.ResetPasswordResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.SearchSupplier.SearchSupplierResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.Search_Product.ProductDetails;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.Search_Product.SearchProductResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.SignUp.SignUpResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.Splash_Response.AuthenticationResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.Splash_Response.StateCityResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.SupplierDetails.SupplierProfile;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.TermsAndCondition.TermsAndConditionResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.banner.BannerModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


/**
 *
 */

public interface APIInterface {

    String BASE_URL = "http://listapp.in";
    //String BASE_URL = "http://dev.listapp.in";
    //Live Url = http://listapp.in
    String API_BASE_URL = "/api/Login/";

    String COMPANY_NOT_FOUND = BASE_URL + API_BASE_URL + "notFoundRequest";
    String PRODUCT_NOT_FOUND = BASE_URL + API_BASE_URL + "notFoundRequest";
    String SUPPLIER_NOT_FOUND = BASE_URL + API_BASE_URL + "notFoundSupplier";

    @FormUrlEncoded
    @POST(API_BASE_URL + "login")
    Call<LoginResponse> login(@Field("mobile_no") String mobileNumber, @Field("password") String password,
                              @Field("device_type") String deviceType, @Field("device_token") String deviceToken);

    @FormUrlEncoded
    @POST(API_BASE_URL + "checkDeviceReg")
    Call<AuthenticationResponse> checkRegistration(@Field("mobile_no") String mobileNumber, @Field("mobile_unique_id") String mobileUniqueID,
                                                   @Field("device_type") String deviceType, @Field("device_token") String deviceToken);

    @GET(API_BASE_URL + "StateCity")
    Call<StateCityResponse> getStateCity();

    @FormUrlEncoded
    @POST(API_BASE_URL + "signUp")
    Call<SignUpResponse> sinUp(@Field("mobile_no") String mobileNumber, @Field("user_type") String user_type,
                               @Field("shop_name") String shop_name, @Field("password") String password,
                               @Field("mobile_unique_id") String mobile_unique_id, @Field("device_type") String device_type,
                               @Field("device_token") String device_token, @Field("city_id") String city_id);

    @GET(API_BASE_URL + "getTermsCondition")
    Call<TermsAndConditionResponse> getTerms();

    @FormUrlEncoded
    @POST(API_BASE_URL + "otpVerify")
    Call<OTPResponse> otpVerify(@Field("mobile_no") String mobileNumber, @Field("otp") String otp,
                                @Field("device_type") String deviceType, @Field("device_token") String deviceToken);

    @FormUrlEncoded
    @POST(API_BASE_URL + "resendOTP")
    Call<ResendOTPResponse> resendOTp(@Field("mobile_no") String mobileNumber,
                                      @Field("device_type") String deviceType, @Field("device_token") String deviceToken);

    @FormUrlEncoded
    @POST(API_BASE_URL + "resetPassword")
    Call<ResetPasswordResponse> resetPassword(@Field("mobile_no") String mobileNumber, @Field("new_password") String newPassword,
                                              @Field("device_type") String deviceType, @Field("device_token") String deviceToken);

    @GET(API_BASE_URL + "getAboutUs")
    Call<AboutUsResponse> getAboutUs();

    @FormUrlEncoded
    @POST(API_BASE_URL + "userLogout")
    Call<ResetPasswordResponse> userLogout(@Field("mobile_no") String mobileNumber, @Field("user_id") String user_id,
                                           @Field("device_type") String deviceType, @Field("device_token") String deviceToken);

    @FormUrlEncoded
    @POST(API_BASE_URL + "retailerProfileDetail")
    Call<GetProfileResponse> getProfileDetail(@Field("mobile_no") String mobileNumber, @Field("user_id") String user_id,
                                              @Field("device_type") String deviceType, @Field("device_token") String deviceToken);

    @FormUrlEncoded
    @POST(API_BASE_URL + "editRetailerProfile")
    Call<EditProfileResponse> editProfileDetail(@Field("mobile_no") String mobileNumber, @Field("dl_no") String dlNo,
                                                @Field("tin_no") String tinNo, @Field("estd_year") String estd_year,
                                                @Field("area") String area, @Field("city_id") String city_id,
                                                @Field("state_id") String state_id, @Field("device_token") String deviceToken,
                                                @Field("shop_name") String shop_name, @Field("address") String address,
                                                @Field("user_id") String userId, @Field("full_name") String full_name,
                                                @Field("email") String email, @Field("device_type") String deviceType,
                                                @Field("contact_name_1") String contactName1,
                                                @Field("contact_name_2") String contactName2, @Field("contact_name_3") String contactName3,
                                                @Field("contact_name_4") String contactName4, @Field("contact_name_5") String contactName5,
                                                @Field("contact_number_1") String contactNumber1, @Field("contact_number_2") String contactNumber2,
                                                @Field("contact_number_3") String contactNumber3, @Field("contact_number_4") String contactNumber4,
                                                @Field("contact_number_5") String contactNumber5);

    @FormUrlEncoded
    @POST(API_BASE_URL + "changeLicenceAndTin")
    Call<ChangeTinDlNumberResponse> changeTinDlNumber(@Field("mobile_no") String mobileNumber, @Field("user_id") String user_id,
                                                      @Field("device_type") String deviceType, @Field("device_token") String deviceToken,
                                                      @Field("tin_number") String tin, @Field("drug_licence_number") String dln);

    @FormUrlEncoded
    @POST(API_BASE_URL + "searchProduct")
    Call<SearchProductResponse> searchProduct(@Field("mobile_no") String mobileNumber, @Field("user_id") String user_id,
                                              @Field("device_type") String deviceType, @Field("device_token") String deviceToken,
                                              @Field("product_name") String productName, @Field("offset") String offset,
                                              @Field("city_id") String cityID);

    @FormUrlEncoded
    @POST(API_BASE_URL + "searchNearBySupplier")
    Call<ProductDetails> getProductDetail(@Field("mobile_no") String mobileNumber, @Field("user_id") String user_id,
                                          @Field("device_type") String deviceType, @Field("device_token") String deviceToken,
                                          @Field("product_id") String productID, @Field("offset") String offset,
                                          @Field("city_id") String cityID);

    @FormUrlEncoded
    @POST(API_BASE_URL + "getSupplierDetail")
    Call<SupplierProfile> getSupplierProfile(@Field("mobile_no") String mobileNumber, @Field("user_id") String user_id,
                                             @Field("device_type") String deviceType, @Field("device_token") String deviceToken,
                                             @Field("supplier_id") String supplierID);

    @Multipart
    @POST(API_BASE_URL + "notFoundRequest")
    Call<NotFindProductResponse> postImageFile(@Part MultipartBody.Part file, @Part("file_name") RequestBody name,
                                               @Part("mobile_no") String mobileNumber, @Part("user_id") String user_id,
                                               @Part("device_type") String deviceType, @Part("device_token") String deviceToken,
                                               @Part("issue") String issue, @Part("description") String description, @Part("city") String cityname);

    @FormUrlEncoded
    @POST(API_BASE_URL + "searchSupplier")
    Call<SearchSupplierResponse> searchSupplier(@Field("mobile_no") String mobileNumber, @Field("user_id") String user_id,
                                                @Field("device_type") String deviceType, @Field("device_token") String deviceToken,
                                                @Field("shop_name") String supplierName, @Field("offset") String offset,
                                                @Field("city_id") String cityID);

    @FormUrlEncoded
    @POST(API_BASE_URL + "searchCompany")
    Call<CompanySearchResponse> searchCompany(@Field("mobile_no") String mobileNumber, @Field("user_id") String user_id,
                                              @Field("device_type") String deviceType, @Field("device_token") String deviceToken,
                                              @Field("company_name") String companyName, @Field("offset") String offset,
                                              @Field("city_id") String cityID);

    @FormUrlEncoded
    @POST(API_BASE_URL + "companyDetail")
    Call<CompanyDetailResponse> companyDetail(@Field("mobile_no") String mobileNumber, @Field("user_id") String user_id,
                                              @Field("device_type") String deviceType, @Field("device_token") String deviceToken,
                                              @Field("company_id") String companyID, @Field("offset") String offset, @Field("city") String city);


    @FormUrlEncoded
    @POST(API_BASE_URL + "otpVerifyNew")
    Call<NewOTPResponse> otpVerifyNew(@Field("mobile_no") String mobileNumber, @Field("new_mobile_no") String newMobileNumber,
                                      @Field("new_otp") String otp, @Field("device_type") String deviceType,
                                      @Field("device_token") String deviceToken, @Field("user_id") String userId);

    @FormUrlEncoded
    @POST(API_BASE_URL + "SendOtpForNew")
    Call<NewResendOTPResponse> sendOtpForNew(@Field("mobile_no") String mobileNumber, @Field("new_mobile_no") String newMobileNumber,
                                             @Field("device_type") String deviceType, @Field("device_token") String deviceToken,
                                             @Field("user_id") String userId);

    @FormUrlEncoded
    @POST(API_BASE_URL + "getNotification")
    Call<NotificationModel> getNotification(@Field("mobile_no") String mobileNumber, @Field("device_type") String deviceType,
                                            @Field("device_token") String deviceToken, @Field("user_id") String userId);

    @FormUrlEncoded
    @POST(API_BASE_URL + "readNotification")
    Call<NotificationRead> readNotification(@Field("mobile_no") String mobileNumber, @Field("device_type") String deviceType,
                                            @Field("device_token") String deviceToken, @Field("user_id") String userId,
                                            @Field("notification_id") String notificationID, @Field("notification_type") String type);

    @FormUrlEncoded
    @POST(API_BASE_URL + "checkMobile")
    Call<CheckNumberResponse> verifyMobile(@Field("mobile_no") String mobileNumber);

    @GET(API_BASE_URL + "getSupplierCities")
    Call<Cities> getSupplierCity();

    @GET(API_BASE_URL + "getCity")
    Call<Cities> getCityID(@Query("city") String name);

    @GET(API_BASE_URL + "getCityById")
    Call<Cities> getCityByID(@Query("city") String name);

    @POST(API_BASE_URL + "postNotification")
    Call<NotificationModel> postNotification(@Field("mobile_no") String mobileNumber, @Field("device_type") String deviceType,
                                             @Field("device_token") String deviceToken, @Field("user_id") String userId, @Field("msg") String msg, @Field("notification_id") String id, @Field("title") String title, @Field("type") String type);

    @GET(API_BASE_URL + "getstates")
    Call<Cities> getstates();

    @GET(API_BASE_URL + "getCityByState")
    Call<Cities> getCityByState(@Query("state") String state);

    @FormUrlEncoded
    @POST(API_BASE_URL + "listSuppliers")
    Call<List<ProductDetails.Data.Supplier>> listSuppliers(@Field("mobile_no") String mobileNumber,
                                                           @Field("shop_name") String supplierName, @Field("offset") String offset,
                                                           @Field("city_id") String cityID);

    @FormUrlEncoded
    @POST(API_BASE_URL + "createOrderList")
    Call<String> createOrderList(@Field("product") String product, @Field("supplier") String supplier, @Field("quantity") String quantity,
                                 @Field("scheme") String scheme, @Field("user") String user, @Field("contact") String contact,
                                 @Field("isordered") String isordered);

    @GET(API_BASE_URL + "getState")
    Call<Cities> getStateID(@Query("state") String name);

    @GET(API_BASE_URL + "getSupplierId")
    Call<Cities> getSupplierID(@Query("city") String name);

    @GET(API_BASE_URL +"bannerlist?")
    Call<BannerModel> getBanner();
}
