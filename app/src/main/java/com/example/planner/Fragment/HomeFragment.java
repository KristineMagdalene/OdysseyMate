package com.example.planner.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.planner.Fragment.AddPlans.AddPlanFragment;
import com.example.planner.R;
import com.example.planner.databinding.FragmentHomeBinding;


public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPlanFragment addPlanFragment = new AddPlanFragment();
                FragmentManager fragmentManager = getParentFragmentManager(); // For Fragment within Fragment
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, addPlanFragment); // Replace "fragment_container" with your actual container ID
                fragmentTransaction.commit();
            }
        });
    }
}