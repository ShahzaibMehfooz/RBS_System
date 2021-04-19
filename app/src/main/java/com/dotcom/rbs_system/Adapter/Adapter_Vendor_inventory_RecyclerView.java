package com.dotcom.rbs_system.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.Classes.Currency;
import com.dotcom.rbs_system.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_Vendor_inventory_RecyclerView extends RecyclerView.Adapter<Adapter_Vendor_inventory_RecyclerView.MyViewHolder> {
    Context context;
    List<String> stockSNo_list;
    List<String> stockName_list;
    List<String> stockCategory_list;
    List<String> stockPrice_list;
    List<String> stockQuantity_list;
    List<String> stockImageUrl_list;
    List<String> edit_btn_list;
    Dialog dialog_box;

    Boolean validate = true;

    public Adapter_Vendor_inventory_RecyclerView(Context context, List<String> stockSNo_list, List<String> stockName_list, List<String> stockCategory_list, List<String> stockPrice_list, List<String> quantity_vendor_inventory, List<String> stockImageUrl_list,List<String>edit_btn_list) {
        this.context = context;
        this.stockSNo_list = stockSNo_list;
        this.stockName_list = stockName_list;
        this.stockCategory_list = stockCategory_list;
        this.stockPrice_list = stockPrice_list;
        this.stockQuantity_list = quantity_vendor_inventory;
        this.stockImageUrl_list = stockImageUrl_list;
        this.edit_btn_list = edit_btn_list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.vendormainscreen_item, parent, false);
        Adapter_Vendor_inventory_RecyclerView.MyViewHolder myViewHolder = new Adapter_Vendor_inventory_RecyclerView.MyViewHolder(v);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.stockSNo_textView.setText(stockSNo_list.get(position));
        holder.stockName_textView.setText(stockName_list.get(position));
        holder.stockCategory_textView.setText(stockCategory_list.get(position));
        holder.stockPrice_textView.setText(stockPrice_list.get(position));
        holder.stockQuantity_textView.setText(stockQuantity_list.get(position));

        holder.currency_textView.setText(Currency.getInstance().getCurrency());

        Picasso.get().load(stockImageUrl_list.get(position)).into(holder.stockImage_imageView);

        holder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_box = new Dialog(context);
                dialog_box.setContentView(R.layout.alert_vendor_stock_edit);

                holder.sno_textview=(TextView)dialog_box.findViewById(R.id.sno_textview);
                holder.item_name_textview=(TextView)dialog_box.findViewById(R.id.item_name_textview);
                holder.category_editText=(TextView)dialog_box.findViewById(R.id.category_editText);
                holder.change_picture_btn=(TextView)dialog_box.findViewById(R.id.change_picture_btn);
                holder.save_btn=(TextView)dialog_box.findViewById(R.id.save_btn);
                holder.cancel_btn=(TextView)dialog_box.findViewById(R.id.cancel_btn);
                holder.item_price_editText=(EditText) dialog_box.findViewById(R.id.item_price_editText);
                holder.item_quantity_edittext=(EditText) dialog_box.findViewById(R.id.item_quantity_edittext);
                holder.stock_pic=(ImageView) dialog_box.findViewById(R.id.stock_pic);
                //TODO

                holder.sno_textview.setText(stockSNo_list.get(position));
                holder.item_name_textview.setText(stockName_list.get(position));
                holder.category_editText.setText(stockCategory_list.get(position));

                holder.item_price_editText.setText(stockPrice_list.get(position));
                holder.item_quantity_edittext.setText(stockQuantity_list.get(position));

                itemPriceTextWatcher(holder.item_price_editText);
                ItemQuantityTextWatcher(holder.item_quantity_edittext);


                holder.save_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (validate){
                            Toast.makeText(context, "Completed", Toast.LENGTH_SHORT).show();
                            dialog_box.dismiss();
                        }

                    }
                });

                holder.cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_box.dismiss();
                    }
                });



                dialog_box.show();

            }

            private void ItemQuantityTextWatcher(final EditText item_quantity_edittext) {
                item_quantity_edittext.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (stockQuantity_list.get(position).equals(item_quantity_edittext.getText().toString())){
                            item_quantity_edittext.setError("Please enter new value!");
                            validate = false;
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (item_quantity_edittext.getText().toString().isEmpty()){
                            item_quantity_edittext.setError("Please enter value!");
                            validate = false;
                        }
                    }
                });
            }

            private void itemPriceTextWatcher(final EditText item_price_editText) {
                item_price_editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (stockPrice_list.get(position).equals(item_price_editText.getText().toString())){
                            item_price_editText.setError("Please enter new value!");
                            validate = false;
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable s) {


                        if (item_price_editText.getText().toString().isEmpty()){
                            item_price_editText.setError("Please enter value!");
                            validate = false;
                        }else {
                            if (Float.parseFloat(item_price_editText.getText().toString())==0.0f){
                                item_price_editText.setError("Please enter valid value!");
                                validate = false;
                            }
                        }
                    }
                });
            }

        });
    }



    @Override
    public int getItemCount() {

        return stockCategory_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView stockImage_imageView;
        TextView stockSNo_textView, stockName_textView, stockCategory_textView, stockPrice_textView, stockQuantity_textView;
        TextView currency_textView;
        ImageButton edit_btn;
        TextView sno_textview,item_name_textview,category_editText;
        EditText item_price_editText,item_quantity_edittext;
        ImageView stock_pic;
        TextView change_picture_btn,save_btn,cancel_btn;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            stockSNo_textView = (TextView) itemView.findViewById(R.id.stockSNo_textView);
            stockName_textView = (TextView) itemView.findViewById(R.id.stockName_textView);
            stockCategory_textView = (TextView) itemView.findViewById(R.id.stockCategory_textView);
            stockPrice_textView = (TextView) itemView.findViewById(R.id.stockPrice_textView);
            stockQuantity_textView = (TextView) itemView.findViewById(R.id.stockQuantity_textView);
            currency_textView = (TextView) itemView.findViewById(R.id.currency_textView);
            stockImage_imageView = (ImageView) itemView.findViewById(R.id.stockImage_imageView);
            edit_btn = (ImageButton) itemView.findViewById(R.id.edit_btn);

        }
    }
}
