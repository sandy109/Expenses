package me.nijraj.expesnses.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.nijraj.expesnses.R;
import me.nijraj.expesnses.adapters.DashboardAdapter;
import me.nijraj.expesnses.adapters.PersonsAdapter;
import me.nijraj.expesnses.models.Expense;
import me.nijraj.expesnses.models.Person;


/**
 * Created by buddha on 12/15/17.
 */

public class FragmentMain extends Fragment {

    FragmentManager fragmentManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        getActivity().setTitle(R.string.dashboard);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_person);
        RecyclerView recyclerViewDashboard = (RecyclerView) view.findViewById(R.id.recyclerView_dashboard);
        TextView textViewNothing = (TextView) view.findViewById(R.id.textView_nothing);
        ArrayList<PersonsAdapter.Person> persons = Person.loadPeople(true);
        if(persons.size() > 0){
            recyclerView.setVisibility(View.VISIBLE);
            textViewNothing.setVisibility(View.GONE);
        }else{
            recyclerView.setVisibility(View.GONE);
            textViewNothing.setVisibility(View.VISIBLE);
        }
        PersonsAdapter adapter = new PersonsAdapter(getFragmentManager(), persons);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        recyclerViewDashboard.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewDashboard.setAdapter(new DashboardAdapter(loadDashboardItems()));

        recyclerView.setNestedScrollingEnabled(false);
        recyclerViewDashboard.setNestedScrollingEnabled(false);
        return view;
    }

    private ArrayList<DashboardAdapter.DashboardItem> loadDashboardItems() {
        ArrayList<DashboardAdapter.DashboardItem> items = new ArrayList<>();
        items.add(new DashboardAdapter.DashboardItem("Lent", Expense.getTotalOfType(Expense.TYPE.LENT)));
        items.add(new DashboardAdapter.DashboardItem("Spent", Expense.getTotalOfType(Expense.TYPE.SPENT)));
        items.add(new DashboardAdapter.DashboardItem("Received", Expense.getTotalOfType(Expense.TYPE.ADDED)));
        items.add(new DashboardAdapter.DashboardItem("Borrowed", Expense.getTotalOfType(Expense.TYPE.BORROWED)));
        return items;
    }
}
