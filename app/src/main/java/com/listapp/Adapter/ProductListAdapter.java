package com.listapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.listapp.API_Utility.Ratrofit_Implementation.Model.Search_Product.SearchProductResponse;
import com.listapp.R;

import java.util.List;

/**
 * Created by Nivesh on 5/30/2017.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.RecyclerViewViewHolder> {

    private RecyclerViewViewHolder viewHolder;
    private View view;
    Context ctx;
    List<SearchProductResponse.Datum> productList;

    public ProductListAdapter(Context ctx, List<SearchProductResponse.Datum> productList) {
        this.ctx = ctx;
        this.productList = productList;
    }

    @Override
    public RecyclerViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_search_product, parent, false);
        viewHolder = new RecyclerViewViewHolder(view);
        viewHolder.setIsRecyclable(false);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewViewHolder holder, int position) {

        holder.favouriteIcon.setImageResource(R.drawable.favourite_icon);

        holder.rowTitle.setText(productList.get(position).getProductName());
        holder.rowCompanyName.setText(productList.get(position).getCompanyName());
        String type = productList.get(position).getPackingType();
    //    Log.e(">>",type);
      //  Log.e(">>",productList.get(position).getProductName());
        if(type.equals("STRIP") || type.equals("TABLET")) {
            holder.rowIcon.setImageResource(R.drawable.tablet);
        ///    holder.rowQuantity.setText("Strip of "+productList.get(position).getPackSize()+" Tablets");
        }
        else if(type.equals("BOTTLE") || type.equals("SYRUP") || type.equals("SUSPENSION")) {
            holder.rowIcon.setImageResource(R.drawable.bottle);
     ///       holder.rowQuantity.setText("Bottle of "+productList.get(position).getPackSize()+" Suspension");
        }
        else if( type.equals("DROPS")){
            holder.rowIcon.setImageResource(R.drawable.drop);
     //       holder.rowQuantity.setText("Packet of "+productList.get(position).getPackSize()+" Drop");
        }
        else if(type.equals("GEL")){
            holder.rowIcon.setImageResource(R.drawable.gel);
     //       holder.rowQuantity.setText("Packet of "+productList.get(position).getPackSize()+" Drop");
        }
        else if(type.equals("CREAM") || type.equals("TUBE") || type.equals("OINTMENT")){
            holder.rowIcon.setImageResource(R.drawable.tube);
     //       holder.rowQuantity.setText("Packet of "+productList.get(position).getPackSize()+" Drop");
        }
        else if(type.equals("LOTION")){
            holder.rowIcon.setImageResource(R.drawable.lotion);
    //        holder.rowQuantity.setText("Packet of "+productList.get(position).getPackSize()+" Drop");
        }
        else if(type.equals("CAPSULE")){
            holder.rowIcon.setImageResource(R.drawable.capsuls);
     //       holder.rowQuantity.setText("Packet of "+productList.get(position).getPackSize()+" Drop");
        }
        else if(type.equals("SOAP")){
            holder.rowIcon.setImageResource(R.drawable.soap);
            //       holder.rowQuantity.setText("Packet of "+productList.get(position).getPackSize()+" Drop");
        }
        else if(type.equals("INJECTION")){
            holder.rowIcon.setImageResource(R.drawable.injection);
            //       holder.rowQuantity.setText("Packet of "+productList.get(position).getPackSize()+" Drop");
        }
        else if(type.equals("SPRAY")){
            holder.rowIcon.setImageResource(R.drawable.spray);
            //       holder.rowQuantity.setText("Packet of "+productList.get(position).getPackSize()+" Drop");
        }
        else
        {
            holder.rowIcon.setImageResource(R.drawable.unnown);
         //   holder.rowQuantity.setText("Miscellaneous type of "+type);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class RecyclerViewViewHolder extends RecyclerView.ViewHolder {

        private ImageView rowIcon;
        private TextView rowTitle;
        private TextView rowCompanyName, rowQuantity;
        private ImageView favouriteIcon;


        public RecyclerViewViewHolder(View itemView) {
            super(itemView);
            rowIcon = (ImageView) view.findViewById(R.id.rowIcon);
            rowTitle = (TextView) view.findViewById(R.id.rowTitle);
            rowCompanyName = (TextView) view.findViewById(R.id.rowCompanyName);
            rowQuantity = (TextView) view.findViewById(R.id.rowQuantity);
            favouriteIcon = (ImageView) view.findViewById(R.id.favouriteIcon);
        }
    }

}
