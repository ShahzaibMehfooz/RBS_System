package com.dotcom.rbs_system;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dotcom.rbs_system.Adapter.Adapter_Vendor_inventory_RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VendorShop#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VendorShop extends Fragment {

    View view;

    RecyclerView vendor_inventory_RecyclerView;
    List<String> vendor_category;
    Button vendor_inventory_add_btn;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VendorShop() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VendorShop.
     */
    // TODO: Rename and change types and number of parameters
    public static VendorShop newInstance(String param1, String param2) {
        VendorShop fragment = new VendorShop();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_vendor_shop, container, false);

        Initialize();
        InitialOperations();
        Onclicklistners();

        return view;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void Initialize() {
        vendor_inventory_RecyclerView = (RecyclerView) view.findViewById(R.id.vendor_inventory_RecyclerView);
        vendor_inventory_add_btn = (Button) view.findViewById(R.id.vendor_inventory_add_btn);
        vendor_category = new ArrayList<>();
        vendor_category.add("Computer");
        vendor_category.add("Laptop");
        Adapter_Vendor_inventory_RecyclerView adapter_vendor_inventory_recyclerView=new Adapter_Vendor_inventory_RecyclerView(getActivity(),null,null,vendor_category,null,null,null);

        vendor_inventory_RecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
        vendor_inventory_RecyclerView.setAdapter(adapter_vendor_inventory_recyclerView);

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void InitialOperations() {

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void Onclicklistners() {
        vendor_inventory_add_btn_listner();
    }

    private void vendor_inventory_add_btn_listner() {
        vendor_inventory_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(),Vendor_stock_entry.class);
                startActivity(intent);

            }
        });
    }
}