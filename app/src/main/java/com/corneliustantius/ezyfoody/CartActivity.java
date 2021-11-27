package com.corneliustantius.ezyfoody;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.corneliustantius.ezyfoody.adapters.AdapterCart;
import com.corneliustantius.ezyfoody.databinding.ActivityCartBinding;
import com.corneliustantius.ezyfoody.helpers.DatabaseHelper;
import com.corneliustantius.ezyfoody.models.CartSelectionModel;

import java.util.List;

public class CartActivity extends AppCompatActivity {
    private ActivityCartBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        refresh(databaseHelper, db);
        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.buttonCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer[] det = getDetails(databaseHelper, db);
                if(det[0] >= det[1]) {
                    startActivity(new Intent(getApplicationContext(), FinishActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "INSUFFICIENT BALANCE!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void refresh(DatabaseHelper databaseHelper, SQLiteDatabase db){
        Integer[] arr = getDetails(databaseHelper, db);
        List<CartSelectionModel> its = databaseHelper.getAllCartItems(db);
        Integer bal = arr[0], total = arr[1];
        String sBal = String.format("%,d", bal);
        String sTotal = String.format("%,d", total);
        binding.userBalance.setText("Current Balance: " + sBal);
        binding.grandTotal.setText("Rp. " + sTotal);

        RecyclerView rvCart = (RecyclerView) findViewById(R.id.rvCartItem);
        AdapterCart adapter = new AdapterCart(its, this, binding);

        rvCart.setAdapter(adapter);
        rvCart.setLayoutManager(new LinearLayoutManager(this));
    }
    public Integer[] getDetails(DatabaseHelper databaseHelper, SQLiteDatabase db){
        Integer bal = databaseHelper.getBalance(db);
        List<CartSelectionModel> its = databaseHelper.getAllCartItems(db);
        Integer total = 0;
        for(CartSelectionModel it:its){
            total += (it.getQty() * it.getPrice());
        }
        return new Integer[]{bal, total};
    }
}