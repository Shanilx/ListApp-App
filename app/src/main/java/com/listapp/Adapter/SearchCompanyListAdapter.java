package com.listapp.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.listapp.API_Utility.Ratrofit_Implementation.Model.CompanySearch.CompanySearchResponse;
import com.listapp.R;

import java.util.ArrayList;
import java.util.List;

public class SearchCompanyListAdapter extends BaseAdapter {

    private List<CompanySearchResponse.Datum> list = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;
    private int length;
    private boolean loaded;

    public SearchCompanyListAdapter(Context context, List<CompanySearchResponse.Datum> list) {
        this.context = context;
        this.list = list;
        this.layoutInflater = LayoutInflater.from(context);
        length = list.size();
        loaded=false;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CompanySearchResponse.Datum getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null) {

                convertView = layoutInflater.inflate(R.layout.row_company_list, null);
                convertView.setTag(new ViewHolder(convertView));

        }
        initializeViews(getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }



    private void initializeViews(CompanySearchResponse.Datum object, ViewHolder holder) {

        holder.dealerName.setText(object.getCompanyName().trim());
    }

    protected class ViewHolder {
        private TextView dealerName;

        public ViewHolder(View view) {

            dealerName = view.findViewById(R.id.dealerName);
        }
    }
}
