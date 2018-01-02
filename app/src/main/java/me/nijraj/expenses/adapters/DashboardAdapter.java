package me.nijraj.expenses.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import me.nijraj.expenses.R;
import me.nijraj.expenses.fragments.FragmentExpenses;
import me.nijraj.expenses.models.Expense;
import me.nijraj.expenses.utils.Currency;

/**
 * Created by buddha on 12/15/17.
 */

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    List<DashboardItem> data;
    FragmentManager fragmentManager;
    String currency;

    public DashboardAdapter(List<DashboardItem> data) {
        this.data = data;
    }

    public DashboardAdapter(Activity activity, FragmentManager fragmentManager, List<DashboardItem> data) {
        this.fragmentManager = fragmentManager;
        currency = Currency.getCurrency(activity);
        this.data = data;
    }

    public static class DashboardItem{
        private String title;
        private double amount;
        private Expense.TYPE type;

        public DashboardItem(String title, Expense.TYPE type) {
            this.title = title;
            this.amount = Expense.getTotalOfType(type);
            this.type = type;
        }

        public Expense.TYPE getType() {
            return type;
        }

        public void setType(Expense.TYPE type) {
            this.type = type;
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

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textViewName, textViewAmount;
        CardView cardView;
        DashboardAdapter adapter;

        ViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textView_name);
            textViewAmount = itemView.findViewById(R.id.textView_amount);
            cardView = itemView.findViewById(R.id.cardView);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            FragmentExpenses expenses = new FragmentExpenses();
            Bundle args = new Bundle();
            args.putSerializable("type", adapter.data.get(getAdapterPosition()).getType());
            expenses.setArguments(args);
            FragmentTransaction fragmentTransaction = adapter.fragmentManager.beginTransaction();
            fragmentTransaction
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .replace(R.id.fragment_container, expenses)
                    .addToBackStack("add_expense")
                    .commit();
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
        holder.adapter = this;
        holder.textViewAmount.setText(String.format(Locale.ENGLISH, "%s%.2f", currency, data.get(position).getAmount()));
    }


    @Override
    public int getItemCount() {
        return data.size();
    }
}
