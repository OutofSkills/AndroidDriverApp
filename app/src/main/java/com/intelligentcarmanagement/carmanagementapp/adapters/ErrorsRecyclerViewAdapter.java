package com.intelligentcarmanagement.carmanagementapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.intelligentcarmanagement.carmanagementapp.R;

public class ErrorsRecyclerViewAdapter extends RecyclerView.Adapter<ErrorsRecyclerViewAdapter.ViewHolder>{
    Context mContext;
    String[] mErrors;

    public ErrorsRecyclerViewAdapter(Context context,  String[] errors) {
        this.mErrors = errors;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ErrorsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_error_list_item, parent, false);
        ErrorsRecyclerViewAdapter.ViewHolder holder = new ErrorsRecyclerViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ErrorsRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.errorText.setText(mErrors[position]);
    }

    @Override
    public int getItemCount() {
        return mErrors.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView errorText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            errorText = itemView.findViewById(R.id.error_text);

        }
    }
}