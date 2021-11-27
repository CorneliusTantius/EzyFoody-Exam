package com.corneliustantius.ezyfoody;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.corneliustantius.ezyfoody.adapters.AdapterProduct;
import com.corneliustantius.ezyfoody.databinding.ActivityProductsBinding;
import com.corneliustantius.ezyfoody.fragments.FragmentHome;
import com.corneliustantius.ezyfoody.helpers.DatabaseHelper;
import com.corneliustantius.ezyfoody.models.ProductModel;

import java.util.List;

public class ProductsActivity extends AppCompatActivity {
    private ActivityProductsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String category = intent.getStringExtra(FragmentHome.EXTRA_CATEGORY);

        TextView textView = findViewById(R.id.productTitle);
        textView.setText(category);
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);
        List<ProductModel> productModels = databaseHelper.getAllProduct(databaseHelper.getReadableDatabase(), category);

        RecyclerView rvProduct = (RecyclerView) findViewById(R.id.rvProduct);
        AdapterProduct adapter = new AdapterProduct(productModels, this);
        rvProduct.setAdapter(adapter);
        rvProduct.setLayoutManager(new GridLayoutManager(this, 2));

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.buttonCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCart();
            }
        });

    }
    public void openCart(){
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
    }
}