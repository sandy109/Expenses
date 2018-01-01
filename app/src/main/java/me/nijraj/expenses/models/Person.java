package me.nijraj.expenses.models;

import com.orm.SugarRecord;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.nijraj.expenses.adapters.PersonsAdapter;

/**
 * Created by buddha on 12/15/17.
 */

public class Person extends SugarRecord<Person> implements Serializable{
    private String name;

    public Person(){ }

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ArrayList<PersonsAdapter.Person> loadPeople(boolean onlyNotDuck) {
        Iterator<Person> personIterator = Person.findAll(Person.class);
        ArrayList<PersonsAdapter.Person> data = new ArrayList<>();
        while (personIterator.hasNext()){
            Person person = personIterator.next();
            List<Expense> expenseList = Expense.find(Expense.class, "person = ?", person.getId().toString());
            double amount = 0;
            for (Expense e:expenseList){
                switch (e.getType()){
                    case LENT:
                        amount += e.getAmount();
                        break;
                    case BORROWED:
                        amount -= e.getAmount();
                        break;
                }
            }
            if(amount == 0 && onlyNotDuck)
                continue;
            PersonsAdapter.Person p = new PersonsAdapter.Person();
            p.setName(person.getName());
            p.setId(person.getId());
            p.setAmount(amount);
            data.add(p);
        }
        return data;
    }

    public JSONObject toJSON(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("name", name);
            obj.put("id", getId());
        }catch (JSONException e){
            return null;
        }
        return obj;
    }

    public static Person fromJSON(JSONObject o) throws JSONException{
        Person p = new Person();
        p.setId(o.getLong("id"));
        p.setName(o.getString("name"));
        return p;
    }

    public void saveRaw(){
        Person.executeQuery("INSERT INTO PERSON (ID, NAME) VALUES (?, ?)", getId().toString(), getName());
    }
}
