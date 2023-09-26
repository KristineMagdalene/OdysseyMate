package com.example.planner.Fragment.AddPlans;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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

import com.example.planner.Fragment.HomeFragment;
import com.example.planner.R;
import com.example.planner.databinding.FragmentAddPlanBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;


public class AddPlanFragment extends Fragment {
    private ProgressDialog progressDialog;
    private FirebaseStorage storage;
    private FirebaseDatabase database;
    private Uri selectedImage;
    private FragmentAddPlanBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddPlanBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(this.requireContext());
        progressDialog.setTitle("PLease wait");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK); // Use ACTION_PICK
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment homeFragment = new HomeFragment();
                FragmentManager fragmentManager = getParentFragmentManager(); // For Fragment within Fragment
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, homeFragment); // Replace "fragment_container" with your actual container ID
                fragmentTransaction.commit();
            }
        });

    }
    String title = "";
    String label = "";
    String description = "";
    private void validateData() {
        title = binding.etTitle.toString();
        description = binding.etDesciption.toString().trim();
        if (title.isEmpty()){
            Toast.makeText(requireContext(),"Empty Fields are not allowed", Toast.LENGTH_SHORT).show();
        }
        else if (description.isEmpty()){
            Toast.makeText(requireContext(),"Empty Fields are not allowed", Toast.LENGTH_SHORT).show();
        }
        else{
            uploadImage();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (data.getDataString() != null) {
                selectedImage = data.getData();
                binding.imgAdd.setImageURI(selectedImage);
            }
        }
    }

    private void uploadImage() {
        progressDialog.setMessage("Uploading Image...");
        progressDialog.show();

        StorageReference reference = storage.getReference().child("PlansImages")
                .child(String.valueOf(new Date().getTime()));

        reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            uploadInfo(uri.toString());
                        }
                    });
                }
            }
        });
    }
    private void uploadInfo(String imgUrl){
        progressDialog.setMessage("Saving Plan...");
        progressDialog.show();
        title = binding.etTitle.getText().toString(); // Fix: Use getText().toString() instead of toString()
        description = binding.etDesciption.getText().toString().trim(); // Fix: Use getText().toString() instead of toString()

        long timestamp = System.currentTimeMillis();
        String currentTime = getCurrentTime();
        String currentDate = getCurrentDate();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("title", title);
        hashMap.put("description", description);
        hashMap.put("image", imgUrl);
        hashMap.put("currentDate", currentDate);
        hashMap.put("currentTime", currentTime);
        hashMap.put("id", String.valueOf(timestamp));

        database.getReference("Plans")
                .child(String.valueOf(timestamp))
                .setValue(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            HomeFragment homeFragment = new HomeFragment();
                            FragmentManager fragmentManager = getParentFragmentManager(); // For Fragment within Fragment
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, homeFragment); // Replace "fragment_container" with your actual container ID
                            fragmentTransaction.commit();
                            Toast.makeText(getActivity(), "Plan Added", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private String getCurrentTime() {
        TimeZone tz = TimeZone.getTimeZone("GMT+08:00");
        Calendar c = Calendar.getInstance(tz);
        String hours = String.format("%02d", c.get(Calendar.HOUR));
        String minutes = String.format("%02d", c.get(Calendar.MINUTE));
        return hours + ":" + minutes;
    }

    @SuppressLint("SimpleDateFormat")
    private String getCurrentDate() {
        Date currentDateObject = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(currentDateObject);
    }

}