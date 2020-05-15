package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.AdapterItemHistoryListRecyclerView;
import com.dotcom.rbs_system.Adapter.AdapterRepairTicketListRecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Item_history extends AppCompatActivity {
    String itemID,itemCategory;
    List<String> customerNameList, rbsList, dateList;

    SimpleDateFormat sfd;

    DatabaseReference itemRef;
    DatabaseReference itemHistoryRef;
    Query orderQuery;

    RecyclerView itemHistoryRecyclerView;
    AdapterItemHistoryListRecyclerView adapterItemHistoryListRecyclerView;

    TextView itemDetailsToggle_textView;
    TextView itemName_textView,serialNo_textView,category_textView,condition_textView,notes_textView;

    LinearLayout itemDetails;

    Progreess_dialog pd1,pd2;

    Boolean toggleCheck = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_history);

        Initialization();
        InitialDataFetch();
        ClickListeners();


    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void Initialization() {
        itemName_textView = (TextView)findViewById(R.id.itemName_textView);
        serialNo_textView = (TextView)findViewById(R.id.serialNo_textView);
        category_textView = (TextView)findViewById(R.id.category_textView);
        condition_textView = (TextView)findViewById(R.id.condition_textView);
        notes_textView = (TextView)findViewById(R.id.notes_textView);

        itemID = getIntent().getStringExtra("ITEM_ID");
        itemCategory = getIntent().getStringExtra("ITEM_CATEGORY");
        customerNameList = new ArrayList<>();
        rbsList = new ArrayList<>();
        dateList = new ArrayList<>();

        sfd = new SimpleDateFormat("dd-MM-yyyy");

        itemHistoryRef = FirebaseDatabase.getInstance().getReference("Item_history/"+itemID);

        orderQuery = itemHistoryRef.orderByChild("Timestamp");

        itemHistoryRecyclerView = (RecyclerView)findViewById(R.id.itemHistoryRecyclerView);
        itemHistoryRecyclerView.setLayoutManager(new GridLayoutManager(Item_history.this,1));

        itemDetailsToggle_textView = (TextView)findViewById(R.id.itemDetailsToggle_textView);

        itemDetails = (LinearLayout)findViewById(R.id.itemDetails);

        itemRef = FirebaseDatabase.getInstance().getReference("Items");

        pd1 = new Progreess_dialog();
        pd2 = new Progreess_dialog();
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void InitialDataFetch() {
        gettingHistoryList();
        getItem(itemCategory,itemID);
    }

    private void gettingHistoryList() {
        orderQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pd1.showProgressBar(Item_history.this);
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        customerNameList.add(dataSnapshot1.child("Customer_name").getValue().toString());
                        rbsList.add(dataSnapshot1.child("RBS").getValue().toString());
                        dateList.add(dataSnapshot1.child("Date").getValue().toString());

                    }
                    Collections.reverse(customerNameList);
                    Collections.reverse(rbsList);
                    Collections.reverse(dateList);

                    adapterItemHistoryListRecyclerView = new AdapterItemHistoryListRecyclerView(Item_history.this,customerNameList,rbsList,dateList);
                    itemHistoryRecyclerView.setAdapter(adapterItemHistoryListRecyclerView);
                    pd1.dismissProgressBar(Item_history.this);
                }else {
                    pd1.dismissProgressBar(Item_history.this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd1.dismissProgressBar(Item_history.this);
            }
        });
    }

    private void getItem(String item_category, String item_keyID) {
        itemRef.child(item_category).child(item_keyID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pd2.showProgressBar(Item_history.this);
                if (dataSnapshot.exists()){
                    itemName_textView.setText(dataSnapshot.child("Item_name").getValue().toString());
                    serialNo_textView.setText(dataSnapshot.child("Item_id").getValue().toString());
                    category_textView.setText(dataSnapshot.child("Category").getValue().toString());
                    condition_textView.setText(dataSnapshot.child("Condition").getValue().toString());
                    notes_textView.setText(dataSnapshot.child("Notes").getValue().toString());

                    pd2.dismissProgressBar(Item_history.this);
                }else {
                    pd2.dismissProgressBar(Item_history.this);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd2.dismissProgressBar(Item_history.this);
            }
        });
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void ClickListeners() {
        itemDetailsToggle();
    }

    private void itemDetailsToggle() {
        itemDetailsToggle_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!toggleCheck){
                    itemDetails.setVisibility(View.VISIBLE);
                    itemDetailsToggle_textView.setText("Hide detailss");
                    itemDetailsToggle_textView.setTextColor(getResources().getColor(R.color.textGrey));
                    itemDetailsToggle_textView.setBackground(getResources().getDrawable(R.drawable.main_button_grey));

                    toggleCheck = true;
                }else {
                    itemDetails.setVisibility(View.GONE);
                    itemDetailsToggle_textView.setText("Show Item Details");
                    itemDetailsToggle_textView.setTextColor(getResources().getColor(R.color.textBlue));
                    itemDetailsToggle_textView.setBackground(getResources().getDrawable(R.drawable.main_button));
                    toggleCheck = false;
                }
            }
        });
    }
}
