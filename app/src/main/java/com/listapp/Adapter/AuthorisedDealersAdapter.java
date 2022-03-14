package com.listapp.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.listapp.API_Utility.Ratrofit_Implementation.Model.SupplierDetails.SupplierProfile;
import com.listapp.R;

import java.util.ArrayList;
import java.util.List;

public class AuthorisedDealersAdapter extends BaseAdapter {

    private List<SupplierProfile.Data.Supplier.CompanyDealership> list = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;

    public AuthorisedDealersAdapter(Context context, List<SupplierProfile.Data.Supplier.CompanyDealership> list) {
        this.context = context;
        this.list = list;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public SupplierProfile.Data.Supplier.CompanyDealership getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_authorised_dealers, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews(getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(SupplierProfile.Data.Supplier.CompanyDealership object, ViewHolder holder) {

        holder.dealerName.setText(object.getCompanyName().trim());
    }

    protected class ViewHolder {
        private TextView dealerName;

        public ViewHolder(View view) {
            dealerName = view.findViewById(R.id.dealerName);
        }
    }
}
