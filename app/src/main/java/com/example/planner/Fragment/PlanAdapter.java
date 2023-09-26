package com.example.planner.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.planner.PlanModel;
import com.example.planner.R;
import com.example.planner.databinding.ItemRowBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolderPlan>{
    private ItemRowBinding binding;
    private Context context;
    public ArrayList<PlanModel> planArrayList;

    public PlanAdapter(ItemRowBinding binding, Context context, ArrayList<PlanModel> planArrayList) {
        this.binding = binding;
        this.context = context;
        this.planArrayList = planArrayList;
    }
    // Inner class to hold UI in row item event
    public class ViewHolderPlan extends RecyclerView.ViewHolder {
        TextView title = binding.tvTitle;
        TextView description = binding.tvDesc;
        ImageButton moreBtn = binding.btnMore;
        ImageView image = binding.imgPicture;
        TextView currentDate = binding.textViewNoteDate;
        TextView currentTime = binding.textViewNoteTime;

        public ViewHolderPlan(View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public PlanAdapter.ViewHolderPlan onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //binding ui row item event
        binding = ItemRowBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolderPlan(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(ViewHolderPlan holder, int position) {
        // Get data
        PlanModel model = planArrayList.get(position);
        String id = model.getId();
        String title = model.getTitle();
        String description = model.getDescription();
        String imageselected = model.getImage();
        String currentDate = model.getCurrentDate();
        String currentTime = model.getCurrentTime();

        // Set data
        holder.title.setText(title);
        holder.description.setText(description);
        holder.currentTime.setText(currentTime);
        holder.currentDate.setText(currentDate);

        Glide.with(context)
                .load(imageselected)
                .into(holder.image);

        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moreOptions(model, holder);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemDetailDialogFragment dialogFragment = new ItemDetailDialogFragment(title, description,imageselected);
                dialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "ItemDetailDialogFragment");
            }
        });
    }

    private void moreOptions(PlanModel model, ViewHolderPlan holder) {
        // Get id and title
        String eventId = model.getId();
        String eventTitle = model.getTitle();
        String eventDescription = model.getDescription();
        String image = model.getImage();

// Show options
        String[] options = {"Edit", "Delete"};

// Show alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose Option")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        // Handle item clicked
                        if (position == 0) {
                            // Edit button
                            editEvent(model);
                        } else if (position == 1) {
                            // Delete button
                            // Dialog for confirmation
                            AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(context);
                            deleteBuilder.setTitle("Delete")
                                    .setMessage("Are you sure you want to delete this event?")
                                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface a, int d) {
                                            Toast.makeText(context, "Deleting...", Toast.LENGTH_SHORT).show();
                                            deleteEvent(model, holder);
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface a, int d) {
                                            a.dismiss();
                                        }
                                    })
                                    .show();
                        }
                    }
                })
                .show();
    }

    private void editEvent(PlanModel model) {
        AlertDialog.Builder editBuilder = new AlertDialog.Builder(context);
        editBuilder.setTitle("Edit Plan");

        // Inflate a custom layout for the edit dialog
        View editView = LayoutInflater.from(context).inflate(R.layout.edit_dialog, null);
        editBuilder.setView(editView);

        // Get references to views in the custom layout
        EditText titleEditText = editView.findViewById(R.id.editTitle);
        EditText descriptionEditText = editView.findViewById(R.id.editDescription);
        ImageView imageView = editView.findViewById(R.id.editImage);

        // Set initial values
        titleEditText.setText(model.getTitle());
        descriptionEditText.setText(model.getDescription());
        Glide.with(context).load(model.getImage()).into(imageView);

        // Handle "Save" button click
        editBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get updated values
                String newTitle = titleEditText.getText().toString();
                String newDescription = descriptionEditText.getText().toString();

                // Update the model (if needed)
                model.setTitle(newTitle);
                model.setDescription(newDescription);

                // Perform the update in the database (you'll need to implement this)
                updateEventInDatabase(model);

                // Notify the adapter that data has changed
                notifyDataSetChanged();

                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        // Handle "Cancel" button click
        editBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        editBuilder.show();
    }

    private void updateEventInDatabase(PlanModel model) {
        // Assuming you have a unique identifier for each event
        String id = model.getId();

        // Get a reference to the "Plans" node in the database
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Plans");

        // Get a reference to the specific event using its ID
        DatabaseReference eventRef = dbRef.child(id);

        // Set the new values for title and description
        eventRef.child("title").setValue(model.getTitle());
        eventRef.child("description").setValue(model.getDescription());

    }


    private void deleteEvent(PlanModel model, ViewHolderPlan holder) {
        // Get the reference to delete
        String id = model.getId();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("Plans");

        dbRef.child(id.toString())
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Unable to delete due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return planArrayList.size();
    }
}
