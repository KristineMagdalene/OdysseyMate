package com.example.planner.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.planner.PlanModel;
import com.example.planner.R;
import com.example.planner.databinding.FragmentHistoryBinding;
import com.example.planner.databinding.ItemRowBinding;
import com.example.planner.databinding.ItemRowHistoryBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HistoryFragment extends Fragment {
    private FragmentHistoryBinding binding;
    private ArrayList<HistoryModel> planArrayList;
    private FirebaseDatabase database;
    private HistoryAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize database
        database = FirebaseDatabase.getInstance();
        getPlans();
    }
    private void getPlans() {
        //initialize
        planArrayList = new ArrayList<>();

        DatabaseReference dbRef = database.getReference("Plans");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear list
                planArrayList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    // Data as model
                    HistoryModel model = createPlanModelFromDataSnapshot(data);

                    // Add to array at the beginning
                    planArrayList.add(0, model);

                }

                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            adapter = new HistoryAdapter(ItemRowHistoryBinding.inflate(getLayoutInflater()), requireContext(), planArrayList);
                            binding.recyclerview.setHasFixedSize(true);
                            binding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                            binding.recyclerview.setAdapter(adapter);
                        }
                    });
                }

                // Set up adapter
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private HistoryModel createPlanModelFromDataSnapshot(DataSnapshot dataSnapshot) {
        String id = dataSnapshot.child("id").getValue(String.class);
        String title = dataSnapshot.child("title").getValue(String.class);
        String description = dataSnapshot.child("description").getValue(String.class);
        return new HistoryModel(id, title,description);
    }
}