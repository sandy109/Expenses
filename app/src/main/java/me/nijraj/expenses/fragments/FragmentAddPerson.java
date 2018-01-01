package me.nijraj.expenses.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import me.nijraj.expenses.R;
import me.nijraj.expenses.models.Person;

/**
 * Created by buddha on 12/15/17.
 */

public class FragmentAddPerson extends Fragment {
    private EditText editTextName;
    private FragmentManager fragmentManager;
    private InputMethodManager inputMethodManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.add_person);
        fragmentManager = getFragmentManager();
        View view = inflater.inflate(R.layout.fragment_add_person, container, false);
        editTextName = view.findViewById(R.id.editText_name);
        editTextName.requestFocus();
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.done, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_continue)
            addPerson();
        return false;
    }

    private void addPerson() {
        String name = editTextName.getText().toString().trim();
        if(name.length() == 0){
            Toast.makeText(getContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
            return;
        }
        Person person = new Person(name);
        person.save();
        editTextName.clearFocus();
        inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        fragmentManager.popBackStack();
    }
}
