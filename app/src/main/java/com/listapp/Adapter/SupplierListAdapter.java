package com.listapp.Adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.listapp.API_Utility.Ratrofit_Implementation.Model.Search_Product.ProductDetails;
import com.listapp.R;

import java.util.ArrayList;
import java.util.List;

public class SupplierListAdapter extends BaseAdapter {

    private List<ProductDetails.Data.Supplier> supplierList = new ArrayList<ProductDetails.Data.Supplier>();

    private Context context;
    private LayoutInflater layoutInflater;
    private int length;

    public SupplierListAdapter(Context context, List<ProductDetails.Data.Supplier> supplierList) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.supplierList = supplierList;
        length = this.supplierList.size();
    }

    @Override
    public int getCount() {
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
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.row_supplierlist, null);
            convertView.setTag(new ViewHolder(convertView));
        }

        initializeViews(getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
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


        public ViewHolder(View view) {
            storeName = view.findViewById(R.id.storeName);
            address = view.findViewById(R.id.address);
            authView = view.findViewById(R.id.authView);
            favouriteView = view.findViewById(R.id.favouriteView);
            viewLine = view.findViewById(R.id.viewLine);
        }
    }
}

