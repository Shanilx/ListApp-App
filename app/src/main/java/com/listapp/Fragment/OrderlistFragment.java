package com.listapp.Fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.listapp.API_Utility.Ratrofit_Implementation.APIClient;
import com.listapp.API_Utility.Ratrofit_Implementation.APIInterface;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.SupplierDetails.SupplierProfile;
import com.listapp.Activity.OrderRegisterActivity;
import com.listapp.Adapter.OrderitemsBaseAdapter;
import com.listapp.Interface.FragmentBack;
import com.listapp.ListAppUtil.AppUtil;
import com.listapp.ListAppUtil.Order;
import com.listapp.ListAppUtil.OrderDB;
import com.listapp.ListAppUtil.OrderDao;
import com.listapp.ListAppUtil.PreferenceConnector;
import com.listapp.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderlistFragment extends Fragment implements FragmentBack {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    public ListView orderlv;
    Context context;
    private LinearLayout parentlayout;
    private FloatingActionButton addorder;
    public OrderSearchFragment osf;
    private OrderlistFragment olf;
    FrameLayout f;
    ListView productslv;
    TextView content;
    private APIInterface apiInterface;
    Button share,sms;
    public EditText notesedittext;
    private String phoneNo;
    private String message;
    public TextView t17;
    LinearLayout supplierheading;
    private String firmname;

    public OrderlistFragment(Context context) {
        // Required empty public constructor
    this.context=context;
    olf=this;
    }

    @SuppressLint("RestrictedApi")
    public void hideFragmentView() {
        f.setVisibility(View.VISIBLE);
        parentlayout.setVisibility(View.GONE);
      // content.setVisibility(View.GONE);
     //   addorder.setVisibility(View.INVISIBLE);
        productslv.setVisibility(View.GONE);
        t17.setVisibility(View.GONE);
        supplierheading.setVisibility(View.GONE);
        share.setVisibility(View.GONE);
        sms.setVisibility(View.GONE);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_orderlist, container, false);
        orderlv=v.findViewById(R.id.productslv);
        parentlayout=v.findViewById(R.id.parentlayout);
        addorder=v.findViewById(R.id.addorderbutton);
        share=v.findViewById(R.id.shareorder);
        sms=v.findViewById(R.id.sms);
        t17=v.findViewById(R.id.textView17);
        supplierheading=v.findViewById(R.id.supplierheading);
        OrderRegisterActivity.backButton.setVisibility(View.GONE);


        notesedittext=v.findViewById(R.id.notes);


        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addorder=view.findViewById(R.id.addorderbutton);
        f=view.findViewById(R.id.fragmentView);
        final LinearLayout parentlayout = view.findViewById(R.id.parentlayout);
        content = view.findViewById(R.id.notes);
        content.setVisibility(View.GONE);
        osf= new OrderSearchFragment();
        ListView productslv=view.findViewById(R.id.productslv);
        productslv.setVisibility(View.GONE);
        share.setVisibility(View.GONE);
        sms.setVisibility(View.GONE);
        t17.setVisibility(View.GONE);
        supplierheading.setVisibility(View.GONE);

        addorder.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
               f.setVisibility(View.VISIBLE);
               parentlayout.setVisibility(View.GONE);
               content.setVisibility(View.GONE);
               addorder.setVisibility(View.INVISIBLE);
               getFragmentManager().beginTransaction().replace(R.id.fragmentView,osf,"OrderRegisterFragment").addToBackStack("OrderRegisterFragment").commit();
            }
        });
        setupviews(view,content);
        OrderRegisterActivity.backButton.setVisibility(View.GONE);



    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onBackPressed() {
        OrderRegisterActivity.backButton.setVisibility(View.GONE);
        if(orderlv.getVisibility()==View.VISIBLE){
            context.startActivity(new Intent(context,OrderRegisterActivity.class));
        }
        return false;
    }

    @Override
    public int getBackPriority() {
        return 0;
    }

    private void setupviews(final View view, final TextView content) {
        OrderRegisterActivity.backButton.setVisibility(View.VISIBLE);
        TextView forname= view.findViewById(R.id.forname);
        firmname= PreferenceConnector.readString(context,PreferenceConnector.SHOP_NAME,"");
        if(firmname == null || firmname ==""){
            firmname=PreferenceConnector.readString(context,PreferenceConnector.FULL_NAME,"");
        }

        forname.setText("For : "+firmname);
        String text="";
        OrderDB db = Room.databaseBuilder(context.getApplicationContext(),
                OrderDB.class, "Order").allowMainThreadQueries().build();
        OrderDao od= db.orderDao();
        List<Order> orders= od.getSupplierlist();
        for(int i=0;i<orders.size();i++){
            LayoutInflater inflater = (LayoutInflater) context.getApplicationContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout container = view.findViewById(R.id.ordersparentview);
            View child = inflater.inflate(R.layout.row_orderdetail, container,false);
            ImageView i1=child.findViewById(R.id.imageView11);
            ImageView i2=child.findViewById(R.id.imageView12);
            ImageView i3=child.findViewById(R.id.imageView13);
            ImageView i4=child.findViewById(R.id.imageView14);
            TextView suppliername=child.findViewById(R.id.suppliername);
            i1.setImageResource(R.drawable.supplier_icon);
            i2.setImageResource(R.drawable.plus);
            i3.setImageResource(R.drawable.whatsapp);
            i4.setImageResource(R.drawable.externallink);
            suppliername.setText(orders.get(i).getSupplier());
            container.addView(child);
            child.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onClick(View v) {
                    OrderRegisterActivity.backButton.setVisibility(View.VISIBLE);
                    final FloatingActionButton addorder = view.findViewById(R.id.addorderbutton);
                    final FrameLayout f = view.findViewById(R.id.fragmentView);
                    final LinearLayout parentlayout = view.findViewById(R.id.parentlayout);
                    final TextView content = view.findViewById(R.id.notes);
                    final TextView s = v.findViewById(R.id.suppliername);
                    parentlayout.setVisibility(View.GONE);
                    content.setVisibility(View.VISIBLE);
                    addorder.setVisibility(View.INVISIBLE);
                    share.setVisibility(View.VISIBLE);
                    sms.setVisibility(View.VISIBLE);
                    productslv = view.findViewById(R.id.productslv);
                    OrderDB db = Room.databaseBuilder(context.getApplicationContext(),
                            OrderDB.class, "Order").allowMainThreadQueries().build();
                    OrderDao od = db.orderDao();
                    t17.setText(s.getText());
                    String str = s.getText().toString();

                    OrderRegisterActivity.heading.setText(firmname);
                    final List<Order> orders = od.getOrderlist(str);
                    OrderitemsBaseAdapter orderitemsBaseAdapter = new OrderitemsBaseAdapter(context, orders, olf);
                    productslv.setAdapter(orderitemsBaseAdapter);

                    productslv.setVisibility(View.VISIBLE);
                    t17.setVisibility(View.VISIBLE);
                    supplierheading.setVisibility(View.VISIBLE);
                    final EditText notes = view.findViewById(R.id.notes);
                    notes.requestFocus();
                    TextView addcontact = view.findViewById(R.id.addcontact);
                    addcontact.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AppUtil.editsupplier(context, orders,olf);
                        }
                    });


                    share.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("InvalidAnalyticsName")
                        @Override
                        public void onClick(View v) {
                            OrderDB db = Room.databaseBuilder(context.getApplicationContext(),
                                    OrderDB.class, "Order").allowMainThreadQueries().build();
                            final OrderDao od = db.orderDao();
                            String str = s.getText().toString();
                            final List<Order> orders = od.getOrderlist(str);

                            FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                            Bundle bundle = new Bundle();
                            bundle.putString("count", "count");
                            mFirebaseAnalytics.logEvent("Whatsapp_Share_count", bundle);


                            final String[] tmpsupplierID = new String[1];
                            String supplierID = orders.get(0).getSupplierid();
                            if (supplierID.length() < 9 || supplierID == null) {
                                String mobileNumber1 = PreferenceConnector.readString(context, PreferenceConnector.MOBILE_NUMBER, "");
                                String userId = PreferenceConnector.readString(context, PreferenceConnector.USER_ID, "");
                                String deviceType = PreferenceConnector.readString(context, PreferenceConnector.DEVICE_TYPE, "Android");
                                String deviceToken = PreferenceConnector.readString(context, PreferenceConnector.DEVICE_TOKEN, "");
                                apiInterface = APIClient.getClient().create(APIInterface.class);

                                Call supplierProfileCall = apiInterface.getSupplierProfile(mobileNumber1, userId, deviceType, deviceToken, supplierID);


                                supplierProfileCall.enqueue(new Callback() {

                                    @Override
                                    public void onResponse(Call call, Response response) {
                                        if (response != null) {
                                            SupplierProfile supplierProfile = (SupplierProfile) response.body();
                                            if (supplierProfile != null) {

                                                String error = supplierProfile.getError();
                                                String msg = supplierProfile.getMessage();
                                                if (error.equals("0")) {
                                                    tmpsupplierID[0] = supplierProfile.getData().getSupplier().get(0).getContactPerson().get(0).getContactNumber();
                                                    String order = "For : ";

                                                    order += firmname + ", " +
                                                            PreferenceConnector.readString(context, PreferenceConnector.ADDRESS, "") + ", " +

                                                            "\n " + "--------------ORDER------------";
                                                    for (int i = 0; i < orders.size(); i++) {
                                                        order += "\n " + orders.get(i).getProduct() + "  " + orders.get(i).getQuantity();
                                                        if (!orders.get(i).getScheme().isEmpty())
                                                            order += "  +  " + orders.get(i).getScheme();

                                                    }
                                                    order += "\n---------------------------------";
                                                    order += "\n\nNotes : \n";
                                                    order += notes.getText().toString();

                                                    Intent sendIntent = new Intent();
                                                    sendIntent.setAction(Intent.ACTION_VIEW);
                                                    String url = null;
                                                    try {
                                                        url = "https://api.whatsapp.com/send?phone=91" + tmpsupplierID[0] + "&text=" + URLEncoder.encode(order, "UTF-8");

                                                    } catch (UnsupportedEncodingException e) {
                                                        e.printStackTrace();
                                                    }
                                                    sendIntent.setData(Uri.parse(url));
                                                    context.startActivity(sendIntent);
                                                    for (int i = 0; i < orders.size(); i++) {
                                                        orders.get(i).setSupplierid(tmpsupplierID[0]);
                                                        orders.get(i).setIsordered("true");
                                                        od.update(orders.get(i));
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call call, Throwable t) {
                                        AppUtil.showAlert(context.getString(R.string.serverError), context);
                                    }
                                });

                            } else {
                                String order = "For : ";

                                order += firmname + ", " +
                                        PreferenceConnector.readString(context, PreferenceConnector.ADDRESS, "") + ", " +
                                        " \n" +
                                        "\n " + "--------------ORDER------------";
                                for (int i = 0; i < orders.size(); i++) {
                                    order += "\n " + orders.get(i).getProduct() + "  " + orders.get(i).getQuantity();
                                    if (!orders.get(i).getScheme().isEmpty())
                                        order += "  +  " + orders.get(i).getScheme();

                                }
                                order += "\n---------------------------------";
                                order += "\n\nNotes : \n";
                                order += notes.getText().toString();

                                Intent sendIntent = new Intent();
                                sendIntent.setAction(Intent.ACTION_VIEW);
                                String url = null;
                                try {
                                    url = "https://api.whatsapp.com/send?phone=91" + supplierID + "&text=" + URLEncoder.encode(order, "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                sendIntent.setData(Uri.parse(url));
                                context.startActivity(sendIntent);
                                for (int i = 0; i < orders.size(); i++) {
                                    orders.get(i).setSupplierid(supplierID);
                                    orders.get(i).setIsordered("true");
                                    od.update(orders.get(i));
                                }
                            }
                        }
                    });

                    sms.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("InvalidAnalyticsName")
                        @Override
                        public void onClick(View v) {

                            FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                            Bundle bundle = new Bundle();
                            bundle.putString("count", "count");
                            mFirebaseAnalytics.logEvent("Share_count", bundle);

                            Bundle bundle2 = new Bundle();
                            bundle.putString("count", "count");
                            mFirebaseAnalytics.logEvent("Order_sent_count", bundle);


                            OrderDB db = Room.databaseBuilder(context.getApplicationContext(),
                                    OrderDB.class, "Order").allowMainThreadQueries().build();
                            final OrderDao od = db.orderDao();
                            String str = s.getText().toString();
                            final List<Order> orders = od.getOrderlist(str);
                            String order = "For : ";

                            order += firmname + ", " +
                                    PreferenceConnector.readString(context, PreferenceConnector.ADDRESS, "") + ", " +

                                    "\n " + "--------------ORDER------------";
                            for (int i = 0; i < orders.size(); i++) {

                                order += "\n " + orders.get(i).getProduct() + "  " + orders.get(i).getQuantity();
                                if (!orders.get(i).getScheme().isEmpty())
                                    order += "  +  " + orders.get(i).getScheme();
                            }
                            order += "\n---------------------------------";
                            order += "\n\nNotes : \n";
                            order += notes.getText().toString();
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, order);
                            sendIntent.setType("text/plain");

                            Intent shareIntent = Intent.createChooser(sendIntent, null);

                            startActivity(shareIntent);

                            for (int i = 0; i < orders.size(); i++) {
                                orders.get(i).setIsordered("true");
                                od.update(orders.get(i));
                            }
                        }
                    });
                }});}}

    public void deleteOrder(int position, String supplier) {
        OrderDB db = Room.databaseBuilder(context.getApplicationContext(),
                OrderDB.class, "Order").allowMainThreadQueries().build();
        final OrderDao od = db.orderDao();
        final List<Order> orders = od.getOrderlist(supplier);
        OrderitemsBaseAdapter orderitemsBaseAdapter = new OrderitemsBaseAdapter(context, orders, olf);
        productslv.setAdapter(orderitemsBaseAdapter);
        Toast.makeText(context,"Your order is successfully removed",Toast.LENGTH_LONG).show();
    }
}







