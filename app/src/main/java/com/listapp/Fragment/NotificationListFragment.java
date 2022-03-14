package com.listapp.Fragment;

/**
 * Created by syscraft on 7/28/2017.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.fragment.app.Fragment;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.listapp.API_Utility.AsyncTask_Utility.CheckNetwork;
import com.listapp.API_Utility.Ratrofit_Implementation.APIClient;
import com.listapp.API_Utility.Ratrofit_Implementation.APIInterface;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.Notification.NotificationModel;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.Notification.NotificationRead;
import com.listapp.Activity.NewSignInActivity;
import com.listapp.Activity.NotificationActivity;
import com.listapp.Activity.UpdateAppActivity;
import com.listapp.Adapter.NotificationAdapter;
import com.listapp.Interface.FragmentBack;
import com.listapp.ListAppUtil.AppUtil;
import com.listapp.ListAppUtil.PreferenceConnector;
import com.listapp.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationListFragment extends Fragment implements AdapterView.OnItemClickListener, FragmentBack {

    private ListView notificationList;
    private Context context;
    private Call<NotificationModel> getNotificationCall;
    private Call<NotificationRead> readNotification;
    private ProgressDialog progressDialog;
    private APIInterface apiInterface;
    private String mobileNumber, userId, deviceType, deviceToken;
    private List<NotificationModel.Datum> notificationListData;
    private TextView noNotificationFound;
    private RelativeLayout notificationDetailView;
    private TextView notificationTitle;
    private TextView notificationDate;
    private TextView notificationMessage;
    private TextView notificationTime;
    private boolean isDetailView, isListView, isNoFoundView;
    private NotificationAdapter notificationAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification_list, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        mobileNumber = PreferenceConnector.readString(getContext(), PreferenceConnector.MOBILE_NUMBER, "");
        userId = PreferenceConnector.readString(getContext(), PreferenceConnector.USER_ID, "");
        deviceType = PreferenceConnector.readString(getContext(), PreferenceConnector.DEVICE_TYPE, "Android");
        deviceToken = PreferenceConnector.readString(getContext(), PreferenceConnector.DEVICE_TOKEN, "");
        noNotificationFound = view.findViewById(R.id.noNotificationFound);
        notificationDetailView = view.findViewById(R.id.notificationDetailView);
        notificationTitle = view.findViewById(R.id.notificationTitle);
        notificationDate = view.findViewById(R.id.notificationDate);
        notificationMessage = view.findViewById(R.id.notificationMessage);
        noNotificationFound = view.findViewById(R.id.noNotificationFound);
        notificationList = view.findViewById(R.id.notificationList);
        notificationTime = view.findViewById(R.id.notificationTime);
        notificationList.setOnItemClickListener(this);
        NotificationActivity.heading.setText("Notifications");

        if (NotificationActivity.title == null) {
            if (CheckNetwork.isNetwordAvailable(context)) {
                getNotificationList();
            } else
                AppUtil.showAlert(context.getString(R.string.networkError), context);

            hideView();
        } else {
            if (!PreferenceConnector.readString(getContext(), PreferenceConnector.USER_ID, "").equals("")) {
                if (NotificationActivity.type.equals("2")) {
                    if (CheckNetwork.isNetwordAvailable(context))
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                                    ("market://details?id=com.listapp.in")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    PreferenceConnector.writeString(context,NotificationActivity.id,NotificationActivity.id);
                    readNotification(NotificationActivity.id, NotificationActivity.type);
                    showList();

                } else {

                    showDetail();
                    notificationTitle.setText(NotificationActivity.title);
                    notificationMessage.setText(NotificationActivity.msg);
                    notificationDate.setText(NotificationActivity.date);
                    notificationTime.setText(NotificationActivity.time);
                    if (CheckNetwork.isNetwordAvailable(context))
                        readNotification(NotificationActivity.id, NotificationActivity.type);
                }
            } else {
                AppUtil.showAlert("Please login to access the application", context);
                Intent intent = new Intent(context, NewSignInActivity.class);
                startActivity(intent);
                getActivity().finishAffinity();
            }
        }
    }

    private void getNotificationList() {

        progressDialog = AppUtil.createProgressDialog(context);
        if (!progressDialog.isShowing())
            progressDialog.show();


        getNotificationCall = apiInterface.getNotification(mobileNumber, deviceType, deviceToken, userId);
        getNotificationCall.enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {

                progressDialog.dismiss();
                if (response != null) {
                    NotificationModel notificationModel = response.body();
                    if (notificationModel != null) {
                        String error, msg;
                        error = notificationModel.getError();
                        msg = notificationModel.getMessage();
                        if (error.equals("0")) {
                            notificationListData = notificationModel.getData();
                            if (notificationListData != null && notificationListData.size() > 0) {
                                notificationAdapter = new NotificationAdapter(context, notificationListData);
                                notificationList.setAdapter(notificationAdapter);
                                NotificationActivity.title = null;
                                showList();
                            } else {
                                showNoFound();
                            }
                        } else {
                            if (msg.equalsIgnoreCase("Your session has been expired")) {
                                AppUtil.sessionLogout(msg, getContext());
                            } else if (msg.equalsIgnoreCase("Your account has been deactivated by administrator"))
                                AppUtil.sessionLogout(msg, getContext());
                            else if (msg.equals("No notifications for this user"))
                                showNoFound();
                            else {
                                AppUtil.showAlert(msg, context);
                            }
                        }
                    } else {
                        hideView();
                        AppUtil.showAlert(context.getString(R.string.serverError), context);
                    }
                } else {
                    AppUtil.showAlert(context.getString(R.string.serverError), context);
                    hideView();
                }
            }

            @Override
            public void onFailure(Call<NotificationModel> call, Throwable t) {
                progressDialog.dismiss();
                hideView();
                AppUtil.showAlert(context.getString(R.string.serverError), context);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (notificationListData.size() > 0) {
            if (notificationListData.get(position).getType().equals("1")) {
                notificationTitle.setText(notificationListData.get(position).getTitle());
                showDetail();
                notificationMessage.setText(notificationListData.get(position).getMeassage());
                notificationDate.setText(notificationListData.get(position).getDate());
                notificationTime.setText(notificationListData.get(position).getTime());
                String ids = notificationListData.get(position).getNotificationId();
                String type = notificationListData.get(position).getType();
                String isRead = notificationListData.get(position).getIsRead();
                if (isRead.equals("2")) {
                    notificationListData.get(position).setIsRead("1");
                    readNotification(ids, type);
                }
            } else {
                readNotification(notificationListData.get(position).getNotificationId(), notificationListData.get(position).getType());
                notificationListData.get(position).setIsRead("1");
                startActivity(new Intent(context, UpdateAppActivity.class));
                notificationAdapter.notifyDataSetChanged();
            }
        }
    }

    private void readNotification(String ids, String type) {
        readNotification = apiInterface.readNotification(mobileNumber, deviceType, deviceToken, userId, ids, type);
        readNotification.enqueue(new Callback<NotificationRead>() {
            @Override
            public void onResponse(Call<NotificationRead> call, Response<NotificationRead> response) {

                if (response != null) {
                    NotificationRead notificationRead = response.body();
                    if (notificationRead != null) {
                        String error, msg;
                        error = notificationRead.getError();
                        msg = notificationRead.getMessage();
                        if (error.equals("0")) {

                        } else {
                            if (msg.equalsIgnoreCase("Your session has been expired")) {
                                AppUtil.sessionLogout(msg, getContext());
                            } else if (msg.equalsIgnoreCase("Your account has been deactivated by administrator"))
                                AppUtil.sessionLogout(msg, getContext());
                        }
                    } else {
                        hideView();
                        AppUtil.showAlert(context.getString(R.string.serverError), context);
                    }
                } else {
                    AppUtil.showAlert(context.getString(R.string.serverError), context);
                    hideView();
                }
            }

            @Override
            public void onFailure(Call<NotificationRead> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onBackPressed() {

        if (isDetailView) {
            showList();
            if (notificationAdapter != null)
                notificationAdapter.notifyDataSetChanged();
        } else return !isNoFoundView && !isListView;
        return true;
    }

    @Override
    public int getBackPriority() {
        return HIGH_BACK_PRIORITY;
    }

    private void hideView() {
        isDetailView = false;
        isListView = false;
        isNoFoundView = false;
        notificationList.setVisibility(View.GONE);
        notificationDetailView.setVisibility(View.GONE);
        noNotificationFound.setVisibility(View.GONE);
    }

    private void showList() {

        if (CheckNetwork.isNetwordAvailable(context)) {
            if (NotificationActivity.title != null) {
                getNotificationList();
                NotificationActivity.title=null;
            }
        } else
            AppUtil.showAlert(context.getString(R.string.networkError), context);

        isListView = true;
        isDetailView = false;
        isNoFoundView = false;
        notificationList.setVisibility(View.VISIBLE);
        notificationDetailView.setVisibility(View.GONE);
        noNotificationFound.setVisibility(View.GONE);
        NotificationActivity.heading.setText("Notifications");
    }

    private void showDetail() {
        isListView = true;
        isDetailView = true;
        isNoFoundView = false;
        notificationList.setVisibility(View.GONE);
        notificationDetailView.setVisibility(View.VISIBLE);
        noNotificationFound.setVisibility(View.GONE);
        NotificationActivity.heading.setText("Notification Details");
    }

    private void showNoFound() {
        isListView = false;
        isDetailView = false;
        isNoFoundView = true;
        notificationList.setVisibility(View.GONE);
        notificationDetailView.setVisibility(View.GONE);
        noNotificationFound.setVisibility(View.VISIBLE);
        NotificationActivity.heading.setText("Notifications");
    }
}
