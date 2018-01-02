package me.nijraj.expenses.fragments;

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
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import me.nijraj.expenses.R;
import me.nijraj.expenses.adapters.DashboardAdapter;
import me.nijraj.expenses.adapters.PersonsAdapter;
import me.nijraj.expenses.models.Expense;
import me.nijraj.expenses.models.Person;


/**
 * Created by buddha on 12/15/17.
 */

public class FragmentMain extends Fragment implements View.OnClickListener {

    FragmentManager fragmentManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        getActivity().setTitle(R.string.dashboard);

        ImageButton imageButtonSpend = view.findViewById(R.id.button_spend);
        ImageButton imageButtonReceive = view.findViewById(R.id.button_receive);

        imageButtonReceive.setOnClickListener(this);
        imageButtonSpend.setOnClickListener(this);

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
        PersonsAdapter adapter = new PersonsAdapter(getFragmentManager(), getActivity(), persons);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        recyclerViewDashboard.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewDashboard.setAdapter(new DashboardAdapter(getActivity(), getFragmentManager(), loadDashboardItems()));

        recyclerView.setNestedScrollingEnabled(false);
        recyclerViewDashboard.setNestedScrollingEnabled(false);
        return view;
    }

    @Override
    public void onClick(View view) {
        FragmentAddExpense fragmentAddExpense = new FragmentAddExpense();
        Bundle args = new Bundle();
        switch (view.getId()){
            case R.id.button_spend:
                args.putSerializable("type", Expense.TYPE.SPENT);
                break;
            case R.id.button_receive:
                args.putSerializable("type", Expense.TYPE.ADDED);
                break;
        }
        fragmentAddExpense.setArguments(args);
        getFragmentManager().beginTransaction()
            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            .replace(R.id.fragment_container, fragmentAddExpense)
            .addToBackStack("add_expense")
            .commit();
    }

    private ArrayList<DashboardAdapter.DashboardItem> loadDashboardItems() {
        ArrayList<DashboardAdapter.DashboardItem> items = new ArrayList<>();
        items.add(new DashboardAdapter.DashboardItem("Lent", Expense.TYPE.LENT));
        items.add(new DashboardAdapter.DashboardItem("Spent", Expense.TYPE.SPENT));
        items.add(new DashboardAdapter.DashboardItem("Received", Expense.TYPE.ADDED));
        items.add(new DashboardAdapter.DashboardItem("Borrowed", Expense.TYPE.BORROWED));
        return items;
    }
}
