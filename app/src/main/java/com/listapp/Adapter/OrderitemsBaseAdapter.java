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
import android.widget.TextView;

import androidx.room.Room;

import com.listapp.Fragment.OrderlistFragment;
import com.listapp.ListAppUtil.AppUtil;
import com.listapp.ListAppUtil.Order;
import com.listapp.ListAppUtil.OrderDB;
import com.listapp.ListAppUtil.OrderDao;
import com.listapp.ListAppUtil.YesNoInteface;
import com.listapp.R;

import java.util.ArrayList;
import java.util.List;

public class OrderitemsBaseAdapter extends BaseAdapter {

    private List<Order> objects = new ArrayList<>();

    private Context context;
    private LayoutInflater layoutInflater;
    OrderlistFragment olf;

    public OrderitemsBaseAdapter(Context context, List<Order> objects, OrderlistFragment olf) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects = objects;
        this.olf=olf;
    }

    @Override
    public int getCount() {
        return (objects.size());
    }

    @Override
    public Order getItem(int position) {

            return objects.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.row_orderitem, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews(position, (ViewHolder) convertView.getTag());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView pn=v.findViewById(R.id.rowTitle);
                olf.notesedittext.setVisibility(View.GONE);
                AppUtil.editproduct(context,pn.getText().toString(),olf);
            }
        });
        return convertView;
    }



    private void initializeViews(final int position, ViewHolder holder) {
        final Order object;

             object = getItem(position);
            final String productName = object.getProduct();
            holder.rowQuantity.setText(object.getQuantity());
            if(object.getScheme().isEmpty())
                holder.scheme.setText("No scheme applied");
            else
                holder.scheme.setText(object.getScheme());

        try {
                 holder.favouriteIcon.setImageResource(R.drawable.close);
                 holder.favouriteIcon.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         AppUtil.yesNoDialog(context, "Are you sure You want to remove this product from order list?", new YesNoInteface() {
                             @Override
                             public void isNoYes(boolean b) {
                                 if(b){
                                     OrderDB db = Room.databaseBuilder(context.getApplicationContext(),
                                             OrderDB.class, "Order").allowMainThreadQueries().build();
                                     final OrderDao od= db.orderDao();
                                     final Order o= od.getOrderbyproduct(productName);
                                     od.deleteOrder(o);
                                     olf.deleteOrder(position,object.getSupplier());

                                 }
                             }
                         });
                     }
                 });
                 holder.rowTitle.setText(productName);
                 holder.rowCompanyName.setText(object.getCompany());
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

    protected class ViewHolder {
        private ImageView rowIcon;
        private TextView rowTitle;
        private TextView rowCompanyName;
        private TextView rowQuantity;
        private TextView scheme;
        private ImageView favouriteIcon;

        public ViewHolder(View view) {
            rowIcon = view.findViewById(R.id.rowIcon);
            rowTitle = view.findViewById(R.id.rowTitle);
            rowCompanyName = view.findViewById(R.id.rowCompanyName);
            rowQuantity = view.findViewById(R.id.editText2);
            favouriteIcon = view.findViewById(R.id.actionicon);
            scheme = view.findViewById(R.id.editText3);

        }
    }
}
