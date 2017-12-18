package me.nijraj.expesnses.adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import me.nijraj.expesnses.R;
import me.nijraj.expesnses.fragments.FragmentAddExpense;

/**
 * Created by buddha on 12/15/17.
 */

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    List<DashboardItem> data;

    public DashboardAdapter(List<DashboardItem> data) {
        this.data = data;
    }

    public static class DashboardItem{
        private String title;
        private double amount;

        public DashboardItem(String title, double amount) {
            this.title = title;
            this.amount = amount;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewName, textViewAmount;

        ViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textView_name);
            textViewAmount = itemView.findViewById(R.id.textView_amount);
        }
    }

    @Override
    public DashboardAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_dashboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textViewName.setText(data.get(position).getTitle());
        holder.textViewAmount.setText("₹" + data.get(position).getAmount());
    }


    @Override
    public int getItemCount() {
        return data.size();
    }
}
