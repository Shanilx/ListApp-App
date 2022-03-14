package com.listapp.Adapter;

/**
 * Created by syscraft on 7/12/2017.
 */

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.listapp.API_Utility.Ratrofit_Implementation.Model.Search_Product.SearchProductResponse;
import com.listapp.Fragment.OrderSearchFragment;
import com.listapp.ListAppUtil.AppUtil;
import com.listapp.ListAppUtil.Order;
import com.listapp.ListAppUtil.PreferenceConnector;
import com.listapp.R;

import java.util.ArrayList;
import java.util.List;

public class OrderListBaseAdapter extends BaseAdapter {

    private List<SearchProductResponse.Datum> objects = new ArrayList<SearchProductResponse.Datum>();

    private Context context;
    private LayoutInflater layoutInflater;
    OrderSearchFragment osf;
    List<Order> orderlist;

    public OrderListBaseAdapter(Context context, List<Order> orderlist, List<SearchProductResponse.Datum> objects, OrderSearchFragment osf) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects = objects;
        this.osf=osf;
        this.orderlist=orderlist;
    }

    @Override
    public int getCount() {
        return (objects.size()+orderlist.size()+1);
    }

    @Override
    public SearchProductResponse.Datum getItem(int position) {

            return objects.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(position==0) {
            convertView = layoutInflater.inflate(R.layout.row_producttoorderlist, null);
            convertView.setTag(new ViewHolder(convertView));
            initializeView((ViewHolder) convertView.getTag());
        }
        else if(position <= orderlist.size()){
            convertView = layoutInflater.inflate(R.layout.row_search_product, null);
            convertView.setTag(new ViewHolder(convertView));
            initializeOrderViews(position, (ViewHolder) convertView.getTag());
        }
        else{
            convertView = layoutInflater.inflate(R.layout.row_search_product, null);
            convertView.setTag(new ViewHolder(convertView));
            initializeViews(position, (ViewHolder) convertView.getTag());
            }


        return convertView;
    }

    private void initializeOrderViews(int position, ViewHolder holder) {

        final Order object = orderlist.get(position-1);
        holder.imageview.setImageResource(R.drawable.expand);
        holder.imageview.getLayoutParams().height=75;
        if(context.getClass().getSimpleName().equals("MedicineSearchActivity")) {
            holder.imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    osf.addingProduct = true;
                    osf.productName = object.getProduct();
                    osf.companyName = object.getCompany();
                    osf.newproducttapped=true;
                    AppUtil.addproduct(context, object.getProduct(), object.getSupplier(), object.getCompany(), object.getCity(), object.getType(), osf, "", object.getSupplierid());
                }
            });
        }

        try {
            holder.favouriteIcon.setImageResource(R.drawable.favourite_icon);
            String productName = object.getProduct().trim();
            holder.rowTitle.setText(productName);
            holder.rowCompanyName.setText(object.getCompany().trim());
            String type = object.getType();
            if (type.equals("STRIP") || type.equals("TABLET") || productName.toLowerCase().contains("tablet") || productName.toLowerCase().contains("strip")) {
                holder.rowIcon.setImageResource(R.drawable.tablet);
            } else if (type.equals("BOTTLE") || type.equals("SYRUP") || type.equals("SUSPENSION")
                    || productName.toLowerCase().contains("bottle") || productName.toLowerCase().contains("syrup") || productName.toLowerCase().contains("suspension")) {
                holder.rowIcon.setImageResource(R.drawable.bottle);
            } else if (type.equals("DROPS") || productName.toLowerCase().contains("drops")) {
                holder.rowIcon.setImageResource(R.drawable.drop);
            } else if (type.equals("GEL") || productName.toLowerCase().contains("gel")) {
                holder.rowIcon.setImageResource(R.drawable.gel);
            } else if (type.equals("CREAM") || type.equals("TUBE") || type.equals("OINTMENT")
                    || productName.toLowerCase().contains("cream") || productName.toLowerCase().contains("tube") || productName.toLowerCase().contains("ointment")
            ) {
                holder.rowIcon.setImageResource(R.drawable.tube);
            } else if (type.equals("LOTION") || productName.toLowerCase().contains("lotion")) {
                holder.rowIcon.setImageResource(R.drawable.lotion);
            } else if (type.equals("CAPSULE") || productName.toLowerCase().contains("capsule")) {
                holder.rowIcon.setImageResource(R.drawable.capsuls);
            } else if (type.equals("SOAP") || productName.toLowerCase().contains("soap")) {
                holder.rowIcon.setImageResource(R.drawable.soap);
            } else if (type.equals("INJECTION") || productName.toLowerCase().contains("injection")) {
                holder.rowIcon.setImageResource(R.drawable.injection);
            } else if (type.equals("SPRAY") || productName.toLowerCase().contains("spray")) {
                holder.rowIcon.setImageResource(R.drawable.spray);
            } else {
                holder.rowIcon.setImageResource(R.drawable.unnown);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeView(ViewHolder holder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.ctatext.setText(Html.fromHtml("<p style='color: black'>Add a New Product </p> to add in your <b> order list</b>", Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.ctatext.setText(Html.fromHtml("<p style='color: black'>Add a New Product </p> to add in your <b> order list</b>"));
        }

    }


    private void initializeViews(int position, ViewHolder holder) {
        final SearchProductResponse.Datum object;
        object = getItem(position-orderlist.size()-1);
        holder.imageview.setImageResource(R.drawable.expand);
        holder.imageview.getLayoutParams().height=75;
        if(context.getClass().getSimpleName().equals("MedicineSearchActivity")) {
            holder.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                osf.addingProduct=true;
                osf.productID = object.getProductId();
                AppUtil.addproduct(context,object.getProductName(),"No Supplier Selected",object.getCompanyName(), PreferenceConnector.readString(context,PreferenceConnector.CITY,""),object.getForm(),osf,"","");
            }
        });
        }
         try {
                 holder.favouriteIcon.setImageResource(R.drawable.favourite_icon);
                 String productName = object.getProductName().trim();
                 holder.rowTitle.setText(productName);
                 holder.rowCompanyName.setText(object.getCompanyName().trim());
                 String type = object.getForm();
                 if (type.equals("STRIP") || type.equals("TABLET") || productName.toLowerCase().contains("tablet") || productName.toLowerCase().contains("strip")) {
                     holder.rowIcon.setImageResource(R.drawable.tablet);
                 } else if (type.equals("BOTTLE") || type.equals("SYRUP") || type.equals("SUSPENSION")
                         || productName.toLowerCase().contains("bottle") || productName.toLowerCase().contains("syrup") || productName.toLowerCase().contains("suspension")) {
                     holder.rowIcon.setImageResource(R.drawable.bottle);
                 } else if (type.equals("DROPS") || productName.toLowerCase().contains("drops")) {
                     holder.rowIcon.setImageResource(R.drawable.drop);
                 } else if (type.equals("GEL") || productName.toLowerCase().contains("gel")) {
                     holder.rowIcon.setImageResource(R.drawable.gel);
                 } else if (type.equals("CREAM") || type.equals("TUBE") || type.equals("OINTMENT")
                         || productName.toLowerCase().contains("cream") || productName.toLowerCase().contains("tube") || productName.toLowerCase().contains("ointment")
                 ) {
                     holder.rowIcon.setImageResource(R.drawable.tube);
                 } else if (type.equals("LOTION") || productName.toLowerCase().contains("lotion")) {
                     holder.rowIcon.setImageResource(R.drawable.lotion);
                 } else if (type.equals("CAPSULE") || productName.toLowerCase().contains("capsule")) {
                     holder.rowIcon.setImageResource(R.drawable.capsuls);
                 } else if (type.equals("SOAP") || productName.toLowerCase().contains("soap")) {
                     holder.rowIcon.setImageResource(R.drawable.soap);
                 } else if (type.equals("INJECTION") || productName.toLowerCase().contains("injection")) {
                     holder.rowIcon.setImageResource(R.drawable.injection);
                 } else if (type.equals("SPRAY") || productName.toLowerCase().contains("spray")) {
                     holder.rowIcon.setImageResource(R.drawable.spray);
                 } else {
                     holder.rowIcon.setImageResource(R.drawable.unnown);
                 }
             } catch (Exception e) {
                 e.printStackTrace();
             }
    }

    protected class ViewHolder {
        private ImageView rowIcon;
        private ImageView imageview;
        private TextView rowTitle;
        private TextView rowCompanyName;
        private TextView ctatext;
        private LinearLayout rowFavourite;
        private ImageView favouriteIcon;


        public ViewHolder(View view) {
            rowIcon = view.findViewById(R.id.rowIcon);
            rowTitle = view.findViewById(R.id.rowTitle);
            rowCompanyName = view.findViewById(R.id.rowCompanyName);
            ctatext = view.findViewById(R.id.ctatext);
            rowFavourite = view.findViewById(R.id.rowFavourite);
            favouriteIcon = view.findViewById(R.id.favouriteIcon);
            imageview = view.findViewById(R.id.actionicon);
        }
    }
}
