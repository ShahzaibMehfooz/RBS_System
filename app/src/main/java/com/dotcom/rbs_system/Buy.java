package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Model.SampleSearchModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class Buy extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    LinearLayout itemDetails,customerDetails;

    DatabaseReference reference;

    ImageButton Back_btn,sms_btn,gmail_btn;
    Button date_btn,customer_add_btn,searchForCustomer_btn, item_add_btn,searchForItem_btn,submit_btn;
    TextView date_textView;
    String firebaseAuthUID;
    List<String> exisitngCustomerList,exisitngCustomerIDList,exisitngCustomerKeyIDList,exisitngItemsList,exisitngItemsIDList,exisitngItemsKeyIDList;
    List<String> exisitngItemsCategoryList,existingItemsConditionsList,existingItemsNotesList,existingCustomerPhnoList,existingCustomerDobList,existingCustomerAddressList,existingCustomerEmailList;
    DatabaseReference existingCustomersRef,existingItemsRef;
    TextView category_textView,condition_textView,notes_textView,phno_textView,dob_textView,address_textView,email_textView;

    String customerKeyID, itemKeyID;
    Progreess_dialog pd;

    EditText suggest_price_editText,purchase_price_editText,quantity_editText,cash_editText,voucher_editText,paid_editText;

    private ArrayList<SampleSearchModel> createItemsData(){
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i=0;i<exisitngItemsList.size();i++){
            items.add(new SampleSearchModel(exisitngItemsList.get(i)+"\n("+exisitngItemsIDList.get(i)+")",exisitngItemsIDList.get(i),exisitngItemsList.get(i),exisitngItemsCategoryList.get(i),existingItemsConditionsList.get(i),existingItemsNotesList.get(i),null,exisitngItemsKeyIDList.get(i)));
        }

        return items;
    }

    private ArrayList<SampleSearchModel> createCustomerData(){
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i=0;i<exisitngCustomerList.size();i++){
            items.add(new SampleSearchModel(exisitngCustomerList.get(i)+"\n("+exisitngCustomerIDList.get(i)+")",exisitngCustomerIDList.get(i),exisitngCustomerList.get(i),existingCustomerPhnoList.get(i),existingCustomerDobList.get(i),existingCustomerAddressList.get(i),existingCustomerEmailList.get(i),exisitngCustomerKeyIDList.get(i)));
        }

        return items;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        initialize();

        fetchingExisitingCustomers();
        fetchingExisitingItems();

        onClickListeners();

    }

    private void initialize() {

        reference = FirebaseDatabase.getInstance().getReference();

        itemDetails = (LinearLayout)findViewById(R.id.itemDetails);
        customerDetails = (LinearLayout)findViewById(R.id.customerDetails);

        exisitngCustomerList = new ArrayList<>();
        exisitngCustomerIDList = new ArrayList<>();
        exisitngCustomerKeyIDList = new ArrayList<>();
        exisitngItemsList = new ArrayList<>();
        exisitngItemsIDList = new ArrayList<>();
        exisitngItemsKeyIDList = new ArrayList<>();

        exisitngItemsCategoryList = new ArrayList<>();
        existingItemsConditionsList= new ArrayList<>();
        existingItemsNotesList= new ArrayList<>();
        existingCustomerPhnoList= new ArrayList<>();
        existingCustomerDobList= new ArrayList<>();
        existingCustomerAddressList= new ArrayList<>();
        existingCustomerEmailList= new ArrayList<>();

        category_textView=(TextView)findViewById(R.id.category_textView);
        condition_textView=(TextView)findViewById(R.id.condition_textView);
        notes_textView=(TextView)findViewById(R.id.notes_textView);
        phno_textView=(TextView)findViewById(R.id.phno_textView);
        dob_textView=(TextView)findViewById(R.id.dob_textView);
        address_textView=(TextView)findViewById(R.id.address_textView);
        email_textView=(TextView)findViewById(R.id.email_textView);

        suggest_price_editText = (EditText)findViewById(R.id.suggest_price_editText);
        purchase_price_editText = (EditText)findViewById(R.id.purchase_price_editText);
        quantity_editText = (EditText)findViewById(R.id.quantity_editText);
        cash_editText = (EditText)findViewById(R.id.cash_editText);
        voucher_editText = (EditText)findViewById(R.id.voucher_editText);
        paid_editText = (EditText)findViewById(R.id.paid_editText);

        searchForCustomer_btn = (Button)findViewById(R.id.searchForCustomer_btn);
        searchForItem_btn = (Button)findViewById(R.id.searchForItem_btn);
        submit_btn = (Button)findViewById(R.id.submit_btn);

        firebaseAuthUID = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid());
        existingCustomersRef = FirebaseDatabase.getInstance().getReference("Customer_list");
        existingItemsRef = FirebaseDatabase.getInstance().getReference("Items");

        Back_btn=(ImageButton)findViewById(R.id.Back_btn);
        sms_btn=(ImageButton)findViewById(R.id.sms_btn);
        customer_add_btn=(Button) findViewById(R.id.customer_add_btn);
        item_add_btn =(Button) findViewById(R.id.item_add_btn);
        date_btn=(Button)findViewById(R.id.date_btn);
        date_textView =(TextView)findViewById(R.id.date_textView);
        gmail_btn=(ImageButton) findViewById(R.id.gmail_btn);

        pd=new Progreess_dialog();
    }

    private void fetchingExisitingCustomers() {
        existingCustomersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    exisitngCustomerList.add(String.valueOf(dataSnapshot1.child("Name").getValue()));
                    exisitngCustomerIDList.add(String.valueOf(dataSnapshot1.child("ID").getValue()));
                    existingCustomerPhnoList.add(String.valueOf(dataSnapshot1.child("Phone_no").getValue()));
                    existingCustomerDobList.add(String.valueOf(dataSnapshot1.child("DOB").getValue()));
                    existingCustomerAddressList.add(String.valueOf(dataSnapshot1.child("Address").getValue()));
                    existingCustomerEmailList.add(String.valueOf(dataSnapshot1.child("Email").getValue()));
                    exisitngCustomerKeyIDList.add(String.valueOf(dataSnapshot1.child("key_id").getValue()));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void fetchingExisitingItems() {
        existingItemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int i = 0;
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    for (DataSnapshot dataSnapshot2: dataSnapshot1.getChildren()){
                        exisitngItemsList.add(String.valueOf(dataSnapshot2.child("Item_name").getValue()));
                        exisitngItemsIDList.add(String.valueOf(dataSnapshot2.child("Item_id").getValue()));
                        exisitngItemsCategoryList.add(String.valueOf(dataSnapshot2.child("Category").getValue()));
                        existingItemsConditionsList.add(String.valueOf(dataSnapshot2.child("Condition").getValue()));
                        existingItemsNotesList.add(String.valueOf(dataSnapshot2.child("Notes").getValue()));
                        exisitngItemsKeyIDList.add(String.valueOf(dataSnapshot2.child("key_id").getValue()));
                        i++;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String currentDateString= DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        date_textView.setText(currentDateString);

    }


    private void onClickListeners() {

        sms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + "0323"));
                intent.putExtra("sms_body", "Hi how are you");
                startActivity(intent);
            }
        });

        Back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchForCustomer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Buy.this, "Search...",
                        "What are you looking for...?", null, createCustomerData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                searchForCustomer_btn.setText(item.getTitle());
                                phno_textView.setText(item.getVal1());
                                dob_textView.setText(item.getVal2());
                                address_textView.setText(item.getVal3());
                                email_textView.setText(item.getVal4());
                                customerKeyID = item.getVal5();
                                searchForCustomer_btn.setBackgroundColor(getResources().getColor(R.color.colorLightGrey));
                                searchForCustomer_btn.setTextColor(getResources().getColor(R.color.textGrey));
                                customerDetails.setVisibility(View.VISIBLE);
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        searchForItem_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Buy.this, "Search...",
                        "What are you looking for...?", null, createItemsData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                searchForItem_btn.setText(item.getTitle());
                                category_textView.setText(item.getVal1());
                                condition_textView.setText(item.getVal2());
                                notes_textView.setText(item.getVal3());
                                itemKeyID = item.getVal5();
                                searchForItem_btn.setBackgroundColor(getResources().getColor(R.color.colorLightGrey));
                                searchForItem_btn.setTextColor(getResources().getColor(R.color.textGrey));
                                itemDetails.setVisibility(View.VISIBLE);
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
        // Firebase config

        date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datepicker=new DatePickerFragment();
                datepicker.show(getSupportFragmentManager(),"date picker");

            }
        });

        customer_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Buy.this,Customer_details.class);
                startActivity(intent);
            }
        });

        item_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Buy.this,Item_detail.class);
                startActivity(intent);
            }
        });

        gmail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(Buy.this, "yes", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(Intent.ACTION_SEND);
                it.setType("message/rfc822");
                startActivity(Intent.createChooser(it,"Choose Mail App"));
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    detailsSubmit();
            }
        });
    }


    private void detailsSubmit() {
        String key = reference.push().getKey();
        reference.child("Buy_list").child(key).child("Customer_keyID").setValue(customerKeyID);
        reference.child("Buy_list").child(key).child("Item_keyID").setValue(itemKeyID);

        reference.child("Buy_list").child(key).child("Suggested_price").setValue(suggest_price_editText.getText().toString());
        reference.child("Buy_list").child(key).child("Purchase_price").setValue(purchase_price_editText.getText().toString());
        reference.child("Buy_list").child(key).child("Quantity").setValue(quantity_editText.getText().toString());
        reference.child("Buy_list").child(key).child("Date").setValue(date_textView.getText().toString());
        reference.child("Buy_list").child(key).child("Cash").setValue(cash_editText.getText().toString());
        reference.child("Buy_list").child(key).child("Voucher").setValue(voucher_editText.getText().toString());
        reference.child("Buy_list").child(key).child("Paid").setValue(paid_editText.getText().toString());


        reference.child("Buy_list").child(key).child("key_id").setValue(key);
        reference.child("Buy_list").child(key).child("added_by").setValue(firebaseAuthUID);


        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }
}