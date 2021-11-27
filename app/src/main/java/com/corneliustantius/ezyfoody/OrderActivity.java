package com.corneliustantius.ezyfoody;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.corneliustantius.ezyfoody.adapters.AdapterProduct;
import com.corneliustantius.ezyfoody.databinding.ActivityMainBinding;
import com.corneliustantius.ezyfoody.databinding.ActivityOrderBinding;
import com.corneliustantius.ezyfoody.databinding.FragmentTopupBinding;
import com.corneliustantius.ezyfoody.fragments.FragmentHome;
import com.corneliustantius.ezyfoody.helpers.DatabaseHelper;
import com.corneliustantius.ezyfoody.models.CartItemModel;
import com.corneliustantius.ezyfoody.models.ProductModel;


public class OrderActivity extends AppCompatActivity {
    private ActivityOrderBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.textviewSecond.setText("Order Food");
        Intent intent = getIntent();

        String productId = intent.getStringExtra(AdapterProduct.EXTRA_PID);
        String productName = intent.getStringExtra(AdapterProduct.EXTRA_PN);
        Integer id = Integer.parseInt(productId);


        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strIn = binding.edittextQty.getText().toString();
                if(strIn.isEmpty())
                    return;
                Integer intIn = Integer.parseInt(strIn);
                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getApplicationContext());
                SQLiteDatabase db = databaseHelper.getReadableDatabase();
                Integer[] arr = databaseHelper.checkCartItem(db, id);
                Integer currQty = arr[0];
                if(currQty == 0){
                    CartItemModel cartItemModel = new CartItemModel();
                    cartItemModel.setProductId(id);
                    cartItemModel.setQuantity(intIn);
                    databaseHelper.addCartItem(db, cartItemModel);
                }
                else{
                    currQty+=intIn;
                    Integer cartEntryId = arr[1];
                    databaseHelper.updateCartItem(db, cartEntryId, currQty);
                }
                Toast.makeText(v.getContext(), "Added " + productName + " to cart!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
