package me.nijraj.expesnses.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import me.nijraj.expesnses.R;
import me.nijraj.expesnses.fragments.FragmentAddExpense;
import me.nijraj.expesnses.fragments.FragmentExpenses;
import me.nijraj.expesnses.models.Expense;

/**
 * Created by buddha on 12/15/17.
 */

public class PersonsAdapter extends RecyclerView.Adapter<PersonsAdapter.ViewHolder> {

    FragmentManager fragmentManager;
    ArrayList<PersonsAdapter.Person> data;

    public PersonsAdapter(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public PersonsAdapter(FragmentManager fragmentManager, ArrayList<Person> data) {
        this.data = data;
        this.fragmentManager = fragmentManager;
    }

    public static class Person{
        private String name;
        private long id;
        private double amount;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, amount;
        Button buttonLend, buttonBorrow, buttonView;

        ViewHolder(final View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView_name);
            amount = itemView.findViewById(R.id.textView_amount);
            buttonLend = itemView.findViewById(R.id.button_lend);
            buttonBorrow = itemView.findViewById(R.id.button_borrow);
            buttonView = itemView.findViewById(R.id.button_view);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(itemView.getContext(), getAdapterPosition() + "", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
    }

    @Override
    public PersonsAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_person, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.buttonLend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddExpense(position, Expense.TYPE.LENT);
            }
        });

        holder.buttonBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddExpense(position, Expense.TYPE.BORROWED);
            }
        });
        
        holder.buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openExpenses(position);
            }
        });

        holder.name.setText(data.get(position).getName());
        double amount = data.get(position).getAmount();
        holder.amount.setText("₹" + Math.abs(amount));
        if(amount == 0)
            holder.amount.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorTextPrimary));
        else if(amount > 0)
            holder.amount.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorGreen));
        else
            holder.amount.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorRed));
    }

    private void openExpenses(int position) {
        FragmentExpenses expenses = new FragmentExpenses();
        Bundle args = new Bundle();
        args.putLong("id", data.get(position).getId());
        expenses.setArguments(args);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.fragment_container, expenses)
                .addToBackStack("add_expense")
                .commit();
    }

    private void openAddExpense(int position, Expense.TYPE type) {
        FragmentAddExpense addExpense = new FragmentAddExpense();
        Bundle args = new Bundle();
        args.putLong("person", data.get(position).getId());
        args.putSerializable("type", type);
        addExpense.setArguments(args);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.fragment_container, addExpense)
                .addToBackStack("add_expense")
                .commit();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
