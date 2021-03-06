package com.dotcom.rbs_system.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapterSettingsFaultListRecyclerView extends RecyclerView.Adapter<AdapterSettingsFaultListRecyclerView.ViewHolder> {
    Context context;
    List<String> faultNameList, faultPriceList, faultKeyIDList;
    Activity activity;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Listed_faults");

    public AdapterSettingsFaultListRecyclerView(Context context, List<String> faultNameList, List<String> faultPriceList, List<String> faultKeyIDList) {
        this.context = context;
        this.faultNameList = faultNameList;
        this.faultPriceList = faultPriceList;
        this.faultKeyIDList = faultKeyIDList;
        activity = (Activity)context;
    }

    @NonNull
    @Override
    public AdapterSettingsFaultListRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_settings_faultlist_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSettingsFaultListRecyclerView.ViewHolder holder, final int position) {
        holder.faultName_textView.setText(faultNameList.get(position));
        holder.faultPrice_textView.setText(faultPriceList.get(position));

        holder.remove_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child(faultKeyIDList.get(position)).removeValue();
                faultNameList.remove(position);
                faultPriceList.remove(position);
                faultKeyIDList.remove(position);

                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return faultNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView faultName_textView,faultPrice_textView;
        TextView remove_textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            faultName_textView = (TextView)itemView.findViewById(R.id.faultName_textView);
            faultPrice_textView = (TextView)itemView.findViewById(R.id.faultPrice_textView);
            remove_textView = (TextView) itemView.findViewById(R.id.remove_textView);
        }
    }
}
