package com.listapp.Adapter;

/**
 * Created by syscraft on 7/12/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.listapp.API_Utility.Ratrofit_Implementation.Model.Search_Product.SearchProductResponse;
import com.listapp.R;

import java.util.ArrayList;
import java.util.List;

public class ProductListBaseAdapter extends BaseAdapter {

    private List<SearchProductResponse.Datum> objects = new ArrayList<SearchProductResponse.Datum>();

    private Context context;
    private LayoutInflater layoutInflater;

    public ProductListBaseAdapter(Context context,List<SearchProductResponse.Datum> objects ) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return (objects.size());
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


        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.row_search_product, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews(position, (ViewHolder) convertView.getTag());

        return convertView;
    }



    private void initializeViews(int position, ViewHolder holder) {
        SearchProductResponse.Datum object;

             object = getItem(position);
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
        private TextView rowTitle;
        private TextView rowCompanyName;
        private TextView rowQuantity;
        private LinearLayout rowFavourite;
        private ImageView favouriteIcon;

        public ViewHolder(View view) {
            rowIcon = view.findViewById(R.id.rowIcon);
            rowTitle = view.findViewById(R.id.rowTitle);
            rowCompanyName = view.findViewById(R.id.rowCompanyName);
            rowQuantity = view.findViewById(R.id.rowQuantity);
            rowFavourite = view.findViewById(R.id.rowFavourite);
            favouriteIcon = view.findViewById(R.id.favouriteIcon);
        }
    }
}
