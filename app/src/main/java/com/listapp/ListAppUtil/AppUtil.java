package com.listapp.ListAppUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.util.Linkify;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.room.Room;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.listapp.API_Utility.Ratrofit_Implementation.APIClient;
import com.listapp.API_Utility.Ratrofit_Implementation.APIInterface;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.City.Cities;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.Search_Product.ProductDetails;
import com.listapp.Activity.NewSignInActivity;
import com.listapp.Activity.OrderRegisterActivity;
import com.listapp.Fragment.OrderSearchFragment;
import com.listapp.Fragment.OrderlistFragment;
import com.listapp.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nivesh on 14-Mar-17.
 */

public class AppUtil {

    public static void showAlert(String msg, final Context context) {
        final SpannableString s = new SpannableString(msg); // msg should have url to enable clicking
        Linkify.addLinks(s, Linkify.ALL);

        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        builder.setCancelable(false);
        builder.setTitle("ListApp").
                setMessage(s).
                setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        ((TextView) Objects.requireNonNull(dialog.findViewById(android.R.id.message))).setMovementMethod(LinkMovementMethod.getInstance());



    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static String UppercaseFirstLetters(String str) {
        boolean prevWasWhiteSp = true;
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (Character.isLetter(chars[i])) {
                if (prevWasWhiteSp) {
                    chars[i] = Character.toUpperCase(chars[i]);
                }
                prevWasWhiteSp = false;
            } else {
                prevWasWhiteSp = Character.isWhitespace(chars[i]);
            }
        }
        return new String(chars);
    }

