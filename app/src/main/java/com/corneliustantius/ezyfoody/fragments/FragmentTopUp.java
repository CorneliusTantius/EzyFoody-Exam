package com.corneliustantius.ezyfoody.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import com.corneliustantius.ezyfoody.R;
import com.corneliustantius.ezyfoody.databinding.FragmentHomeBinding;
import com.corneliustantius.ezyfoody.databinding.FragmentTopupBinding;
import com.corneliustantius.ezyfoody.helpers.DatabaseHelper;

public class FragmentTopUp extends Fragment {

    private FragmentTopupBinding binding;
    private Toast toast;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentTopupBinding.inflate(inflater, container, false);

        float scale = getActivity().getResources().getDisplayMetrics().density;
        int topPad = (int) (140*scale + 0.5f);
        int btmPad = (int) (200*scale + 0.5f);
        int sidePad = (int) (40*scale + 0.5f);
        container.setPadding(sidePad,topPad,sidePad,btmPad);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        clearStack();
        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearStack();
                NavHostFragment.findNavController(FragmentTopUp.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strIn = binding.edittextTopup.getText().toString();
                Integer intIn = Integer.parseInt(strIn);
                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getContext());
                SQLiteDatabase db = databaseHelper.getReadableDatabase();
                Integer curr = databaseHelper.getBalance(db);
                databaseHelper.setBalance(db,curr+intIn);
                if(toast != null)
                    toast.cancel();
                String fInt = String.format("%,d", intIn);
                toast = Toast.makeText(getContext(), "Added Rp. " + fInt + " to balance!", Toast.LENGTH_SHORT);
                toast.show();
            }
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
}