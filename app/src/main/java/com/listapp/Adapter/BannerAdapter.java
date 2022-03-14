package com.listapp.Adapter;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.banner.BannerModel;
import com.listapp.R;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.MyHolder> {
    Context mContext;
    public static final int ITEM_PER_AD = 3;
    BannerModel sliderItems;
    ViewPager2 viewPager2;

    public BannerAdapter(Context mContext, ViewPager2 viewPager2) {
        this.mContext = mContext;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_banner_layout, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  MyHolder holder, final int position) {
        // int itemPosition = position - position / ITEM_PER_AD;

        String url = sliderItems.getData().get(position).getImageUrl();
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.N){
            // glide not loading image URL start with https:// for below N version so replace with http://
            if (url.startsWith("https://")){
                url = url.replace("https://","http://");
            }
        }else {
            Log.i("tag","Upper Android 7.0");
        }

        Glide.with(mContext)
                .load(url)
                .placeholder(R.drawable.loading_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(true)
                .into(holder.adapterImg);

        holder.adapterImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = sliderItems.getData().get(position).getLink();
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url = "http://" + url;

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                mContext.startActivity(browserIntent);
            }
        });

       /* if (position == sliderItems.getData().size() - 2) {
            viewPager2.post(runnable);
        }*/
    }

    @Override
    public int getItemCount() {
        //  int itemCount = 1;
        //  itemCount += itemCount / ITEM_PER_AD;
        if (sliderItems != null && sliderItems.getData() != null)
            return sliderItems.getData().size();
        else return 0;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView adapterImg;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            adapterImg = itemView.findViewById(R.id.adapterImg);
        }
    }

    public void setAdapterData(BannerModel item) {
        this.sliderItems = item;
        notifyDataSetChanged();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sliderItems.getData().addAll(sliderItems.getData());
            notifyDataSetChanged();
        }
    };

}