    public static ProgressDialog createProgressDialog(Context mContext) {
        ProgressDialog dialog = new ProgressDialog(mContext, R.style.MyRequestDialog);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {
        }
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.transparentprogresdialog);
        return dialog;
    }

    public static void hideKeyBoard(Context context) {
        View viewq = ((Activity) context).getCurrentFocus();
        if (viewq != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(viewq.getWindowToken(), 0);
        }
    }

    public static void hideKeyBoardWithAllView(Context context, MotionEvent event) {
        View v = ((Activity) context).getCurrentFocus();

        if (v instanceof EditText) {
            View w = ((Activity) context).getCurrentFocus();
            int[] scrcoords = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom())) {

                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(((Activity) context).getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    public static SpannableString getHalfBoldString(String bold) {
        SpannableString ss1 = new SpannableString(bold);
        ss1.setSpan(new StyleSpan(Typeface.BOLD), 0, ss1.length(), 0);
        return ss1;
    }

    public static void userLogout(final Context context) {

        clearNotification(context);
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_logout);
        Button yes, no;
        yes = dialog.findViewById(R.id.yes);
        no = dialog.findViewById(R.id.no);
        no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
                PreferenceConnector.clear(context);
                Intent intent = new Intent(context, NewSignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
                ((Activity) context).finish();
                ((Activity) context).finishAffinity();
            }
        });

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public static void sessionLogout(String msg, final Context context) {

        PreferenceConnector.clear(context);
        clearNotification(context);

        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        builder.setCancelable(false);
        builder.setTitle("ListApp").
                setMessage(msg).
                setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(context, NewSignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);

                        ((Activity) context).finish();
                        ((Activity) context).finishAffinity();

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public static void showExitAlert(String msg, final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        builder.setCancelable(false);
        builder.setTitle("ListApp").
                setMessage(msg).
                setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((Activity) context).finish();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    public static void showKeyboard(Context context) {
        ((InputMethodManager) (context).getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static SpannableStringBuilder getStarHint(String simple) {
        String colored = " *";
        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(simple);
        int start = builder.length();
        builder.append(colored);
        int end = builder.length();

        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    public static void yesNoDialog(final Context context, String msg, final YesNoInteface yesNoInteface) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context, R.style.MyDialogTheme);
        builder.setTitle("ListApp");
        builder.setCancelable(false);
        builder.setMessage(msg);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                yesNoInteface.isNoYes(true);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                yesNoInteface.isNoYes(false);
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static void okayEventDialog(String s, Context context, final OKayEvent oKayEvent) {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(context, R.style.MyDialogTheme);
        builder.setTitle("ListApp");
        builder.setCancelable(false);
        builder.setMessage(s);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                oKayEvent.okayEvent(true);
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static String fileName;

    @SuppressLint("SimpleDateFormat")
    public static Uri getOutputMediaFile(int type, Context context) {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), context.getString(R.string.app_name));

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File mediaFile;
        if (type == 1) {
            String imageStoragePath = mediaStorageDir + " Images/";

            createDirectory(imageStoragePath);
            mediaFile = new File(imageStoragePath + "IMG" + timeStamp + ".jpg");
        } else {
            return null;
        }
        fileName = mediaFile.getName();
        return  FileProvider.getUriForFile(
                context,
                context.getApplicationContext()
                        .getPackageName() + ".provider", mediaFile);
    }

    public static void createDirectory(String filePath) {
        if (!new File(filePath).exists()) {
            new File(filePath).mkdirs();
        }
    }

    public static void supplierListSort(List<ProductDetails.Data.Supplier> supplierList) {
        Collections.sort(supplierList, new Comparator<ProductDetails.Data.Supplier>() {
            @Override
            public int compare(ProductDetails.Data.Supplier o1, ProductDetails.Data.Supplier o2) {
                return o1.getShopName().compareToIgnoreCase(o2.getShopName());
            }
        });
    }

    public static void shareOnWhatsApp(final String shareText, final Context context) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_share);
        Button cancel;
        cancel = dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        LinearLayout shareWhatsapp = dialog.findViewById(R.id.shareWhatsApp);
        shareWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.setPackage("com.whatsapp");
                    intent.putExtra(Intent.EXTRA_TEXT, shareText);
                    context.startActivity(intent);
                } catch (Exception e) {
                    AppUtil.showAlert("WhatsApp is not installed on your device for sharing", context);
                    e.printStackTrace();
                }
            }
        });

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public static void addproduct(final Context context, final String productName, final String supplierName, final String companyName, String city, final String type, final OrderSearchFragment orderSearchFragment, final String newProduct, final String supplierId) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_addproduct);
        TextView productname=dialog.findViewById(R.id.productname);
        productname.setText(productName);
        orderSearchFragment.productName=productName;
        EditText qtyedit=dialog.findViewById(R.id.orderqty);
        qtyedit.setText(orderSearchFragment.qty);
        EditText schedit=dialog.findViewById(R.id.scheme);
        schedit.setText(orderSearchFragment.sch);
        TextView companyname=dialog.findViewById(R.id.companyname);
        companyname.setText(companyName);
        orderSearchFragment.companyName=companyName;
        TextView t12= dialog.findViewById(R.id.textView12);
        final OrderDB db = Room.databaseBuilder(context.getApplicationContext(),
                OrderDB.class, "Order").allowMainThreadQueries().build();
        final OrderDao od= db.orderDao();
        t12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,OrderRegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(intent);
            }
        });
        ImageView cancel;
        cancel = dialog.findViewById(R.id.close);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        Button searchsupplier=dialog.findViewById(R.id.searchsupplier);
        if(supplierName!="" && supplierName != null )
        searchsupplier.setText(supplierName);
        searchsupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText qtyedit=dialog.findViewById(R.id.orderqty);
                EditText schedit=dialog.findViewById(R.id.scheme);
                orderSearchFragment.qty= qtyedit.getText().toString();
                orderSearchFragment.sch = schedit.getText().toString();
                orderSearchFragment.productName=productName;
                orderSearchFragment.companyName=companyName;
                if(orderSearchFragment.newproducttapped)orderSearchFragment.showInAppProductDetail();
                else
                orderSearchFragment.showProductDetail();
                dialog.hide();
            }
        });

        Button addtolist=dialog.findViewById(R.id.addtolist);
        if(city==""){
            city=PreferenceConnector.readString(context,PreferenceConnector.CITY,"");
        }

        final String finalCity = city;
        if(newProduct=="true"){
            orderSearchFragment.addingProduct=true;

        }
        addtolist.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("InvalidAnalyticsName")
            @Override
            public void onClick(View v) {

                orderSearchFragment.addingProduct=false;
                orderSearchFragment.qty= "";
                orderSearchFragment.sch = "";


                FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                Bundle bundle = new Bundle();
                String firetxt=productName;
                bundle.putString("Product Name",firetxt);
                mFirebaseAnalytics.logEvent("AddProductEvent",bundle);

                Bundle bundle2 = new Bundle();
                String firetxt2=supplierName;
                bundle2.putString("Supplier Name",firetxt2);
                mFirebaseAnalytics.logEvent("AddSupplierEvent",bundle2);


                EditText qtyedit=dialog.findViewById(R.id.orderqty);
                String qty="1";
                if(qtyedit.getText().toString().isEmpty()||qtyedit.getText().toString().equals("0")){
                    Toast.makeText(context, "Quantity cannot be nil or zero", Toast.LENGTH_SHORT).show();
                }else {
                    qty = qtyedit.getText().toString();
                    EditText schedit = dialog.findViewById(R.id.scheme);
                    String sch = schedit.getText().toString();

                    Order order = new Order();
                    order.setProduct(productName);
                    order.setQuantity(qty);
                    order.setSupplier(supplierName);
                    order.setCompany(companyName);
                    order.setScheme(sch);
                    order.setUser(PreferenceConnector.readString(context, PreferenceConnector.USER_ID, ""));
                    order.setCity(finalCity);
                    order.setType(type);
                    order.setSupplierid(supplierId);
                    if (newProduct == "true")
                        order.setNewproduct(newProduct);

                    final Order finalorder = order;
                    try {
                        od.addOrder(order);
                        Toast.makeText(context, "Your order is successfully added", Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    } catch (Exception e) {


                        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

                        Call<Cities> getcityid = apiInterface.getSupplierID(supplierName.trim());
                        getcityid.enqueue(new Callback() {

                            @Override
                            public void onResponse(Call call, Response response) {

                                if (response != null) {
                                    Cities cities = (Cities) response.body();
                                    final String cityId = cities.getmData().get(0);
                                    YesNoInteface yesnointerface = new YesNoInteface() {
                                        @Override
                                        public void isNoYes(boolean b) {
                                            if (b) {
                                                EditText qtyedit = dialog.findViewById(R.id.orderqty);
                                                String qty = "1";
                                                qty = qtyedit.getText().toString();
                                                EditText schedit = dialog.findViewById(R.id.scheme);
                                                String sch = schedit.getText().toString();
                                                if (qtyedit.getText().toString().isEmpty() || qtyedit.getText().toString().equals("0")) {
                                                    Toast.makeText(context, "Quantity cannot be nil or zero", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Order o = od.getOrderbyproduct(productName);
                                                    o.setQuantity(qty);
                                                    o.setSupplier(supplierName);
                                                    o.setCompany(companyName);
                                                    o.setScheme(sch);
                                                    o.setUser(PreferenceConnector.readString(context, PreferenceConnector.USER_ID, ""));
                                                    o.setCity(finalCity);
                                                    o.setType(type);
                                                    o.setSupplierid(supplierId);
                                                    od.update(o);
                                                    Toast.makeText(context, "Your order is successfully edited", Toast.LENGTH_LONG).show();
                                                    if (orderSearchFragment.editing)
                                                        context.startActivity(new Intent(context, OrderRegisterActivity.class));
                                                    dialog.cancel();
                                                }
                                            } else {
                                                Toast.makeText(context, "Your order is not edited", Toast.LENGTH_LONG).show();
                                                dialog.cancel();
                                            }
                                        }
                                    };
                                    yesNoDialog(context, "This product is already being ordered from another supplier,\n Do you want to edit it?", yesnointerface);
                                    dialog.cancel();


                                }
                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {

                            }
                        });
                    }
                }}});


        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public static void addnewproduct(final Context context, final OrderSearchFragment orderSearchFragment, String text) {

        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_addnewproduct);
        ImageView cancel;
        cancel = dialog.findViewById(R.id.close);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        ImageView next=dialog.findViewById(R.id.next);
        final EditText pn=dialog.findViewById(R.id.productname);
        pn.setText(text);
        Button sn=dialog.findViewById(R.id.suppliername);
        final EditText cn=dialog.findViewById(R.id.companyname);
        if(!orderSearchFragment.publicsupplierName.isEmpty())
           sn.setText(orderSearchFragment.publicsupplierName);
        sn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderSearchFragment.addingProduct=true;
                orderSearchFragment.productName=pn.getText().toString();
                orderSearchFragment.companyName=cn.getText().toString();
                orderSearchFragment.newproduct=true;
                orderSearchFragment.showInAppProductDetail();
                dialog.cancel();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("InvalidAnalyticsName")
            @Override
            public void onClick(View v) {
                orderSearchFragment.addingProduct=true;
                EditText pn=dialog.findViewById(R.id.productname);
                EditText cn=dialog.findViewById(R.id.companyname);
                Button sn=dialog.findViewById(R.id.suppliername);
                EditText sid=dialog.findViewById(R.id.whano);
                String productName = pn.getText().toString();
                FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                Bundle bundle = new Bundle();
                String firetxt=productName;
                bundle.putString("New Product Added",firetxt);
                mFirebaseAnalytics.logEvent("New_Product_Count",bundle);
                String supplierName = sn.getText().toString();
                String companyName = cn.getText().toString();
                String supplierId =sid.getText().toString();
                String newProduct ="true";
                orderSearchFragment.publicsupplierName=supplierName;
                orderSearchFragment.productName=productName;
                orderSearchFragment.companyName= companyName;

                if(productName.isEmpty()){
                    Toast.makeText(context, "Product name is required", Toast.LENGTH_SHORT).show();
                }else{
                    AppUtil.addproduct(context, productName,supplierName, companyName, "", "", orderSearchFragment,newProduct, supplierId);
                    orderSearchFragment.addingProduct=true;
                    orderSearchFragment.newproduct=false;
                    dialog.cancel();
                }
            }
        });
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public static void addnewsupplier(final Context context, final String product, final String company, final String type , final OrderSearchFragment orderSearchFragment) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_addnewsupplier);
        ImageView cancel;
        cancel = dialog.findViewById(R.id.close);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        ImageView next=dialog.findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("InvalidAnalyticsName")
            @Override
            public void onClick(View v) {
                EditText supplier = dialog.findViewById(R.id.firmname);
                EditText city = dialog.findViewById(R.id.city);
                orderSearchFragment.publicsupplierName=supplier.getText().toString();
                FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                Bundle bundle = new Bundle();
                String firetxt=supplier.getText().toString();
                bundle.putString("New Supplier Added",firetxt);
                mFirebaseAnalytics.logEvent("New_Supplier_Count",bundle);


                orderSearchFragment.hideProductDetail();
                EditText whatsapp=dialog.findViewById(R.id.whatsappnumber);
                String supplierId= whatsapp.getText().toString();
                if(supplier.getText().toString().isEmpty()||city.getText().toString().isEmpty()){
                    Toast.makeText(context, "Firm Name and City name are required", Toast.LENGTH_SHORT).show();
                }else{
                AppUtil.addproduct(context, product, supplier.getText().toString(), company, city.getText().toString(), type, orderSearchFragment,"", supplierId);
                dialog.cancel();}
            }
        });
        if(!dialog.isShowing())

            {
                dialog.show();
            }
        }

    public static void editproduct(final Context context, final String productName, final OrderlistFragment orderlistFragment) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_editproduct);
        TextView productname=dialog.findViewById(R.id.productname);
        productname.setText(productName);
        OrderDB db = Room.databaseBuilder(context.getApplicationContext(),
                OrderDB.class, "Order").allowMainThreadQueries().build();
        final OrderDao od= db.orderDao();
        final Order o= od.getOrderbyproduct(productName);

        TextView companyname=dialog.findViewById(R.id.companyname);
        companyname.setText(o.getCompany());

        TextView t12= dialog.findViewById(R.id.textView12);
        t12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,OrderRegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(intent);
            }
        });
        ImageView cancel;
        cancel = dialog.findViewById(R.id.close);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                orderlistFragment.notesedittext.setVisibility(View.VISIBLE);
                dialog.cancel();
            }
        });
        final Button searchsupplier=dialog.findViewById(R.id.searchsupplier);
        searchsupplier.setText(o.getSupplier());
        final EditText qtyedit=dialog.findViewById(R.id.orderqty);
        final EditText schedit=dialog.findViewById(R.id.scheme);


        searchsupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderSearchFragment osf =new OrderSearchFragment();
                osf.companyName=o.getCompany();
                osf.productName=o.getProduct();
                osf.editing=true;
                osf.qty=qtyedit.getText().toString();
                osf.sch=schedit.getText().toString();
                osf.addingProduct=true;
                orderlistFragment.hideFragmentView();
                orderlistFragment.getFragmentManager().beginTransaction().replace(R.id.fragmentView,osf,"OrderRegisterFragment").addToBackStack("OrderRegisterFragment").commit();

               // OrderRegisterActivity.search.setText(productName);

                dialog.cancel();
            }
        });
        String qty=o.getQuantity();
        qtyedit.setText(qty);

        String sch=o.getScheme();
        schedit.setText(sch);
        Button addtolist=dialog.findViewById(R.id.addtolist);
        addtolist.setText("Edit Order");
       String city=o.getCity();
        if(city==""){
            city=PreferenceConnector.readString(context,PreferenceConnector.CITY,"");
        }
        final String finalCity=city;
        final Order finalorder=o;

        addtolist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                orderlistFragment.notesedittext.setVisibility(View.VISIBLE);
                if (qtyedit.getText().toString().isEmpty() || qtyedit.getText().toString().equals("0")) {
                    Toast.makeText(context, "Quantity cannot be nill or 0", Toast.LENGTH_SHORT).show();
                } else {
                    final String finalQty = qtyedit.getText().toString();
                    final String supplierName = searchsupplier.getText().toString();
                    final String finalSch = schedit.getText().toString();
                    finalorder.setProduct(productName);
                    finalorder.setQuantity(finalQty);
                    finalorder.setSupplier(supplierName);
                    finalorder.setScheme(finalSch);
                    finalorder.setCity(finalCity);

                    APIInterface apiInterface =  APIClient.getClient().create(APIInterface.class);

                    Call<Cities> getcityid = apiInterface.getSupplierID(supplierName.trim());
                    getcityid.enqueue(new Callback() {

                        @Override
                        public void onResponse(Call call, Response response) {

                            if (response != null) {
                            Cities supplier = (Cities) response.body();
                                   String supplierID = supplier.getmData().get(0);
                                    finalorder.setSupplierid(supplierID);
                                YesNoInteface yesnointerface = new YesNoInteface() {
                                    @Override
                                    public void isNoYes(boolean b) {
                                        if (b) {
                                            od.update(finalorder);
                                            context.startActivity(new Intent(context,OrderRegisterActivity.class));
                                            Toast.makeText(context, "Your order is successfully edited", Toast.LENGTH_LONG).show();
                                        } else
                                            Toast.makeText(context, "Your order is not edited", Toast.LENGTH_LONG).show();

                                    }
                                };
                                yesNoDialog(context, " Do you want to edit the order?", yesnointerface);
                                dialog.cancel();


                            }
                        }
                                    @Override
                                    public void onFailure(Call call, Throwable t) {

                                }});
                                  }
            }

        });

        if (!dialog.isShowing()) {
            dialog.show();
        }


    }



    private static void clearNotification(Context context)
    {
        try {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void editsupplier(final Context context, final List<Order> orders, final OrderlistFragment olf) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_addnewsupplier);
        ImageView cancel;
        cancel = dialog.findViewById(R.id.close);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        ImageView next=dialog.findViewById(R.id.next);
        final EditText supplier = dialog.findViewById(R.id.firmname);
        final EditText city = dialog.findViewById(R.id.city);
        supplier.setText(orders.get(0).getSupplier());
        city.setText(orders.get(0).getCity());
        EditText whatsapp=dialog.findViewById(R.id.whatsappnumber);
        whatsapp.setText(orders.get(0).getSupplierid());
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText whatsapp=dialog.findViewById(R.id.whatsappnumber);
                String supplierId= whatsapp.getText().toString();
                OrderDB db = Room.databaseBuilder(context.getApplicationContext(),
                        OrderDB.class, "Order").allowMainThreadQueries().build();
                final OrderDao od= db.orderDao();
                for(int i=0;i<orders.size();i++){
                    orders.get(i).setSupplierid(supplierId);
                    orders.get(i).setSupplier(supplier.getText().toString());
                    orders.get(i).setCity(city.getText().toString());
                    od.update(orders.get(i));
                }
                olf.t17.setText(supplier.getText().toString());
                Toast.makeText(context, "Contact updated", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        if(!dialog.isShowing())

        {
            dialog.show();
        }

    }
}
