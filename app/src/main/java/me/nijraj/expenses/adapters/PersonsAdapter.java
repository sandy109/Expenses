package me.nijraj.expenses.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import me.nijraj.expenses.R;
import me.nijraj.expenses.fragments.FragmentAddExpense;
import me.nijraj.expenses.fragments.FragmentExpenses;
import me.nijraj.expenses.models.Expense;
import me.nijraj.expenses.utils.Currency;

/**
 * Created by buddha on 12/15/17.
 */

public class PersonsAdapter extends RecyclerView.Adapter<PersonsAdapter.ViewHolder> {

    FragmentManager fragmentManager;
    ArrayList<PersonsAdapter.Person> data;
    String currency;

    public PersonsAdapter(FragmentManager fragmentManager, Activity activity, ArrayList<Person> data) {
        this.data = data;
        currency = Currency.getCurrency(activity);
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

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener{
        TextView name, amount;
        Button buttonLend, buttonBorrow, buttonView;
        Person person;
        private PersonsAdapter adapter;

        ViewHolder(final View itemView, PersonsAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            name = itemView.findViewById(R.id.textView_name);
            amount = itemView.findViewById(R.id.textView_amount);
            buttonLend = itemView.findViewById(R.id.button_lend);
            buttonBorrow = itemView.findViewById(R.id.button_borrow);
            buttonView = itemView.findViewById(R.id.button_view);
            itemView.setOnLongClickListener(this);
            buttonView.setOnClickListener(this);
            buttonBorrow.setOnClickListener(this);
            buttonLend.setOnClickListener(this);
        }

        void setPerson(Person person) {
            this.person = person;
        }

        @Override
        public void onClick(View view) {
            Expense.TYPE type = null;
            switch (view.getId()){
                case R.id.button_view:
                    FragmentExpenses expenses = new FragmentExpenses();
                    Bundle args = new Bundle();
                    args.putLong("id", adapter.data.get(getAdapterPosition()).getId());
                    expenses.setArguments(args);
                    FragmentTransaction fragmentTransaction = adapter.fragmentManager.beginTransaction();
                    fragmentTransaction
                            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                            .replace(R.id.fragment_container, expenses)
                            .addToBackStack("add_expense")
                            .commit();
                    break;
                case R.id.button_borrow:
                    type = Expense.TYPE.BORROWED;
                case R.id.button_lend:
                    if(type == null)
                        type = Expense.TYPE.LENT;
                    FragmentAddExpense addExpense = new FragmentAddExpense();
                    Bundle args2 = new Bundle();
                    args2.putLong("person", adapter.data.get(getAdapterPosition()).getId());
                    args2.putSerializable("type", type);
                    addExpense.setArguments(args2);
                    adapter.fragmentManager.beginTransaction()
                            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                            .replace(R.id.fragment_container, addExpense)
                            .addToBackStack("add_expense")
                            .commit();
                    break;
            }
        }

        @Override
        public boolean onLongClick(final View view) {
            CharSequence[] menuItems = new CharSequence[] { "Delete" };
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle("Choose an action");
            builder.setItems(menuItems, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    switch (i){
                        case 0:
                            me.nijraj.expenses.models.Person p = me.nijraj.expenses.models.Person.findById(me.nijraj.expenses.models.Person.class, person.getId());
                            p.delete();
                            adapter.data.remove(getAdapterPosition());
                            adapter.notifyItemRemoved(getAdapterPosition());
                            break;
                    }
                }
            });
            builder.show();
            return false;
        }

        public Person getPerson() {
            return person;
        }
    }

    @Override
    public PersonsAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_person, parent, false);
        return new ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.setPerson(data.get(position));
        holder.name.setText(data.get(position).getName());
        double amount = data.get(position).getAmount();
        holder.amount.setText(String.format(Locale.ENGLISH, "%s%.2f", currency, Math.abs(amount)));
        if(amount == 0)
            holder.amount.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorTextPrimary));
        else if(amount > 0)
            holder.amount.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorGreen));
        else
            holder.amount.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorRed));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
