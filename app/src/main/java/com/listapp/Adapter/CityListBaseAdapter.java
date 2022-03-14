package com.listapp.Adapter;



import android.content.Context;
import android.view.Gravity;
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

public class CityListBaseAdapter extends BaseAdapter {

    private List<String> objects;

    private Context context;
    private LayoutInflater layoutInflater;

    public CityListBaseAdapter(Context context, List<String> objects ) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public String getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_supplierlist, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews(getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(String object, ViewHolder holder) {
        try {

            holder.rowIcon.setImageResource(R.drawable.location);
            String productName = object;
            holder.rowTitle.setText(productName);
            holder.favouriteIcon.setVisibility(View.INVISIBLE);
            holder.rowFavourite.setVisibility(View.INVISIBLE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected class ViewHolder {
        private ImageView rowIcon;
        private TextView rowTitle;
        private ImageView rowFavourite;
        private ImageView favouriteIcon;

        public ViewHolder(View view) {
            rowIcon = view.findViewById(R.id.supicon);
            rowTitle = view.findViewById(R.id.storeName);
            rowFavourite = view.findViewById(R.id.authView);
            favouriteIcon = view.findViewById(R.id.favouriteView);
        }
    }
}
