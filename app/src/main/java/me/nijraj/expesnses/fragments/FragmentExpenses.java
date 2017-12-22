package me.nijraj.expesnses.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import me.nijraj.expesnses.R;
import me.nijraj.expesnses.adapters.DashboardAdapter;
import me.nijraj.expesnses.adapters.ExpensesAdapter;
import me.nijraj.expesnses.adapters.PersonsAdapter;
import me.nijraj.expesnses.models.Expense;
import me.nijraj.expesnses.models.Person;


/**
 * Created by buddha on 12/15/17.
 */

public class FragmentExpenses extends Fragment {

    FragmentManager fragmentManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expenses, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_expenses);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        long personId = getArguments().getLong("id");
        Expense.TYPE type = (Expense.TYPE) getArguments().getSerializable("type");
        Person p = Person.findById(Person.class, personId);
        DividerItemDecoration decoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(decoration);
        if(p != null) {
            getActivity().setTitle("Transactions with " + p.getName());
            recyclerView.setAdapter(new ExpensesAdapter(Expense.getAllOfPerson(p)));
        }else if(type != null){
            switch (type){
                case SPENT:
                    getActivity().setTitle("Spendings");
                    break;
                case ADDED:
                    getActivity().setTitle("Received");
                    break;
            }
            recyclerView.setAdapter(new ExpensesAdapter(Expense.getAllOfType(type)));
        }
        return view;
    }
}
