package com.listapp.Fragment;

/**
 * Created by syscraft on 7/1/2017.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.listapp.API_Utility.AsyncTask_Utility.CheckNetwork;
import com.listapp.API_Utility.Ratrofit_Implementation.APIClient;
import com.listapp.API_Utility.Ratrofit_Implementation.APIInterface;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.Get_Profile.GetProfileResponse;
import com.listapp.Activity.ProfileActivity;
import com.listapp.ListAppUtil.AppUtil;
import com.listapp.ListAppUtil.PreferenceConnector;
import com.listapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetailerDetailsFragment extends Fragment implements View.OnClickListener {

    private TextView name;
    private TextView cityName;
    private ImageView edit;
    private LinearLayout parentDetailView;
    private TextView area;
    private TextView address;
    private TextView contctNumber;
    private TextView email;
    private TextView drugLicenceNumber;
    private TextView tinNumber;
    private TextView estdYear;
    private TextView contactPerson1, contactPerson2, contactPerson3, contactPerson4, contactPerson5;
    private APIInterface apiInterface;
    private Call getProfileCall;
    private String mobileNumber, deviceType, deviceToken, userId;
    private ProgressDialog progressDialog;
    private String dln;
    private String tin;
    private String estd;
    private Context context;
    private List<String> contactNames;
    private List<String> contactNumbers;
    private LinearLayout viewContact5, viewContact4, viewContact3, viewContact2, viewContact1;
    private ImageView contactOneImage;
    private View view;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ProfileActivity.heading.setText("PROFILE");
       view = inflater.inflate(R.layout.fragment_retailer_profile, null);
        setUpView();
        return view;
    }


    public void setUpView() {
        name = view.findViewById(R.id.name);
        cityName = view.findViewById(R.id.cityName);
        edit = view.findViewById(R.id.edit);
        parentDetailView = view.findViewById(R.id.parentDetailView);
        area = view.findViewById(R.id.area);
        address = view.findViewById(R.id.address);
        contctNumber = view.findViewById(R.id.contctNumber);
        contctNumber.setOnClickListener(this);
        email = view.findViewById(R.id.email);
        drugLicenceNumber = view.findViewById(R.id.drugLicenceNumber);
        tinNumber = view.findViewById(R.id.tinNumber);
        estdYear = view.findViewById(R.id.estdYear);

        viewContact5 = view.findViewById(R.id.viewContact5);
        viewContact4 = view.findViewById(R.id.viewContact4);
        viewContact3 = view.findViewById(R.id.viewContact3);
        viewContact2 = view.findViewById(R.id.viewContact2);
        viewContact1 = view.findViewById(R.id.viewContact1);
        contactOneImage = view.findViewById(R.id.contactOneImage);

        contactPerson1 = view.findViewById(R.id.contactPerson1);
        contactPerson2 = view.findViewById(R.id.contactPerson2);
        contactPerson3 = view.findViewById(R.id.contactPerson3);
        contactPerson4 = view.findViewById(R.id.contactPerson4);
        contactPerson5 = view.findViewById(R.id.contactPerson5);

        contactPerson1.setOnClickListener(this);
        contactPerson2.setOnClickListener(this);
        contactPerson3.setOnClickListener(this);
        contactPerson4.setOnClickListener(this);
        contactPerson5.setOnClickListener(this);

        edit.setOnClickListener(this);

        mobileNumber = PreferenceConnector.readString(getContext(), PreferenceConnector.MOBILE_NUMBER, "");
        userId = PreferenceConnector.readString(getContext(), PreferenceConnector.USER_ID, "");
        deviceType = PreferenceConnector.readString(getContext(), PreferenceConnector.DEVICE_TYPE, "Android");
        deviceToken = PreferenceConnector.readString(getContext(), PreferenceConnector.DEVICE_TOKEN, "");

        if (CheckNetwork.isNetwordAvailable(context)) {
            getProfileDetail();
        } else
            AppUtil.showAlert(context.getString(R.string.networkError), context);
    }

    private void setProfileInfo() {

        String stName = PreferenceConnector.readString(getContext(), PreferenceConnector.SHOP_NAME, "");
        String strArea = PreferenceConnector.readString(getContext(), PreferenceConnector.AREA, "");
        String strAddress = PreferenceConnector.readString(getContext(), PreferenceConnector.ADDRESS, "");
        String strEmail = PreferenceConnector.readString(getContext(), PreferenceConnector.EMAIl, "");
        contactNames = PreferenceConnector.readArraylist(getContext(), PreferenceConnector.CONTACTNAMES);
        contactNumbers = PreferenceConnector.readArraylist(getContext(), PreferenceConnector.CONTACTNUMBERS);

        if (stName.equals(""))
            name.setText(" -");
        else
            name.setText(stName);

        if (strArea.equals(""))
            area.setText(" -");
        else
            area.setText(strArea);
        if (strAddress.equals(""))
            address.setText(" -");
        else
            address.setText(strAddress);
        if (strEmail.equals(""))
            email.setText(" -");
        else
            email.setText(strEmail);

        contctNumber.setText(PreferenceConnector.readString(getContext(), PreferenceConnector.MOBILE_NUMBER, "-"));

        dln = PreferenceConnector.readString(getContext(), PreferenceConnector.DL_NUMBER, " -");
        if (!dln.equals("") && !dln.equals("0"))
            drugLicenceNumber.setText(dln);
        else
            drugLicenceNumber.setText(" -");

        tin = PreferenceConnector.readString(getContext(), PreferenceConnector.TIN_NUMBER, " -");
        if (!tin.equals("") && !tin.equals("0"))
            tinNumber.setText(tin);
        else
            tinNumber.setText(" -");

        estd = PreferenceConnector.readString(getContext(), PreferenceConnector.ESTD_YEAR, " -");
        if (!estd.equals("") && !estd.equals("0000"))
            estdYear.setText(estd);
        else
            estdYear.setText(" -");

    //    if (!PreferenceConnector.readString(getContext(), PreferenceConnector.User_CITY, " -").equals("0"))
            cityName.setText(PreferenceConnector.readString(getContext(), PreferenceConnector.CITY, "-").toUpperCase());

        if (contactNames != null && contactNumbers != null) {
            int length = contactNames.size();
            int length1 = contactNumbers.size();
            if (length == length1) {
                if (contactNames.size() != 0 && contactNumbers.size() != 0) {

                    contactOneImage.setVisibility(View.VISIBLE);
                    if (length == 1) {
                        contactPerson1.setText(contactNames.get(0) + " : " +
                                contactNumbers.get(0));

                        viewContact1.setVisibility(View.VISIBLE);
                        viewContact2.setVisibility(View.GONE);
                        viewContact3.setVisibility(View.GONE);
                        viewContact4.setVisibility(View.GONE);
                        viewContact5.setVisibility(View.GONE);
                    } else if (length == 2) {
                        contactPerson1.setText(contactNames.get(0) + " : " +
                                contactNumbers.get(0));

                        contactPerson2.setText(contactNames.get(1) + " : " +
                                contactNumbers.get(1));

                        viewContact1.setVisibility(View.VISIBLE);
                        viewContact2.setVisibility(View.VISIBLE);
                        viewContact3.setVisibility(View.GONE);
                        viewContact4.setVisibility(View.GONE);
                        viewContact5.setVisibility(View.GONE);
                    } else if (length == 3) {

                        contactPerson1.setText(contactNames.get(0) + " : " +
                                contactNumbers.get(0));

                        contactPerson2.setText(contactNames.get(1) + " : " +
                                contactNumbers.get(1));

                        contactPerson3.setText(contactNames.get(2) + " : " +
                                contactNumbers.get(2));

                        viewContact1.setVisibility(View.VISIBLE);
                        viewContact2.setVisibility(View.VISIBLE);
                        viewContact3.setVisibility(View.VISIBLE);
                        viewContact4.setVisibility(View.GONE);
                        viewContact5.setVisibility(View.GONE);
                    } else if (length == 4) {
                        contactPerson1.setText(contactNames.get(0) + " : " +
                                contactNumbers.get(0));

                        contactPerson2.setText(contactNames.get(1) + " : " +
                                contactNumbers.get(1));

                        contactPerson3.setText(contactNames.get(2) + " : " +
                                contactNumbers.get(2));

                        contactPerson4.setText(contactNames.get(3) + " : " +
                                contactNumbers.get(3));

                        viewContact1.setVisibility(View.VISIBLE);
                        viewContact2.setVisibility(View.VISIBLE);
                        viewContact3.setVisibility(View.VISIBLE);
                        viewContact4.setVisibility(View.VISIBLE);
                        viewContact5.setVisibility(View.GONE);
                    } else if (length == 5) {

                        contactPerson1.setText(contactNames.get(0) + " : " +
                                contactNumbers.get(0));

                        contactPerson2.setText(contactNames.get(1) + " : " +
                                contactNumbers.get(1));

                        contactPerson3.setText(contactNames.get(2) + " : " +
                                contactNumbers.get(2));

                        contactPerson4.setText(contactNames.get(3) + " : " +
                                contactNumbers.get(3));

                        contactPerson5.setText(contactNames.get(4) + " : " +
                                contactNumbers.get(4));

                        viewContact1.setVisibility(View.VISIBLE);
                        viewContact2.setVisibility(View.VISIBLE);
                        viewContact3.setVisibility(View.VISIBLE);
                        viewContact4.setVisibility(View.VISIBLE);
                        viewContact5.setVisibility(View.VISIBLE);
                    }
                } else {
                    contactPerson1.setText(" -");
                    contactOneImage.setVisibility(View.GONE);
                    viewContact1.setVisibility(View.VISIBLE);
                    viewContact2.setVisibility(View.GONE);
                    viewContact3.setVisibility(View.GONE);
                    viewContact4.setVisibility(View.GONE);
                    viewContact5.setVisibility(View.GONE);
                }
            } else {
                contactPerson1.setText(" -");
                contactOneImage.setVisibility(View.GONE);
                viewContact1.setVisibility(View.VISIBLE);
                viewContact2.setVisibility(View.GONE);
                viewContact3.setVisibility(View.GONE);
                viewContact4.setVisibility(View.GONE);
                viewContact5.setVisibility(View.GONE);
            }
        } else {
            contactPerson1.setText(" -");
            contactOneImage.setVisibility(View.GONE);
            viewContact1.setVisibility(View.VISIBLE);
            viewContact2.setVisibility(View.GONE);
            viewContact3.setVisibility(View.GONE);
            viewContact4.setVisibility(View.GONE);
            viewContact5.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.edit:
                if (CheckNetwork.isNetwordAvailable(context)) {
                    getFragmentManager().beginTransaction().replace(R.id.homeActivityFragment, new EditProfileFragment(), "").addToBackStack("edit profile").commit();
                } else
                    AppUtil.showAlert(context.getString(R.string.networkError), context);
                break;

            case R.id.contctNumber:
                String mobileNumber = contctNumber.getText().toString().trim();
                if (!mobileNumber.equals("") && !mobileNumber.equals(" -") && mobileNumber.length() > 8)
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobileNumber)));
                break;

            case R.id.contactPerson1:
                if (contactNumbers != null && contactNumbers.size() != 0) {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contactNumbers.get(0))));
                }
                break;

            case R.id.contactPerson2:
                if (contactNumbers != null && contactNumbers.size() != 0) {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contactNumbers.get(1))));
                }
                break;
            case R.id.contactPerson3:
                if (contactNumbers != null && contactNumbers.size() != 0) {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contactNumbers.get(2))));
                }
                break;
            case R.id.contactPerson4:
                if (contactNumbers != null && contactNumbers.size() != 0) {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contactNumbers.get(3))));
                }
                break;
            case R.id.contactPerson5:
                if (contactNumbers != null && contactNumbers.size() != 0) {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contactNumbers.get(4))));
                }
                break;
        }
    }

    private void getProfileDetail() {

        progressDialog = AppUtil.createProgressDialog(getContext());
        if (!progressDialog.isShowing())
            progressDialog.show();

        apiInterface = APIClient.getClient().create(APIInterface.class);
        getProfileCall = apiInterface.getProfileDetail(mobileNumber, userId, deviceType, deviceToken);
        getProfileCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                if(progressDialog!=null)
                progressDialog.dismiss();
                GetProfileResponse getProfileResponse = (GetProfileResponse) response.body();
                if (getProfileResponse != null) {

                    if (getProfileResponse.getError().equals("0")) {
                        PreferenceConnector.writeBoolean(getContext(), "firstTimeGetProfile1", true);
                        PreferenceConnector.writeString(getContext(), PreferenceConnector.FULL_NAME, getProfileResponse.getData().getRetailerName());
                        PreferenceConnector.writeString(getContext(), PreferenceConnector.USER_ID, getProfileResponse.getData().getUserId());
                        PreferenceConnector.writeString(getContext(), PreferenceConnector.EMAIl, getProfileResponse.getData().getEmail());
                        PreferenceConnector.writeString(getContext(), PreferenceConnector.AREA, getProfileResponse.getData().getArea());
                        PreferenceConnector.writeString(getContext(), PreferenceConnector.ADDRESS, getProfileResponse.getData().getAddress());
                        PreferenceConnector.writeString(getContext(), PreferenceConnector.User_CITY, getProfileResponse.getData().getCity());
                        PreferenceConnector.writeString(getContext(), PreferenceConnector.STATE, getProfileResponse.getData().getState());
                        PreferenceConnector.writeString(getContext(), PreferenceConnector.SHOP_NAME, getProfileResponse.getData().getShopName());
                        PreferenceConnector.writeString(getContext(), PreferenceConnector.CONTACT_NUMBER, getProfileResponse.getData().getContactNumber());
                        PreferenceConnector.writeString(getContext(), PreferenceConnector.TIN_NUMBER, getProfileResponse.getData().getTinNumber());
                        PreferenceConnector.writeString(getContext(), PreferenceConnector.ESTD_YEAR, getProfileResponse.getData().getEstdYear());
                        PreferenceConnector.writeString(getContext(), PreferenceConnector.DL_NUMBER, getProfileResponse.getData().getDrugLicNo());
                        PreferenceConnector.writeString(getContext(), PreferenceConnector.User_CITY_ID, getProfileResponse.getData().getCityId());
                        PreferenceConnector.writeString(getContext(), PreferenceConnector.STATE_ID, getProfileResponse.getData().getStateId());

                        List<GetProfileResponse.Data.ContactPerson> list = getProfileResponse.getData().getContactPerson();
                        List<String> contactNames, contactNumbers;
                        contactNames = new ArrayList<String>();
                        contactNumbers = new ArrayList<String>();

                        if (list != null) {
                            int length = list.size();
                            for (int i = 0; i < length; i++) {
                                contactNames.add(list.get(i).getContactName());
                                contactNumbers.add(list.get(i).getContactNumber());
                            }
                        }
                        PreferenceConnector.writeArraylist(getContext(), PreferenceConnector.CONTACTNAMES, contactNames);
                        PreferenceConnector.writeArraylist(getContext(), PreferenceConnector.CONTACTNUMBERS, contactNumbers);

                        setProfileInfo();
                    } else {
                        if (getProfileResponse.getMessage().equalsIgnoreCase("Your session has been expired")) {
                            AppUtil.sessionLogout(getProfileResponse.getMessage(), getContext());
                        } else if (getProfileResponse.getMessage().equalsIgnoreCase("Your account has been deactivated by administrator"))
                            AppUtil.sessionLogout(getProfileResponse.getMessage(), context);
                        else
                            AppUtil.showAlert(getProfileResponse.getMessage(), getContext());
                    }
                } else
                    AppUtil.showAlert(context.getString(R.string.serverError), getContext());
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                AppUtil.showAlert(context.getString(R.string.serverError), getContext());
                if(progressDialog!=null)
                progressDialog.dismiss();
            }
        });
    }
}

