package com.example.planner.Fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.planner.PlanModel;
import com.example.planner.databinding.ItemRowBinding;

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
                //moreOptions(model, holder);
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

    @Override
    public int getItemCount() {
        return planArrayList.size();
    }
}
