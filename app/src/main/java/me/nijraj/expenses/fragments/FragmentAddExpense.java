package me.nijraj.expenses.fragments;

import java.util.Calendar;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import me.nijraj.expenses.R;
import me.nijraj.expenses.models.Expense;
import me.nijraj.expenses.models.Person;

/**
 * Created by buddha on 12/15/17.
 */

public class FragmentAddExpense extends Fragment {
    private Expense.TYPE type;
    private long personId;
    private EditText editTextAmount, editTextDescription;
    private Person person;
    private InputMethodManager inputMethodManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.add_expense);
        View view = inflater.inflate(R.layout.fragment_add_expense, container, false);
        editTextAmount = view.findViewById(R.id.editText_amount);
        editTextDescription = view.findViewById(R.id.editText_description);

        // Focus on amount and show keyboard
        editTextAmount.requestFocus();
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        type = (Expense.TYPE) getArguments().getSerializable("type");
        personId = getArguments().getLong("person");
        person = Person.findById(Person.class, personId);
        TextView textViewDetails = view.findViewById(R.id.textView_details);
        textViewDetails.setText(getDetailsText());
        return view;
    }

    private String getDetailsText() {
        String detail = "";
        switch (type){
            case LENT:
                detail += "Lend to ";
                break;
            case BORROWED:
                detail += "Borrow from ";
                break;
            case SPENT:
                detail += "Spend";
                break;
            case ADDED:
                detail += "Receive";
                break;
        }
        if(person != null)
            detail += person.getName();
        return detail;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_continue)
            addExpense();
        return false;
    }

    private void addExpense() {
        try {
            double amount = Double.parseDouble(editTextAmount.getText().toString());
            String description = editTextDescription.getText().toString().trim();
            long timestamp = Calendar.getInstance().getTimeInMillis();

            if(description.length() == 0){
                Toast.makeText(getContext(), "Enter some description", Toast.LENGTH_SHORT).show();
                return;
            }
            Expense expense = null;
            if(person != null)
                expense = new Expense(amount, person, type, description, timestamp);
            else
                expense = new Expense(amount, type, description, timestamp);
            expense.save();
            editTextDescription.clearFocus();
            editTextAmount.clearFocus();
            inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            getFragmentManager().popBackStack();
        }catch (NumberFormatException e){
            Toast.makeText(getContext(), "Enter a valid amount", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }
}
