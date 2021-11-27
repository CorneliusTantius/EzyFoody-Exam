package com.corneliustantius.ezyfoody.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import com.corneliustantius.ezyfoody.CartActivity;
import com.corneliustantius.ezyfoody.ProductsActivity;
import com.corneliustantius.ezyfoody.R;
import com.corneliustantius.ezyfoody.databinding.FragmentHomeBinding;
import com.corneliustantius.ezyfoody.helpers.DatabaseHelper;

public class FragmentHome extends Fragment {

    private FragmentHomeBinding binding;
    private DatabaseHelper db;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        this.db = DatabaseHelper.getInstance(this.getActivity());
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        float scale = getActivity().getResources().getDisplayMetrics().density;
        int topPad = (int) (160*scale + 0.5f);
        int btmPad = (int) (240*scale + 0.5f);
        container.setPadding(0,topPad,0,btmPad);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        clearStack();
        binding.buttonTopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearStack();
                NavHostFragment.findNavController(FragmentHome.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
        binding.buttonAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearStack();
                NavHostFragment.findNavController(FragmentHome.this)
                        .navigate(R.id.action_FirstFragment_to_ThirdFragment);
            }
        });

        binding.buttonFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { openProduct("Food"); }
        });
        binding.buttonDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { openProduct("Drink"); }
        });
        binding.buttonSnacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { openProduct("Snacks"); }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        clearStack();
        binding = null;
    }
    public void clearStack(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }
    public static final String EXTRA_CATEGORY = "com.example.myfirstapp.CATEGORY";
    public void openProduct(String category){
        Intent intent = new Intent(this.getActivity(), ProductsActivity.class);
        intent.putExtra(EXTRA_CATEGORY, category);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        clearStack();
    }
}