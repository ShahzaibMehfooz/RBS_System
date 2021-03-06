package com.dotcom.rbs_system.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.Progreess_dialog;
import com.dotcom.rbs_system.R;
import com.dotcom.rbs_system.Repair_details;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.List;

public class AdapterRepairTicketListRecyclerView extends RecyclerView.Adapter<AdapterRepairTicketListRecyclerView.ViewHolder> {
    Context context;
    List<String> customerNameList, itemNameList, ticketNoList,pendingStatusList;
    Activity activity;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Repairs_ticket_list/"+ FirebaseAuth.getInstance().getCurrentUser().getUid().toString());

    public AdapterRepairTicketListRecyclerView(Context context, List<String> customerNameList, List<String> itemNameList, List<String> ticketNoList,List<String> pendingStatusList) {
        this.context = context;
        this.customerNameList = customerNameList;
        Collections.reverse(this.customerNameList);
        this.itemNameList = itemNameList;
        Collections.reverse(this.itemNameList);
        this.ticketNoList = ticketNoList;
        Collections.reverse(this.ticketNoList);
        this.pendingStatusList = pendingStatusList;
        Collections.reverse(this.pendingStatusList);
        activity = (Activity)context;
    }

    @NonNull
    @Override
    public AdapterRepairTicketListRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_repairticket_repairticketlist_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRepairTicketListRecyclerView.ViewHolder holder, final int position) {
        holder.customerName_textView.setText(customerNameList.get(position));
        holder.itemName_textView.setText(itemNameList.get(position));
        if (pendingStatusList!=null){
            if (pendingStatusList.get(position).equals("pending")){
                holder.ticketNo_textView.setText(ticketNoList.get(position)+"\n(Pending)");
            }else {
                holder.ticketNo_textView.setText(ticketNoList.get(position));
            }
        }else {
            holder.ticketNo_textView.setText(ticketNoList.get(position));
        }


        holder.leftLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Repair_details.class);
                intent.putExtra("REPAIR_ID",ticketNoList.get(position));
                context.startActivity(intent);
            }
        });

        holder.remove_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child(ticketNoList.get(position)).removeValue();
                activity.recreate();
            }
        });


    }

    @Override
    public int getItemCount() {
        return ticketNoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        Progreess_dialog pd;

        TextView ticketNo_textView,customerName_textView,itemName_textView;
        TextView remove_textView;
        LinearLayout leftLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pd = new Progreess_dialog();

            ticketNo_textView = (TextView)itemView.findViewById(R.id.ticketNo_textView);
            customerName_textView = (TextView)itemView.findViewById(R.id.customerName_textView);
            itemName_textView = (TextView)itemView.findViewById(R.id.item_category_textView);
            remove_textView = (TextView)itemView.findViewById(R.id.remove_textView);

            leftLayout = (LinearLayout) itemView.findViewById(R.id.leftLayout);

        }
    }
}
