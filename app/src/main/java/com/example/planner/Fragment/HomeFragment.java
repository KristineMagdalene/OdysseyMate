package com.example.planner.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Toast;

import com.example.planner.Fragment.AddPlans.AddPlanFragment;
import com.example.planner.PlanModel;
import com.example.planner.R;
import com.example.planner.databinding.FragmentHomeBinding;
import com.example.planner.databinding.ItemRowBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collection;


public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private ProgressDialog progressDialog;
    private ArrayList<PlanModel> planArrayList;
    private FirebaseDatabase database;
    private PlanAdapter adapter;
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
        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setTitle("PLease wait");
        progressDialog.setCanceledOnTouchOutside(false);
        // Initialize database
        database = FirebaseDatabase.getInstance();
        getPlans();

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
                    PlanModel model = createPlanModelFromDataSnapshot(data);

                    // Add to array at the beginning
                    planArrayList.add(0, model);

                }

                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            adapter = new PlanAdapter(ItemRowBinding.inflate(getLayoutInflater()), requireContext(), planArrayList);
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
    private PlanModel createPlanModelFromDataSnapshot(DataSnapshot dataSnapshot) {
        String id = dataSnapshot.child("id").getValue(String.class);
        String title = dataSnapshot.child("title").getValue(String.class);
        String description = dataSnapshot.child("description").getValue(String.class);
        String image = dataSnapshot.child("image").getValue(String.class);
        String currentDate = dataSnapshot.child("currentDate").getValue(String.class);
        String currentTime = dataSnapshot.child("currentTime").getValue(String.class);
        return new PlanModel(id, title,description,image,currentDate,currentTime);
    }
}