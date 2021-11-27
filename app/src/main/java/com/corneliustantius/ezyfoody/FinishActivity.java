package com.corneliustantius.ezyfoody;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.corneliustantius.ezyfoody.adapters.AdapterCart;
import com.corneliustantius.ezyfoody.adapters.AdapterFinishItem;
import com.corneliustantius.ezyfoody.adapters.AdapterProduct;
import com.corneliustantius.ezyfoody.databinding.ActivityFinishBinding;
import com.corneliustantius.ezyfoody.helpers.DatabaseHelper;
import com.corneliustantius.ezyfoody.models.CartSelectionModel;

import java.util.List;

public class FinishActivity extends AppCompatActivity {
    private ActivityFinishBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        binding = ActivityFinishBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        refresh(databaseHelper, db);
        Integer[] det = getDetails(databaseHelper, db);
        if(det[0] >= det[1]) {
            Integer newBal = det[0] - det[1];
            databaseHelper.setBalance(db, newBal);
            databaseHelper.deleteAllCartItem();
            Toast.makeText(getApplicationContext(), "Thanks for checking out :D", Toast.LENGTH_LONG).show();
        }
        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);// New activity
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
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
        binding.grandTotal.setText("Total: Rp. " + sTotal);

        RecyclerView rvCart = (RecyclerView) findViewById(R.id.rvFinish);
        AdapterFinishItem adapter = new AdapterFinishItem(its, this);

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
