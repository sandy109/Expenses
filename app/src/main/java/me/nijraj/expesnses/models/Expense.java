package me.nijraj.expesnses.models;

import android.util.Log;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import me.nijraj.expesnses.adapters.PersonsAdapter;

/**
 * Created by buddha on 12/15/17.
 */

public class Expense extends SugarRecord<Expense> {
    public enum TYPE {
        SPENT, // as in spent on stuff
        ADDED, // received from somewhere
        LENT, // lent to someone
        BORROWED, // borrowed from someone
        CREDITED,
        DEPOSITED
    }

    private Person person;
    private double amount;
    private TYPE type;
    private String description;
    private long timestamp;

    public Expense(){ }

    public Expense(double amount, Person person, TYPE type, String description, long timestamp) {
        this.person = person;
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.timestamp = timestamp;
    }

    public Expense(double amount, TYPE type, String description, long timestamp) {
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static double getTotalOfType(TYPE type){
        List<Expense> expenses = Expense.listAll(Expense.class);
        double amount = 0;
        for (Expense e:expenses) {
            if(e.getType() == type)
                amount += e.getAmount();
        }
        return amount;
    }

    public static List<Expense> getAllOfPerson(Person p){
        List<Expense> expenses = Expense.find(Expense.class, "person = ?", p.getId().toString());
        expenses.sort(new Comparator<Expense>() {
            @Override
            public int compare(Expense expense, Expense t1) {
                return (int)(t1.getTimestamp() - expense.getTimestamp());
            }
        });

        return expenses;
    }
}
