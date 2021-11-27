package com.corneliustantius.ezyfoody.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import com.corneliustantius.ezyfoody.R;
import com.corneliustantius.ezyfoody.databinding.FragmentAboutBinding;

public class FragmentAbout extends Fragment {
    private FragmentAboutBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentAboutBinding.inflate(inflater, container, false);
        float scale = getActivity().getResources().getDisplayMetrics().density;
        int topPad = (int) (160*scale + 0.5f);
        int btmPad = (int) (240*scale + 0.5f);
        container.setPadding(0,topPad,0,btmPad);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        clearStack();
        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearStack();
                NavHostFragment.findNavController(FragmentAbout.this)
                        .navigate(R.id.action_ThirdFragment_to_FirstFragment);
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
