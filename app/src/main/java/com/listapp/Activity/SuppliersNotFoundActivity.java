package com.listapp.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.listapp.API_Utility.AsyncTask_Utility.CheckNetwork;
import com.listapp.API_Utility.AsyncTask_Utility.MultipartUtility;
import com.listapp.API_Utility.Ratrofit_Implementation.APIInterface;
import com.listapp.Fragment.CompanySearchFragment;
import com.listapp.Fragment.MedicineSearchFragment;
import com.listapp.Fragment.SupplierSearchFragment;
import com.listapp.ListAppUtil.AppUtil;
import com.listapp.ListAppUtil.MarshMallowPermission;
import com.listapp.ListAppUtil.OKayEvent;
import com.listapp.ListAppUtil.PreferenceConnector;
import com.listapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by syscraft on 7/24/2017.
 */

public class SuppliersNotFoundActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int FILE_REQUEST_CODE = 10;
    private static final int GALLERY_REQUEST_CODE = 11;
    private static final int CAMERA_REQUEST_CODE = 12;
    private TextView shopName;
    private TextView address;
    private TextView mobileNumber;
    private Spinner searchErrorSpinner;
    private LinearLayout spinnerError;
    private LinearLayout descriptionError;
    private LinearLayout fileNameView;
    private TextView fileNameText;
    private MarshMallowPermission marshMallowPermission;
    private ProgressDialog progressDialog;
    private String mobileNumber1, cityID, deviceType, deviceToken, userId, text;
    private TextView heading;
    private Uri fileUri;
    private String keyWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_item);
        try {
            text = getIntent().getStringExtra("text");
            keyWord = getIntent().getStringExtra("keyWord");
        } catch (Exception e) {
            e.printStackTrace();
        }
        shopName = findViewById(R.id.shopName);
        address = findViewById(R.id.address);
        mobileNumber = findViewById(R.id.mobileNumber);
        searchErrorSpinner = findViewById(R.id.searchErrorSpinner);
        spinnerError = findViewById(R.id.spinnerError);
        spinnerError.setVisibility(View.INVISIBLE);
        descriptionError = findViewById(R.id.descriptionError);
        findViewById(R.id.upload).setOnClickListener(this);
        findViewById(R.id.upload2).setOnClickListener(this);
        findViewById(R.id.upload1).setOnClickListener(this);
        fileNameView = findViewById(R.id.fileNameView);
        fileNameText = findViewById(R.id.fileNameText);
        findViewById(R.id.submit).setOnClickListener(this);
        findViewById(R.id.toolbarBackButton).setOnClickListener(this);
        heading = findViewById(R.id.heading);
        heading.setText("SUPPLIER NOT FOUND");


        shopName.setText("I am ");
        shopName.append(AppUtil.getHalfBoldString(PreferenceConnector.readString(SuppliersNotFoundActivity.this, PreferenceConnector.SHOP_NAME, "")));
        address.setText(AppUtil.getHalfBoldString(PreferenceConnector.readString(SuppliersNotFoundActivity.this, PreferenceConnector.ADDRESS, "")));
        mobileNumber.setText("RMN ");
        mobileNumber.append(AppUtil.getHalfBoldString(PreferenceConnector.readString(SuppliersNotFoundActivity.this, PreferenceConnector.MOBILE_NUMBER, "")));

        marshMallowPermission = new MarshMallowPermission(SuppliersNotFoundActivity.this);
        mobileNumber1 = PreferenceConnector.readString(SuppliersNotFoundActivity.this, PreferenceConnector.MOBILE_NUMBER, "");
        userId = PreferenceConnector.readString(SuppliersNotFoundActivity.this, PreferenceConnector.USER_ID, "");
        deviceType = PreferenceConnector.readString(SuppliersNotFoundActivity.this, PreferenceConnector.DEVICE_TYPE, "Android");
        deviceToken = PreferenceConnector.readString(SuppliersNotFoundActivity.this, PreferenceConnector.DEVICE_TOKEN, "");
        cityID = PreferenceConnector.readString(SuppliersNotFoundActivity.this, PreferenceConnector.CITY_ID, "");

        ArrayList<String> spinnerErrorList = new ArrayList<>();
        spinnerErrorList.add("Not Finding Supplier");
        ArrayAdapter cityAdapter = new ArrayAdapter(SuppliersNotFoundActivity.this, android.R.layout.simple_spinner_item, spinnerErrorList);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchErrorSpinner.setAdapter(cityAdapter);

        getDescription().setText(text);
      //  NavigationView navigationView = findViewById(R.id.nav_view);
      //  View headerView = navigationView.getHeaderView(0);

      //  ImageView shareApp =headerView.findViewById(R.id.shareapp);
      //  shareApp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent sendIntent = new Intent();
