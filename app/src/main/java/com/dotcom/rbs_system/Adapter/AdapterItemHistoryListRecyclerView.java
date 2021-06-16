package com.dotcom.rbs_system.Adapter;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterItemHistoryListRecyclerView extends RecyclerView.Adapter<AdapterItemHistoryListRecyclerView.ViewHolder> {
    Context context;
    List<String> status_textView;
    List<String> dateList;
    List<String> shopkeeper_status;
    List<String> shopkeeper_name_textview;
    List<String> customer_status;
    List<String> customer_name_textview;
//    List<String> shopkeeperImage_imageView;
    Activity activity;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Listed_faults");

    public AdapterItemHistoryListRecyclerView(Context context,List<String> status_textView, List<String> shopkeeper_status, List<String> shopkeeper_name_textview,List<String> customer_status,List<String> customer_name_textview,List<String> dateList) {
        this.context = context;
        this.status_textView = status_textView;
        this.shopkeeper_status = shopkeeper_status;
        this.shopkeeper_name_textview = shopkeeper_name_textview;
        this.customer_status = customer_status;
        this.customer_name_textview = customer_name_textview;
//        this.shopkeeperImage_imageView = shopkeeperImage_imageView;
        this.dateList = dateList;

    }

    @NonNull
    @Override
    public AdapterItemHistoryListRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_itemhistorylist_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterItemHistoryListRecyclerView.ViewHolder holder, final int position) {

        holder.status_textView.setText(status_textView.get(position));
        holder.shopkeeper_status.setText(shopkeeper_status.get(position));
        holder.shopkeeper_name_textview.setText(shopkeeper_name_textview.get(position));
        holder.date_textView.setText(dateList.get(position));
//        Picasso.get(shopkeeperImage_imageView).load();

    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView date_textView,status_textView,shopkeeper_status,shopkeeper_name_textview,customer_status,customer_name_textview;
        ImageView shopkeeperImage_imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date_textView = (TextView)itemView.findViewById(R.id.date_textView);
            status_textView = (TextView)itemView.findViewById(R.id.status_textView);
            shopkeeper_status = (TextView)itemView.findViewById(R.id.shopkeeper_status);
            shopkeeper_name_textview = (TextView)itemView.findViewById(R.id.shopkeeper_name_textview);
            customer_status = (TextView)itemView.findViewById(R.id.customer_status);
            customer_name_textview = (TextView)itemView.findViewById(R.id.customer_name_textview);
            shopkeeperImage_imageView = (ImageView) itemView.findViewById(R.id.shopkeeperImage_imageView);


        }
    }
}
