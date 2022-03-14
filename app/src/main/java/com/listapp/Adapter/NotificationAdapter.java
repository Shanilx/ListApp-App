package com.listapp.Adapter;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.listapp.API_Utility.Ratrofit_Implementation.Model.Notification.NotificationModel;
import com.listapp.ListAppUtil.AppUtil;
import com.listapp.ListAppUtil.PreferenceConnector;
import com.listapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationAdapter extends BaseAdapter {

    private List<NotificationModel.Datum> list = new ArrayList<NotificationModel.Datum>();
    private Context context;
    private LayoutInflater layoutInflater;

    public NotificationAdapter(Context context, List<NotificationModel.Datum> list) {
        this.context = context;
        this.list = list;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public NotificationModel.Datum getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_notification, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews(getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(NotificationModel.Datum object, ViewHolder holder) {

        String read = object.getIsRead();
        if (PreferenceConnector.readString(context,object.getNotificationId(),"").equals(object.getNotificationId()) || read.equals("1")) {
            holder.title.setText(object.getTitle());
            String date1 = object.getDate();
            if (date1.equals(object.getCurrentDate()))
                holder.date.setText(object.getTime());
            else
                holder.date.setText(date1);

            holder.msg.setText(object.getMeassage());
        } else {
            holder.title.setText(AppUtil.getHalfBoldString(object.getTitle()));
            String date1 = object.getDate();
            if (date1.equals(object.getCurrentDate()))
                holder.date.setText(AppUtil.getHalfBoldString(object.getTime()));
            else
                holder.date.setText(date1);

            holder.msg.setText(AppUtil.getHalfBoldString(object.getMeassage()));
        }
    }

    protected class ViewHolder {
        private LinearLayout viewTitle;
        private TextView title;
        private TextView date;
        private TextView msg;

        public ViewHolder(View view) {
            viewTitle = view.findViewById(R.id.viewTitle);
            title = view.findViewById(R.id.title);
            date = view.findViewById(R.id.date);
            msg = view.findViewById(R.id.msg);
        }
    }
}