//                sendIntent.setAction(Intent.ACTION_SEND);
//                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi,\n" +
//                        "Use ListApp for finding right Supplier & Ordering to procure medicines\n" +
//                        "Download - https://bit.ly/listapp1");
//                sendIntent.setType("text/plain");
//
//                Intent shareIntent = Intent.createChooser(sendIntent, null);
//
//                startActivity(shareIntent);
//            }
//        });
    }

    private EditText getDescription() {
        return (EditText) findViewById(R.id.description);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.upload:
                uploadEvent();
                break;

            case R.id.toolbarBackButton:
                if(keyWord.contains("cine")){
                    Intent i = new Intent(this,MedicineSearchActivity.class);
                    startActivity(i);
                }
                else if(keyWord.contains("pany")){
                    Intent i = new Intent(this,CompanySearchActivity.class);
                    startActivity(i);
                }else{
                        Intent i = new Intent(this,SupplierSearchActivity.class);
                        startActivity(i);

                }
                finish();
                break;

            case R.id.upload1:
                uploadEvent();
                break;

            case R.id.upload2:
                uploadEvent();
                break;

            case R.id.submit:
                AppUtil.hideKeyBoard(SuppliersNotFoundActivity.this);
                submitEvent();
                break;
        }
    }

    private void uploadEvent() {
        marshMallowPermission = new MarshMallowPermission(SuppliersNotFoundActivity.this);
        if (marshMallowPermission.checkPermissionForStorage()) {
            final CharSequence[] options = {"Take Photo", "Upload Photo", "Upload File",
                    "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(SuppliersNotFoundActivity.this);
            builder.setTitle("Select Option");
            builder.setItems(options, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Upload Photo")) {
                        callGallery();
                    } else if (options[item].equals("Take Photo")) {
                        callCamera();
                    } else if (options[item].equals("Upload File")) {
                        callSdCard();
                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        } else
            marshMallowPermission.requestPermissionForStorage();
    }

    public void callCamera() {
        try {
            Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            fileUri = AppUtil.getOutputMediaFile(1, SuppliersNotFoundActivity.this);
            intent1.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent1, CAMERA_REQUEST_CODE);
        } catch (Exception e) {
            AppUtil.showAlert("Something went wrong", SuppliersNotFoundActivity.this);
        }
    }

    private void callSdCard() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        try {
            startActivityForResult(intent, FILE_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callGallery() {

        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        try {
            startActivityForResult(intent, GALLERY_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String picturePath, filePath;
    private boolean isImage;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case CAMERA_REQUEST_CODE:

                    File newfile = new File(Environment.getExternalStorageDirectory(), getString(R.string.app_name) + " Images/" + AppUtil.fileName);
                    if (newfile.exists()) {
                        picturePath = newfile.getAbsolutePath();
                        fileNameView.setVisibility(View.VISIBLE);
                        fileNameText.setText(newfile.getName());
                    }
                    break;

                case GALLERY_REQUEST_CODE:
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(
                            selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    picturePath = c.getString(columnIndex);
                    fileNameView.setVisibility(View.VISIBLE);
                    fileNameText.setText(new File(picturePath).getName());
                    isImage = true;
                    c.close();
                    break;

                case FILE_REQUEST_CODE:
                    picturePath = data.getData().getPath();
                    if (picturePath.endsWith(".txt") || picturePath.endsWith(".doc") || picturePath.endsWith(".docx") || picturePath.endsWith(".xls") || picturePath.endsWith(".xlsx") || picturePath.endsWith(".pdf")) {
                        fileNameView.setVisibility(View.VISIBLE);
                        fileNameText.setText(new File(picturePath).getName());
                    } else {
                        AppUtil.showAlert("Invalid file format. Please upload valid format like .txt, .doc, .docx, .xls, .xlsx, .pdf", SuppliersNotFoundActivity.this);
                        fileNameView.setVisibility(View.INVISIBLE);
                        fileNameText.setText("");
                        picturePath = null;
                    }
                    break;
            }

        } else {
            fileNameView.setVisibility(View.INVISIBLE);
            fileNameText.setText("");
            picturePath = null;
        }
    }

    private void submitEvent() {

        final String query = "Not Finding Supplier";
        final String description = getDescription().getText().toString().trim();

        if (!query.equals("")) {
            spinnerError.setVisibility(View.INVISIBLE);
            if (!description.equals("")) {
                descriptionError.setVisibility(View.INVISIBLE);

                if (CheckNetwork.isNetwordAvailable(SuppliersNotFoundActivity.this)) {
                    if (progressDialog == null) {
                        progressDialog = AppUtil.createProgressDialog(SuppliersNotFoundActivity.this);
                        if (!progressDialog.isShowing())
                            progressDialog.show();
                    } else {
                        if (!progressDialog.isShowing())
                            progressDialog.show();
                    }
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            demoUpload(query, description);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            progressDialog.dismiss();
                            try {
                                if (error.equals("0")) {

                                    try {
                                        SupplierSearchFragment.afterSubmitNotForm = 1;
                                        MedicineSearchFragment.afterSubmitNotForm = 1;
                                        CompanySearchFragment.afterSubmitNotForm = 1;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    if (picturePath != null)
                                        AppUtil.okayEventDialog("Your Supplier request has been sent successfully", SuppliersNotFoundActivity.this, new OKayEvent() {
                                            @Override
                                            public void okayEvent(boolean b) {
                                           finish();


                                            }
                                        });
                                    else
                                        AppUtil.okayEventDialog("Your Supplier request has been sent successfully", SuppliersNotFoundActivity.this, new OKayEvent() {
                                            @Override
                                            public void okayEvent(boolean b) {
                                                if(keyWord.contains("cine")){
                                                    Intent i = new Intent(getBaseContext(),MedicineSearchActivity.class);
                                                    startActivity(i);
                                                }
                                                else if(keyWord.contains("pany")){
                                                    Intent i = new Intent(getBaseContext(),CompanySearchActivity.class);
                                                    startActivity(i);
                                                }else{
                                                    Intent i = new Intent(getBaseContext(),SupplierSearchActivity.class);
                                                    startActivity(i);

                                                }
                                                finish();
                                                finish();
                                            }
                                        });
                                } else {
                                    if (msg.equalsIgnoreCase("Your session has been expired")) {
                                        AppUtil.sessionLogout(msg, SuppliersNotFoundActivity.this);
                                    } else if (msg.equalsIgnoreCase("Your account has been deactivated by administrator"))
                                        AppUtil.sessionLogout(msg, SuppliersNotFoundActivity.this);
                                    else
                                        AppUtil.showAlert(msg, SuppliersNotFoundActivity.this);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.execute();
                } else
                    AppUtil.showAlert(getString(R.string.networkError), SuppliersNotFoundActivity.this);
            } else
                descriptionError.setVisibility(View.VISIBLE);
        } else
            spinnerError.setVisibility(View.VISIBLE);

    }

    String error, msg;

    private void demoUpload(String query, String description) {
        File file = null;
        if (picturePath != null)
            file = new File(picturePath);
        String response = null;
        try {
            MultipartUtility multipart = new MultipartUtility(APIInterface.SUPPLIER_NOT_FOUND, "UTF-8");
            multipart.addFormField("user_id", userId);
            multipart.addFormField("mobile_no", mobileNumber1);
            multipart.addFormField("device_type", deviceType);
            multipart.addFormField("device_token", deviceToken);
            multipart.addFormField("issue", query);
            multipart.addFormField("description", description);
            multipart.addFormField("city", PreferenceConnector.readString(getApplicationContext(),PreferenceConnector.CITY,""));
            multipart.addFormField("keyword", keyWord + " " + text);
            if (file != null && file.isFile() && file.exists())
                multipart.addFilePart("file_name", file);
            response = multipart.finish();
            try {
                JSONObject jsonObject = new JSONObject(response);
                error = jsonObject.optString("error");
                msg = jsonObject.optString("message");
            } catch (JSONException e) {
                e.printStackTrace();
                AppUtil.showAlert(getString(R.string.serverError), SuppliersNotFoundActivity.this);
            }
        } catch (IOException e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }
}
