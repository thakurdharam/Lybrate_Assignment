package com.example.assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class fetchdataAdapter extends RecyclerView.Adapter<fetchdataAdapter.ViewHolder> implements Filterable {
    private ArrayList<fetchdata> Fetch_data;
    private ArrayList<fetchdata> Items_List;
    private Context context;

    public fetchdataAdapter(ArrayList<fetchdata> mFetchdata, Context context) {
        this.Fetch_data = mFetchdata;
        this.context = context;
        this.Items_List = new ArrayList<>(mFetchdata);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dataview, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        fetchdata currentItems = Fetch_data.get(position);
        holder.Restaurant_Name.setText(currentItems.getName());
        holder.Cuisines_Name.setText(currentItems.getCuisines());
    }

    @Override
    public int getItemCount() {
        return Fetch_data.size();
    }

    @Override
    public Filter getFilter() {
        return Filter_data;
    }

    private Filter Filter_data = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<fetchdata> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(Items_List);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (fetchdata item : Items_List) {
                    if (item.getCuisines().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            Fetch_data.clear();
            Fetch_data.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView Restaurant_Name;
        public TextView Cuisines_Name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Restaurant_Name = itemView.findViewById(R.id.txtvw2);
            Cuisines_Name = itemView.findViewById(R.id.txtvw4);
        }
    }
}
