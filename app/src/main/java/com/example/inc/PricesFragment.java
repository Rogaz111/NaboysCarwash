package com.example.inc;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;


public class PricesFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private PriceAdapter mAdapter;
    private List<String> mPriceList;
    private DatabaseHelper mDatabaseHelper;

    public PricesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prices, container, false);

        mDatabaseHelper = new DatabaseHelper(getActivity());

        // Initialize the price list
        mPriceList = new ArrayList<>();
        loadPriceItems();

        mRecyclerView = view.findViewById(R.id.price_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new PriceAdapter(mPriceList);
        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton fabAddItem = view.findViewById(R.id.fab_add_item);
        fabAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Add Price List Item");

                final EditText input = new EditText(getActivity());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newItem = input.getText().toString();
                        mPriceList.add(newItem);
                        savePriceItem(newItem);
                        int newItemPosition = mPriceList.size() - 1;
                        mAdapter.notifyItemInserted(newItemPosition);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        return view;
    }


    private void loadPriceItems() {
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query("prices", new String[]{"id", "price"}, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String price = cursor.getString(cursor.getColumnIndex("price"));
                mPriceList.add(price);
            }
            cursor.close();
        }
    }


    private void savePriceItem(String price) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("price", price);
        db.insert("prices", null, values);
    }


    private void deletePriceItem(String price) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        db.delete("prices", "price = ?", new String[]{price});
    }


    public static class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.ViewHolder> {

        private List<String> mPriceList;

        public PriceAdapter(List<String> priceList) {
            mPriceList = priceList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recylcer_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String price = mPriceList.get(position);
            holder.priceTextView.setText(price);
        }

        @Override
        public int getItemCount() {
            return mPriceList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView priceTextView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                priceTextView = itemView.findViewById(R.id.price_textview);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                        builder.setTitle("Remove Item");
                        builder.setMessage("Are you sure you want to remove this item?");

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int position = getAdapterPosition();
                                String removedPrice = mPriceList.get(position);
                                mPriceList.remove(position);
                                notifyItemRemoved(position);

                                if (itemView.getContext() instanceof OnPriceItemDeletedListener) {
                                    OnPriceItemDeletedListener listener = (OnPriceItemDeletedListener) itemView.getContext();
                                    listener.onPriceItemDeleted(removedPrice);
                                }
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();
                    }
                });
            }
        }
    }


}