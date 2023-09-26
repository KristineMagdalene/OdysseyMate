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
import com.example.planner.databinding.ItemRowHistoryBinding;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolderHistory>{
    private ItemRowHistoryBinding binding;
    private Context context;
    public ArrayList<HistoryModel> historyArrayList;

    public HistoryAdapter(ItemRowHistoryBinding binding, Context context, ArrayList<HistoryModel> planArrayList) {
        this.binding = binding;
        this.context = context;
        this.historyArrayList = planArrayList;
    }
    public class ViewHolderHistory extends RecyclerView.ViewHolder {
        TextView title = binding.tvTitle;
        TextView description = binding.tvDesc;

        public ViewHolderHistory(View itemView) {
            super(itemView);
        }
    }
    @NonNull
    @Override
    public HistoryAdapter.ViewHolderHistory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //binding ui row item event
        binding = ItemRowHistoryBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolderHistory(binding.getRoot());
    }
    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolderHistory holder, int position) {
        // Get data
        HistoryModel model = historyArrayList.get(position);
        String id = model.getId();
        String title = model.getTitle();
        String description = model.getDescription();


        // Set data
        holder.title.setText(title);
        holder.description.setText(description);


    }
    @Override
    public int getItemCount() {
        return historyArrayList.size();
    }
}
