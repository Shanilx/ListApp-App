package com.listapp.Adapter;

/**
 * Created by syscraft on 7/31/2017.
 */

import java.util.List;

import android.content.Context;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.listapp.Models.YearModel;
import com.listapp.R;

public class YearAdapter extends BaseAdapter {

    private final List<YearModel> objects;

    private Context context;
    private LayoutInflater layoutInflater;
    private String selectedYear;

    public YearAdapter(Context context, List<YearModel> years) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects = years;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public YearModel getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.year_adapter, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews(getItem(position), (ViewHolder) convertView.getTag(), position);
        return convertView;
    }

    private void initializeViews(YearModel object, ViewHolder holder, int position) {

        if (object.isSleted()) {
            holder.yearView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
            holder.textYear.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.textYear.setText(object.getYear());
        } else if (position == 0) {
            holder.yearView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
            holder.textYear.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.textYear.setText(object.getYear());
        } else {
            holder.yearView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
            holder.textYear.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.textYear.setText(object.getYear());
        }

    }

    protected class ViewHolder {
        private LinearLayout yearView;
        private TextView textYear;

        public ViewHolder(View view) {
            yearView = view.findViewById(R.id.year_view);
            textYear = view.findViewById(R.id.textYear);

        }
    }


}

