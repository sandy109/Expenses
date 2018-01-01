package me.nijraj.expesnses.models;

import android.util.Log;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created by buddha on 12/15/17.
 */

public class Expense extends SugarRecord<Expense>  implements Serializable{

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

    @Ignore
    private boolean selected;

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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public TYPE getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public boolean isSelected() {
        return selected;
    }

    public void toggleSelected() {
        selected = !selected;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public static String getVerb(TYPE type) {
        switch (type){
            case BORROWED:
                return "borrowed";
            case LENT:
                return "lent";
            case SPENT:
                return "spent";
            case ADDED:
                return "received";
            default:
                return "";
        }
    }

    public static TYPE getType(String type){
        switch (type){
            case "LENT":
                return TYPE.LENT;
            case "BORROWED":
                return TYPE.BORROWED;
            case "ADDED":
                return TYPE.ADDED;
            case "SPENT":
                return TYPE.SPENT;
            default:
                return TYPE.SPENT;
        }
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

    public static List<Expense> getAllOfType(TYPE type){
        Iterator<Expense> e = Expense.findAll(Expense.class);
        List<Expense> expenses = new ArrayList<>();
        while (e.hasNext()){
            Expense exp = e.next();
            if(exp.getType() == type)
                expenses.add(exp);
        }
        return expenses;
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

    public JSONObject toJSON(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", getId());
            try {
                obj.put("person", person.getId());
            }catch(NullPointerException ex){
                obj.put("person", "");
            }
            obj.put("amount", amount);
            obj.put("type", type);
            obj.put("description", description);
            obj.put("timestamp", timestamp);
        }catch (JSONException e){
            return null;
        }
        return obj;
    }

    public static Expense fromJSON(JSONObject o) throws JSONException {
        Expense e = new Expense(
                o.getDouble("amount"),
                Expense.getType(o.getString("type")),
                o.getString("description"),
                o.getLong("timestamp")
        );
        e.setId(o.getLong("id"));
        try {
            Person p = new Person();
            p.setId(o.getLong("person"));
            e.setPerson(p);
        }catch (Exception ex){

        }
        return e;
    }
    public void saveRaw(){
        Expense.executeQuery("INSERT INTO EXPENSE (ID, AMOUNT, TYPE, DESCRIPTION, TIMESTAMP, PERSON) VALUES (?, ?, ?, ?, ?, ?)",
                getId().toString(),
                getAmount() + "",
                getType().toString(),
                getDescription(),
                getTimestamp() + "",
                person == null ? "" : person.getId().toString()
        );
    }
}
