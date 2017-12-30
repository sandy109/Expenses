package me.nijraj.expesnses.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import me.nijraj.expesnses.R;
import me.nijraj.expesnses.adapters.PersonsAdapter;
import me.nijraj.expesnses.models.Person;

/**
 * Created by buddha on 12/15/17.
 */

public class FragmentPeople extends Fragment {
    FragmentManager fragmentManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people, container, false);
        setHasOptionsMenu(false);
        getActivity().setTitle(R.string.people);
        fragmentManager = getFragmentManager();
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_add_person);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new FragmentAddPerson();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack("add_person")
                        .commit();
            }
        });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_person);
        recyclerView.setNestedScrollingEnabled(false);
        PersonsAdapter adapter = new PersonsAdapter(getFragmentManager(), Person.loadPeople(false));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }
}
