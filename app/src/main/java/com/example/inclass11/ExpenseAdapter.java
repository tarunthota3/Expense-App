package com.example.inclass11;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {
    public static InteractWithMainActivity interact;
    Context ctx;
    ArrayList<Expense> expenses = new ArrayList<>();

    public ExpenseAdapter(ArrayList<Expense> expenses, Context ctx) {
        this.expenses = expenses;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ExpenseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout rv_layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(rv_layout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseAdapter.ViewHolder holder, final int position) {
        interact = (InteractWithMainActivity) ctx;

        holder.title.setText(expenses.get(position).getTitle());
        holder.cost.setText("$"+expenses.get(position).getCost());

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                interact.deleteItem(position);
                return false;
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                interact.showItem(expenses.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView cost;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            cost = itemView.findViewById(R.id.cost);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    public interface InteractWithMainActivity{
        void deleteItem(int position);
        void showItem(Expense expense);
    }
}
