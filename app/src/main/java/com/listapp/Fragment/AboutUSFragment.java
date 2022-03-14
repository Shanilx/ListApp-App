package com.listapp.Fragment;


import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.listapp.API_Utility.AsyncTask_Utility.CheckNetwork;
import com.listapp.API_Utility.Ratrofit_Implementation.APIClient;
import com.listapp.API_Utility.Ratrofit_Implementation.APIInterface;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.AboutUs.AboutUsResponse;
import com.listapp.ListAppUtil.AppUtil;
import com.listapp.ListAppUtil.PreferenceConnector;
import com.listapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutUSFragment extends Fragment {

    private ImageView image;
    private TextView heading, content;
    private APIInterface apiInterface;
    private Call getAboutUsCall;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.about_us, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        image = view.findViewById(R.id.image);
        heading = view.findViewById(R.id.heading);
        content = view.findViewById(R.id.content);

        if (CheckNetwork.isNetwordAvailable(getContext())) {

            progressDialog = AppUtil.createProgressDialog(getContext());
            if (!progressDialog.isShowing())
                progressDialog.show();

            apiInterface = APIClient.getClient().create(APIInterface.class);
            getAboutUsCall = apiInterface.getAboutUs();
            getAboutUsCall.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if(progressDialog!=null)
                    progressDialog.dismiss();
                    AboutUsResponse aboutUsResponse = (AboutUsResponse) response.body();
                    if (aboutUsResponse != null) {
                        if (aboutUsResponse.getError().equals("0")) {
                            PreferenceConnector.writeString(getContext(), PreferenceConnector.ABOUT_US_CONTENT, aboutUsResponse.getData().get(0).getAboutContent());

                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
                                content.setText(Html.fromHtml(aboutUsResponse.getData().get(0).getAboutContent()));
                            else
                                content.setText((Html.fromHtml(aboutUsResponse.getData().get(0).getAboutContent(),Html.FROM_HTML_MODE_LEGACY)));

                            content.setMovementMethod(LinkMovementMethod.getInstance());
                        } else {
                            if (aboutUsResponse.getMessage().equalsIgnoreCase("Your session has been expired")) {
                                AppUtil.sessionLogout(aboutUsResponse.getMessage(), getContext());
                            } else if (aboutUsResponse.getMessage().equalsIgnoreCase("Your account has been deactivated by administrator"))
                                AppUtil.sessionLogout(aboutUsResponse.getMessage(), getContext());
                            else {
                                AppUtil.showAlert(aboutUsResponse.getMessage(), getContext());
                                content.setText(PreferenceConnector.readString(getContext(), PreferenceConnector.ABOUT_US_CONTENT, "Not Available"));
                                content.setMovementMethod(LinkMovementMethod.getInstance());
                            }
                        }

                    } else {
                        AppUtil.showAlert(getContext().getString(R.string.serverError), getContext());
                        content.setText(PreferenceConnector.readString(getContext(), PreferenceConnector.ABOUT_US_CONTENT, "Not Available"));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    if(progressDialog!=null)
                    progressDialog.dismiss();
                    AppUtil.showAlert(getContext().getString(R.string.serverError), getContext());
                    content.setText(PreferenceConnector.readString(getContext(), PreferenceConnector.ABOUT_US_CONTENT, "Not Available"));
                }
            });
        } else
            AppUtil.showAlert(getContext().getString(R.string.networkError), getContext());
    }
}
