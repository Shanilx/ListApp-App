package com.listapp.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.listapp.API_Utility.Ratrofit_Implementation.Model.Search_Product.ProductDetails;
import com.listapp.Fragment.OrderSearchFragment;
import com.listapp.ListAppUtil.Order;
import com.listapp.R;

import java.util.ArrayList;
import java.util.List;

public class OrderSupplierListAdapter extends BaseAdapter {

    private final OrderSearchFragment osf;
    private List<ProductDetails.Data.Supplier> supplierList = new ArrayList<ProductDetails.Data.Supplier>();

    private Context context;
    private LayoutInflater layoutInflater;
    private int length;
    List<Order> orderList;

    public OrderSupplierListAdapter(Context context, List<ProductDetails.Data.Supplier> supplierList, OrderSearchFragment osf) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.supplierList = supplierList;
        if(osf.addingProduct)
        length = this.supplierList.size()+1;
        else
        length = this.supplierList.size();
        this.osf=osf;
    }

    @Override
    public int getCount() {
        if(osf.addingProduct)
        return supplierList.size()+1;
        else
            return supplierList.size();

    }

    @Override
    public ProductDetails.Data.Supplier getItem(int position) {
        return supplierList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(position == 0 && osf.addingProduct){
            convertView = layoutInflater.inflate(R.layout.row_producttoorderlist, null);
            TextView  t13 = convertView.findViewById(R.id.textView13);
            t13.setText("Not finding the right supplier");
            TextView ctatext= convertView.findViewById(R.id.ctatext);
            ctatext.setText("Add new supplier");
            return convertView;
        }

        else{
            convertView = layoutInflater.inflate(R.layout.row_supplierlist, null);
            convertView.setTag(new ViewHolder(convertView));
            if(osf.addingProduct)
                initializeViews(getItem(position-1), (ViewHolder) convertView.getTag());
            else
                initializeViews(getItem(position), (ViewHolder) convertView.getTag());

            return convertView;}
    }

    private void initializeOrderViews(Order object, ViewHolder holder) {
        holder.storeName.setText(object.getSupplier().trim());
        holder.address.setText(object.getCity().trim());
        holder.authView.setImageResource(R.drawable.demmy_white);
    }


    private void initializeViews(ProductDetails.Data.Supplier object, ViewHolder holder) {


        holder.storeName.setText(object.getShopName().trim());
        holder.address.setText(object.getSupplierAddress().trim());
        if (object.getAuthorisedStatus().equals("Yes"))
            holder.authView.setImageResource(R.drawable.auth_icon);
        else
            holder.authView.setImageResource(R.drawable.demmy_white);
    }

    protected class ViewHolder {
        private TextView storeName;
        private TextView address;
        private ImageView authView;
        private ImageView favouriteView;
        private View viewLine;
        private LinearLayout adContainerView2;


        public ViewHolder(View view) {
            storeName = view.findViewById(R.id.storeName);
            address = view.findViewById(R.id.address);
            authView = view.findViewById(R.id.authView);
            favouriteView = view.findViewById(R.id.favouriteView);
            viewLine = view.findViewById(R.id.viewLine);

        }
    }
}

