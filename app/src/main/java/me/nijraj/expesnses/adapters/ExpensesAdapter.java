package me.nijraj.expesnses.adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.nijraj.expesnses.R;
import me.nijraj.expesnses.models.Expense;

/**
 * Created by buddha on 12/15/17.
 */

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ViewHolder> {

    List<Expense> data;

    public ExpensesAdapter() {
    }

    public ExpensesAdapter(List<Expense> data){
        this.data = data;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener{
        TextView textViewDescription, textViewAmount, textViewDate, textViewType;
        LinearLayout linearLayout;
        ExpensesAdapter adapter;

        ViewHolder(View itemView) {
            super(itemView);
            textViewDescription = itemView.findViewById(R.id.textView_description);
            textViewAmount = itemView.findViewById(R.id.textView_amount);
            textViewDate = itemView.findViewById(R.id.textView_date);
            textViewType = itemView.findViewById(R.id.textView_type);
            linearLayout = itemView.findViewById(R.id.linearLayout_container);
            linearLayout.setOnLongClickListener(this);
            linearLayout.setOnClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            CharSequence[] menuItems = new CharSequence[] { "Delete" };
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle("Choose an action");
            builder.setItems(menuItems, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    switch (i){
                        case 0:
                            deleteItem(getAdapterPosition());
                            break;
                    }
                }
            });
            builder.show();
            return true;
        }

        private void deleteItem(int adapterPosition) {
            Expense e = adapter.data.get(adapterPosition);
            e.delete();
            adapter.data.remove(adapterPosition);
            adapter.notifyItemRemoved(adapterPosition);
        }

        @Override
        public void onClick(View view) {
            adapter.data.get(getAdapterPosition()).toggleSelected();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public ExpensesAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_expense, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Expense e = data.get(position);
        String type = "";
        switch (e.getType()) {
            case BORROWED:
                holder.linearLayout.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorRedTransparent));
            case SPENT:
                holder.linearLayout.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorRedTransparent));
                holder.linearLayout.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorRedTransparent));
                break;
            case LENT:
                holder.linearLayout.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorGreenTransparent));
            case ADDED:
                holder.linearLayout.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorGreenTransparent));
                break;
        }

        type = Expense.getVerb(e.getType());
        if(e.isSelected())
            holder.linearLayout.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorSelected));
        holder.textViewDescription.setText(e.getDescription());
        holder.textViewAmount.setText("₹" + e.getAmount());
        holder.textViewType.setText(type);
        holder.adapter = this;
        Date date = new Date(e.getTimestamp());
        holder.textViewDate.setText(date.toString());
    }

    public List<Expense> getSelectedItems(){
        ArrayList<Expense> expenses = new ArrayList<>();
        for(Expense e: data){
            if(e.isSelected())
                expenses.add(e);
        }
        return expenses;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
